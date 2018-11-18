package es.source.code.activity;
/**
 * @name SCOS
 * @class name：es.source.code.activity
 * @class describe
 * @author togepro QQ:1032006226
 * @time 2018/10/9 14:08
 * @change
 * @chang time
 * @class describe
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;


import es.source.code.utils.BaseActivity;

public class SCOSEntry extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        editor.putInt("LoginState",0);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void next(View view) {

        Intent intent = new Intent(this, MainScreen.class);
        //Intent intent = new Intent(this, FoodView.class);
        intent.putExtra("from","FromEntry");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }

    @Override
    public void pre(View view) {
        //do nothing
    }
}
