package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.MainScreen;
import es.source.code.activity.R;
import es.source.code.br.ButtonBroadcastReceiver;
import es.source.code.model.Food;

public class UpdateService extends IntentService {
    private ArrayList<Food> mFoodList = new ArrayList<Food>();

    public final static String ACTION_BUTTON = "es.source.code.br.ButtonBroadcastReceiver";
    private NotificationManager notifyManager;
    private ButtonBroadcastReceiver buttonBroadcastReceiver;
    private static final int FLAG_RECEIVER_INCLUDE_BACKGROUND=0x01000000;

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String channelId="im_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this,channelId);
        mbuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(channelId)
                .build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(channelId, "im_channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }
        startForeground(101, mbuilder.build());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("UPdate","onHandleIntent()");

        requestNewFood();
        //checkNewFood();
        playNotification();
    }

    public void checkNewFood(){
        Food food =new Food("蒜苗肉丝",10, 10, 0, R.drawable.food_cold_lbhg);
        mFoodList.add(food);
        Intent intent = new Intent(this, FoodDetailed.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("foods",mFoodList);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        sendNotification(food, pendingIntent);
    }

    private void sendNotification(Food food, PendingIntent intent) {
        String channelId="default";
        String price = String.valueOf(food.getPrice());
        String content = getString(R.string.notify_new_food_content, food.getFoodName(), price);
        Bitmap foodBitMap = BitmapFactory.decodeResource(getResources(), food.getImageId(), new BitmapFactory.Options());

        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(foodBitMap)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(content)
                .setContentIntent(intent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setChannelId(channelId)
                .build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, "update", NotificationManager.IMPORTANCE_DEFAULT);
            notifyManager.createNotificationChannel(mChannel);
        }
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }

    public void requestNewFood(){
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        try {
            String urlStr = "http://10.0.2.2:8080/SCOSServer/FoodUpdateService";
            url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.connect();


            if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while((line=br.readLine())!=null){
                    buffer.append(line);
                }
                in.close();
                br.close();
                String result = buffer.toString();

                sendUpdateNotification(result);
            }else{
            }
        } catch (Exception e) {
        }finally{
            httpURLConnection.disconnect();
        }
    }

    public void sendUpdateNotification(String res) throws JSONException, DocumentException {
        String channelId="default";
        //解析json数据
        JSONObject jsonnote = new JSONObject(res);
        int foodlistlength=0;
        long time1=System.currentTimeMillis();
        try {
            JSONArray jsonArray= jsonnote.getJSONArray("foods");
            foodlistlength = jsonArray.length();
            Food food = new Food();
            food.setImageId(0);
            food.setOrderState(0);
            Log.i("Len",foodlistlength+"");
            for (int i = 0; i <foodlistlength;i++){
                food.setFoodName(((JSONObject)jsonArray.get(i)).getString("foodname"));
                food.setPrice(((JSONObject)jsonArray.get(i)).getInt("foodprice"));
                food.setStore(((JSONObject)jsonArray.get(i)).getInt("foodstore"));

                mFoodList.add(food);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        long  time2 = System.currentTimeMillis();
        Log.i("JSON解析事件:",(time2-time1)+"");
        //解析xml数据
/*
        Food food =new Food();
        food.setImageId(0);
        food.setOrderState(0);
        long  time3 = System.currentTimeMillis();

        org.dom4j.Document doc = DocumentHelper.parseText(res);
        Element root = doc.getRootElement();

        List<Element> foodElements = root.elements();
        int foodlistlength = foodElements.size();
        for(Element foodelement: foodElements){
            food.setFoodName(foodelement.elementText("foodname"));
            food.setPrice(Integer.parseInt( foodelement.elementText("foodprice")));
            food.setStore(Integer.parseInt( foodelement.elementText("foodstore")));
            mFoodList.add(food);
        }
        Log.i("len",foodlistlength+"");


        long time4 = System.currentTimeMillis();
        Log.i("XML解析事件:",(time4-time3)+"");*/

        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        Notification notification = builder.build();
        String content = "新品上架："+foodlistlength;

        buttonBroadcastReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ACTION_BUTTON);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(buttonBroadcastReceiver,intentFilter);

        Intent buttonIntent = new Intent(ACTION_BUTTON);
        buttonIntent.putExtra("id",1);

        PendingIntent cancelIntent = PendingIntent.getBroadcast(this,5,buttonIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action action= new NotificationCompat.Action.Builder(0,"清除",cancelIntent)
                .build();

        Intent activityIntent = new Intent(this, MainScreen.class);
        activityIntent.putExtra("from","UpdateService");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.addAction(action)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setChannelId(channelId)
                .build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, "update", NotificationManager.IMPORTANCE_DEFAULT);
            notifyManager.createNotificationChannel(mChannel);
        }
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Log.i("TAG","启动");
        notifyManager.notify(1, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(buttonBroadcastReceiver);
    }

    public void playNotification(){
        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, ringtone);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }


    /*
    * 广播监听按钮点击事件
    * */
/*    public  class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Tag1","清除");
            if (action.equals(ACTION_BUTTON)) {
                Log.i("Tag","清除");
                    notifyManager.cancel(1);
            }
        }
    }*/
}
