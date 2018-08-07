package phone.ktv.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import phone.ktv.app.App;
import phone.ktv.tootls.SPUtil;

public class SocketService extends Service {
    private final String tag = "MyService";
//    socketIO
//    端口：8000
//
//    注册：
//    register
//    参数：String mac  手机号
//    int type  3  手机端ktv
//
//    提醒续费：
//    发送数据：
//    事件 warning  String类型（直接显示出来，并且给个确认键让用户确认）
//
//    广告：rollAdList
//            返回数据类型
//    List<RollAdEntity> rollAdList


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private App app;
    private SPUtil spUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (App) getApplication();
        spUtil = new SPUtil(this);
        websocket();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Socket socket;

//    private AdList adLists;

    private void websocket() {

        try {
            socket = IO.socket(App.socketurl);
            socket.on("warning", new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        final String json = arg0[0].toString();
                        Log.d(tag + "---" + "warning", json);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                warning(json);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            socket.on("rollAdList", new Emitter.Listener() {

                public void call(Object... arg0) {//广告
                    // TODO Auto-generated method stub
                    try {
                        String json = arg0[0].toString();
                        Log.d(tag + "---" + "adList", json);
//                        System.out.println("初始化广告");
//                        AdList tmp = new ArrayList<>(Arrays.asList(App.gson.fromJson(json, AdList[].class))).get(0);
//                        adLists = tmp;
//                        app.setAdLists(adLists);
//                        adList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.on("add_ad", new Emitter.Listener() {

                public void call(Object... arg0) {//新增广告
                    // TODO Auto-generated method stub
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.on("update_ad", new Emitter.Listener() {

                public void call(Object... arg0) {//更新广告
                    // TODO Auto-generated method stub
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.on("delete_ad", new Emitter.Listener() {

                public void call(Object... arg0) {//删除广告
                    // TODO Auto-generated method stub
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        System.out.println("Socket连接成功----online");
                        register();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    System.out.println("Socket断开连接----offline");
                }

            });
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    System.out.println("Socket连接失败----online fail");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.connect();
    }
//    删除：delete_ad
//    返回：adId

    private void delete_ad() {
//        Intent intent = new Intent(App.DeleteAdList);
////        Bundle bundle = new Bundle();
////        bundle.putSerializable("key", (Serializable) adLists);
////        intent.putExtras(bundle);
//        sendBroadcast(intent);
    }

    //    修改：update_ad
//    返回数据：AdGroupEntity adGroupEntity
    private void update_ad() {
//        Intent intent = new Intent(App.UpdateAdList);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("key", (Serializable) adLists);
//        intent.putExtras(bundle);
//        sendBroadcast(intent);
    }

    //    新增：add_ad
//    返回数据：AdGroupEntity adGroupEntity
    private void add_ad() {
//        Intent intent = new Intent(App.UpdateAdList);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("key", (Serializable) adLists);
//        intent.putExtras(bundle);
//        sendBroadcast(intent);
    }

    //    广告：adList
//            返回数据类型
    private void adList() {
//        Intent intent = new Intent(App.InitAdList);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("key", (Serializable) adLists);
//        intent.putExtras(bundle);
//        sendBroadcast(intent);
    }


    private void register() {
        socket.emit("register", spUtil.getString("telPhone", ""), 3);
    }

    //    提醒续费：
//    发送数据：
//    warning  String类型（直接显示出来，并且给个确认键让用户确认）
    private AlertDialog.Builder builder;
    AlertDialog ad;

    private void warning(String json) {
//        Toast.makeText(MyService.this, json, Toast.LENGTH_SHORT).show();
        builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(json)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        ad = builder.create();
        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        ad.show();
    }

}
