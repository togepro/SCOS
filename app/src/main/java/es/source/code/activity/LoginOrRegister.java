package es.source.code.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import es.source.code.model.User;
import es.source.code.utils.CommonUtil;

public class LoginOrRegister extends AppCompatActivity {

    private EditText accountET, accountPW;
    private Button loginBtn;
    private Button registerBtn;
    private User user;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String name,password;
    private int responsecode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        loginBtn = findViewById(R.id.login);
        registerBtn = findViewById(R.id.register);

        accountET = findViewById(R.id.et_number);
        accountPW = findViewById(R.id.et_password);

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();

        /*
        * 编辑框焦点改变监听
        * */
        accountET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!accountET.hasFocus()) {
                    checkFormat(accountET);
                    String username = name;
                    String avalue = sharedPreferences.getString("userName","Null");
                    if(avalue.equals("Null")){
                        loginBtn.setVisibility(View.INVISIBLE);
                        editor.putString("userName",username);

                        editor.commit();

                    }
                    else {
                        registerBtn.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    accountET.setError(null,null);
                    loginBtn.setEnabled(true);
                }
                name = accountET.getText().toString();
            }
        });
        accountPW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!accountPW.hasFocus()) {
                    checkFormat(accountPW);
                }
                else{
                    accountPW.setError(null,null);
                    loginBtn.setEnabled(true);
                }
                password = accountPW.getText().toString();
            }
        });
    }

    /*
    * 登录
    * */
    public void login(View v) {
        editor.putString("userName",name);
        editor.putInt("LoginState",1);
        editor.commit();
        connectServer();
        user = new User(name,password,true);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.app_name));
        progressDialog.setMessage(getResources().getString(R.string.dialog_login));
        progressDialog.setCancelable(false);
        //progressDialog.show();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Intent intent = new Intent(LoginOrRegister.this,MainScreen.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);// 序列化
                intent.putExtras(bundle);
                intent.putExtra("from","LoginSuccess");
                if(responsecode == 1) {
                    progressDialog.show();
                    startActivity(intent);
                }
                else
                    Toast.makeText(LoginOrRegister.this,"账户密码错误",Toast.LENGTH_SHORT).show();
            }
        }, 2000);

    }

    private void checkFormat(EditText account) {
        String text = account.getText().toString();
        if (!CommonUtil.validateFormat(text)) {
            loginBtn = findViewById(R.id.login);
            loginBtn.setEnabled(false);
            //Drawable drawable = getResources().getDrawable(R.drawable.hint);//错误时要显示的图片
            //drawable.setBounds(new Rect(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight()));
            account.setError("输入");
        }
    }

    /*
    *返回主页面
    * */
    public void returnMain(View v){
        editor.putInt("LoginState",0);
        editor.commit();

        Intent intent = new Intent(this, MainScreen.class);
        intent.putExtra("from","Return");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }
    /*
    * 注册
    * */
    public void register(View v){
        editor.putString("userName",name);
        editor.putInt("LoginState",1);
        editor.commit();

        user = new User(name,password,false);
        Intent intent = new Intent(LoginOrRegister.this,MainScreen.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);// 序列化
        intent.putExtras(bundle);
        intent.putExtra("from","RegisterSuccess");
        connectServer();
        startActivity(intent);

    }

    public void connectServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                URL url = null;
                try{
                    url = new URL("http://10.0.2.2:8080/SCOSServer/LoginValidator?name="+name+"&password="+password);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    httpURLConnection.connect();

                    /*接收来自服务器端的响应*/
                    Log.i("TAG",httpURLConnection.getResponseCode()+"");
                    if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                        Log.i("TAG","响应");
                        InputStream in = httpURLConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String str = null;
                        StringBuffer buffer = new StringBuffer();
                        while((str = br.readLine())!=null){//BufferedReader特有功能，一次读取一行数据
                            buffer.append(str);
                        }
                        in.close();
                        br.close();

                        JSONObject rjson = new JSONObject(buffer.toString());
                        Log.d("zxy", "rjson="+rjson);
                        responsecode = rjson.getInt("RESULTCODE");
                        if(responsecode==1){
                            Log.i("Tag","登录成功");
                        }
                        else{
                            Log.i("TAG","登录失败");
                        }
                    }
                }catch(MalformedURLException e){
                    Log.i("格式","失败");
                }
                catch (IOException e){
                    e.printStackTrace();
                    Log.i("failed","失败");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }finally {
                    httpURLConnection.disconnect();
                }

            }
        }).start();
    }
}