package es.source.code.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.apache.commons.mail.HtmlEmail;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.adapter.MainScreenAdapter;
import es.source.code.model.User;
import es.source.code.utils.MailEvent;

public class SCOSHelper extends AppCompatActivity {
    private User user;

    private GridView mgridView;
    private MainScreenAdapter madapter;
    private List listitem;
    private Intent intent;
    private SmsManager smsManager;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.helper);
         setData();
         initView();

         //注册EventBus
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setData(){
        listitem = new ArrayList();
        int icno[] = { R.drawable.protocl, R.drawable.system, R.drawable.phone, R.drawable.message,R.drawable.mail};
        //图标下的文字
        String name[]={"用户使用协议","关于系统","电话人工帮助","短信帮助","邮件帮助"};
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            listitem.add(map);
        }
    }



    public void initView(){
/*        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1) {
                    Toast.makeText(SCOSHelper.this,"求助邮件已发送成功",Toast.LENGTH_SHORT).show();
                }
                Log.i("what",msg.what+"");
            }
        };*/



        mgridView =  findViewById(R.id.helper_gridview);
        madapter = new MainScreenAdapter(this,listitem);
        mgridView.setAdapter(madapter);
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent();
                        break;
                    case 1:
                        break;
                    case 2:
                        callUp();
                        break;
                    case 3:
                        sendMessage();
                        break;
                    case 4:
                        sendMail();
                        break;
                }
            }
        });

    }

    /*
    * 检查权限
    * */
    private  void callUp(){
        if (isLackPermission(Manifest.permission.CALL_PHONE)) {
            requestPermission(Manifest.permission.CALL_PHONE);
        } else{
            doCallingUp();
        }
    }

    /*
    * 执行打电话
    * */
    private void doCallingUp(){
        intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:5554"));
        startActivity(intent);
    }


    /*
    * 发送消息
    * */
    private void  sendMessage(){
        if (isLackPermission(Manifest.permission.SEND_SMS)) {
            requestPermission(Manifest.permission.SEND_SMS);
        } else {
            doSendMessage();
        }
    }


    /*
    * 执行发短信
    * */
    private void doSendMessage(){
        intent = new Intent();
        smsManager = SmsManager.getDefault();
        PendingIntent pendingIntent= PendingIntent.getActivity(SCOSHelper.this,0,new Intent(),0);
        smsManager.sendTextMessage("5554",null,"test scos helper",pendingIntent,null);
        Toast.makeText(SCOSHelper.this,"“求助短信发送成功",Toast.LENGTH_SHORT).show();
    }

    /*
    * 发送邮件
    * */
    private void sendMail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Message message = handler.obtainMessage();
                try{
                    doSendMail();
                    //message.what=1;
                    //handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //使用EventBus发送信息
                EventBus.getDefault().post(new MailEvent("1"));
            }
        }).start();
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onMailThread(MailEvent mailEvent){
        String receivedmsg = mailEvent.getMsg();
        if(receivedmsg.equals("1")){
            Toast.makeText(SCOSHelper.this,"求助邮件已发送成功",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(SCOSHelper.this,"求助邮件已发送失败",Toast.LENGTH_SHORT).show();
        }

    }


    /*
    *
    * 执行发送邮件
    *
    * */
    private void doSendMail() throws Exception{
        Log.d("","send start");
        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setHostName("smtp.qq.com");
        htmlEmail.setSmtpPort(587);
        htmlEmail.setAuthentication("572298529@qq.com","jthljeaxpetgbdgf");
        htmlEmail.setCharset("gbk");
        htmlEmail.addTo("togepro@163.com");
        htmlEmail.setFrom("572298529@qq.com","SCOS");
        htmlEmail.setSubject("SCOS求助邮件");
        htmlEmail.setMsg("SCOS问题反馈！");
        htmlEmail.send();
        Log.d("","send finish");
    }

    /*
    *检查是否缺少相应权限
    * */
    private boolean isLackPermission(String permission){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this,permission);
        }
        return false;
    }

    /*
    * 请求发送短息权限
    * */
    private void requestPermission(String permission){
        ActivityCompat.requestPermissions(this,new String[]{permission},1);
    }



    /*
    * 权限申请结果
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:

                if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(permissions[0] == Manifest.permission.CALL_PHONE){
                        doCallingUp();
                        Log.i("call","yesy");
                    }
                    else if(permissions[0] == Manifest.permission.SEND_SMS){
                        doSendMessage();
                        Log.i("send","asda");
                    }
                }

                else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
