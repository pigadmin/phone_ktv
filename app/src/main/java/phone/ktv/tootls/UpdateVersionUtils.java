package phone.ktv.tootls;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import phone.ktv.BuildConfig;
import phone.ktv.app.App;
import phone.ktv.bean.AJson;
import phone.ktv.bean.UpdateVerBean;

/**
 * 版本更新Utils
 */
public class UpdateVersionUtils {

    private static final String TAG = "UpdateVersionUtils";

    private SVProgressHUD mSvProgressHUD;
    private Context context;

    private static final int Request_Success = 30;//请求成功
    private static final int Request_Failure = 40;//请求失败
    private static final int Version_New_Date = 80;//版本已最新

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Request_Success:
                    String json = GsonJsonUtils.parseObj2Json(((AJson) msg.obj).getData());
                    UpdateVerBean verBean = GsonJsonUtils.parseJson2Obj(json, UpdateVerBean.class);
                    state(verBean);
                    break;
                case Request_Failure:
                    ToastUtils.showLongToast(context, "更新失败:" + msg.obj);
                    break;
                case Version_New_Date:
                    ToastUtils.showLongToast(context, "当前版本已最新");
                    break;
            }
            mSvProgressHUD.dismiss();
        }
    };

    public UpdateVersionUtils(SVProgressHUD mSvProgressHUD, Context context) {
        this.context = context;
        this.mSvProgressHUD = mSvProgressHUD;
    }

    /**
     * 提交登录数据
     */
    public void submLoginData() {
        mSvProgressHUD.showWithStatus("正在检查版本号...");
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("version", "" + getVersionCode());//本地版本号
        String url = App.getRqstUrl(App.headurl + "upgrade/get", weakHashMap);

        Logger.i(TAG, "url.." + url);

        if (NetUtils.hasNetwork(context)) {
            OkhttpUtils.doStart(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //返回失败
                    mHandler.obtainMessage(Request_Failure, e.getMessage()).sendToTarget();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    String s = response.body().string();
                    Logger.i(TAG, "版本更新s.." + s);
                    AJson aJson = GsonJsonUtils.parseJson2Obj(s, AJson.class);
                    if (aJson != null) {
                        if (aJson.getCode() == 0) {
                            if (aJson.getData() != null) {
                                mHandler.obtainMessage(Request_Success, aJson).sendToTarget();
                            } else {
                                mHandler.sendEmptyMessage(Version_New_Date);
                            }
                        } else {
                            mHandler.obtainMessage(Request_Failure, aJson.getMsg()).sendToTarget();
                        }
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            mSvProgressHUD.dismiss();
            ToastUtils.showShortToast(context, "网络连接异常,请检查网络配置");
        }
    }

    private void state(UpdateVerBean verBean) {
        if (verBean != null) {
            loadNewVersionProgress(verBean.path);
            Logger.d(TAG, "verBean.path.." + verBean.path);
        }
    }

    /**
     * 下载APK文件(网页版更新)
     *
     * @param download_url
     */
    protected void downloadApk(String download_url, Context context) {
        Uri uri = Uri.parse(download_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 下载新版本程序
     */
    private void loadNewVersionProgress(final String apkPath) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        //启动子线程下载任务
        new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    File file = downloadFile(apkPath, "com.ktv.phone", pd);
                    sleep(1000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                    Looper.loop();
                } catch (Exception e) {
                    //下载apk失败
                    Toast.makeText(context, "下载新版本失败", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从服务器下载最新更新文件
     *
     * @param path 下载路径
     * @param pd   进度条
     * @return
     * @throws Exception
     */
    private static File downloadFile(String path, String appName, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            String fileName = Environment.getExternalStorageDirectory() + File.separator
                    + appName + ".apk";
            File file = new File(fileName);
            // 目录不存在创建目录
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            throw new IOException("未发现有SD卡");
        }
    }

    /**
     * 安装apk
     */
    public void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkFileUri = FileProvider.getUriForFile(context.getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".fileprovider", file);
            intent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 返回版本号
     *
     * @return 非0 则代表获取成功
     */
    public int getVersionCode() {
        //1,包管理者对象packageManager
        PackageManager pm = context.getPackageManager();
        //2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称:清单文件中
     *
     * @return 应用版本名称    返回null代表异常
     */
    public String getVersionName(Context context) {
        //1,包管理者对象packageManager
        PackageManager pm = context.getPackageManager();
        //2,从包的管理者对象中,获取指定包名的基本信息(版本名称,版本号),传0代表获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //3,获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
