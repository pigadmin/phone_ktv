package phone.ktv.tootls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;

/**
 * Android6.0动态权限请求工具类
 */
public class PermissionRequestUtil {

    private static final String TAG = "PermissionRequestUtil";

    /**
     * 默认构造方法
     */
    private PermissionRequestUtil() {

    }

    /**
     * 定义一个接口，处理回调结果
     */
    public interface PermissionRequestListener {
        /**
         * 处理回调结果的接口方法
         *
         * @param reqCode 请求码
         * @param isAllow 是否被授权(eg:如有N个权限，只要有其中一个权限没有被授予,那么isAllow为false，否则为true)
         */
        void onPermissionReqResult(int reqCode, boolean isAllow);
    }

    /**
     * 处理回调结果
     *
     * @param listener     回调结果接口方法
     * @param reqCode      请求码
     * @param grantResults 权限请求结果集
     */
    public static void solvePermissionRequest(PermissionRequestListener listener, int reqCode, int[] grantResults) {
        if (grantResults == null) {
            return;
        }
        Logger.d(TAG, "grantResults.length=" + grantResults.length);
        boolean isAllow = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllow = false;
                break;
            }
        }
        if (listener == null) {
            return;
        }
        //调用接口
        listener.onPermissionReqResult(reqCode, isAllow);
    }

    /**
     * @param context     上下文
     * @param permissions 权限数组
     * @param requestCode 请求码
     * @return 检查是否需要进行权限动态申请 true：有此权限 false：无权限需要申请
     */
    public static boolean judgePermissionOver23(Context context, String[] permissions, int requestCode) {
        try {
            Logger.d(TAG, "Android api=" + Build.VERSION.SDK_INT);
            if (permissions == null || permissions.length == 0) {
                return true;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                ArrayList<String> checkResult = new ArrayList<>();
                for (String permission : permissions) {
                    if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        checkResult.add(permission);
                    }
                }
                if (checkResult.size() == 0) {
                    return true;
                }
                int i = 0;
                String[] reqPermissions = new String[checkResult.size()];
                for (String string : checkResult) {
                    reqPermissions[i] = string;
                    i++;
                }
                ((Activity) context).requestPermissions(reqPermissions, requestCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 打开 APP 的详情设置
     */
    public static void openAppDetails(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("App申请的权限已被拒绝,为了能正常使用,请进入设置--权限管理打开相关权限");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 系统悬浮窗权限(特殊)
     */
    public static void showSuspeWindow(final Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(activity)) {
                new AlertDialog.Builder(activity)
                        .setTitle("提示")
                        .setCancelable(false)
                        .setMessage("悬浮窗权限,为了能正常使用,请进入设置--权限管理打开")
                        .setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + activity.getPackageName()));
                                activity.startActivityForResult(intent, Contants.PermissRequest);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        }
    }
}