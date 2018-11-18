package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.adapter.MainScreenAdapter;
import es.source.code.model.Food;
import es.source.code.model.User;


public class MainScreen extends AppCompatActivity {
    private User user;
    private Button btn;
    private GridView mgridView;
    private MainScreenAdapter madapter;
    private List  listitem;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private int loginState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        Intent intent = getIntent();
        String value = intent.getStringExtra("from");
        Log.i("from",value);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        loginState = sharedPreferences.getInt("LoginState",0);


        if(value.equals("LoginSuccess")){
            user = (User) intent.getParcelableExtra("user");
        }
        else if(("RegisterSuccess").equals(value)){
            user = (User) intent.getParcelableExtra("user");
            Toast.makeText(this,"欢迎您成为SCOS新用户",Toast.LENGTH_LONG).show();
        }
        else if (("FromEntry").equals(value)) {
            user=null;
        }
        else{
            user=null;
        }
        setData();
        initView();
    }

    public void setData(){
        listitem = new ArrayList();
        Log.i("Tag","setData");
        if(loginState==1){
            int icno[] = { R.drawable.select, R.drawable.order, R.drawable.contacts, R.drawable.help};
            //图标下的文字
            String name[]={"点餐","查看订单","登录/注册","系统帮助"};
            for (int i = 0; i <icno.length; i++) {
                Map<String, Object> map=new HashMap();
                map.put("img", icno[i]);
                map.put("text",name[i]);
                listitem.add(map);
            }
        }
        else if(loginState==0){
            int icno[] = { R.drawable.contacts, R.drawable.help};
            //图标下的文字
            String name[]={"登录/注册","系统帮助"};
            for (int i = 0; i <icno.length; i++) {
                Map<String, Object> map=new HashMap();
                map.put("img", icno[i]);
                map.put("text",name[i]);
                listitem.add(map);
            }
        }
    }



    void initView() {
        mgridView =  findViewById(R.id.gridview);

        madapter = new MainScreenAdapter(MainScreen.this,listitem);
        mgridView.setAdapter(madapter);
        if(loginState==1) {
            mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            intent = new Intent(MainScreen.this, FoodView.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("user", user);// 序列化
                            intent.putExtras(bundle);
                            break;
                        case 1:
                            intent = new Intent(MainScreen.this, FoodOrderView.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putParcelable("user", user);// 序列化
                            intent.putExtras(bundle1);
                            break;
                        case 2:
                            intent = new Intent(MainScreen.this, LoginOrRegister.class);
                            break;
                        case 3:
                            intent = new Intent(MainScreen.this, SCOSHelper.class);
                            break;
                    }
                    startActivity(intent);
                }
            });
        }
        else if(loginState==0){
            mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            intent = new Intent(MainScreen.this, LoginOrRegister.class);
                            break;
                        case 1:
                            intent = new Intent(MainScreen.this, SCOSHelper.class);
                            break;
                    }
                    startActivity(intent);
                }
            });
        }
    }
}
