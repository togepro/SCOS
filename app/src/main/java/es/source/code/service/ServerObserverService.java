package es.source.code.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import es.source.code.model.Food;

public class ServerObserverService extends Service{
    private static final int MAG_UPDATE = 1;
    private static final int MAG_UNUPDATE = 0;
    private IBinder ibinder = new ServiceBinder();
    private CMessageHandler cMessageHandler = new CMessageHandler();
    private  Messenger mServiceMessager = new Messenger(cMessageHandler);
    private Messenger cMessenger;

    private ArrayList<Food> foodlist;
    private Bundle bundle;

    private boolean serviceRunning=false;

    private class CMessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MAG_UPDATE:
                    serviceRunning=true;
                    cMessenger = msg.replyTo;

                    Log.i("Message","接受来自客户端的消息"+msg.what);
                    bundle = msg.getData();
                    progressData();

                    break;
                case  MAG_UNUPDATE:
                    serviceRunning=false;
                    Log.i("Message","接受来自客户端的消息"+msg.what);

                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void progressData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                foodlist = bundle.getParcelableArrayList("food");
                try {
                    Thread.sleep(300);
                }catch(Exception e){
                    e.printStackTrace();
                }
                sendToActivityHandler();
            }
        }).start();
    }


    public void sendToActivityHandler(){
        Message message= Message.obtain();
        message.what=10;
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("food",foodlist);
        message.setData(bundle);
        try {
            cMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG","call onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("tag","onBind");
        return mServiceMessager.getBinder();
    }

    public class ServiceBinder extends Binder {
        public ServerObserverService getService() {
            return ServerObserverService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("onUnbind","成功解绑");
        return super.onUnbind(intent);
    }
}
