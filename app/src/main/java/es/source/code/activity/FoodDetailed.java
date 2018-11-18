package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import java.util.List;

import es.source.code.adapter.FoodDetailPagerAdapter;
import es.source.code.model.Data;
import es.source.code.model.Food;

public class FoodDetailed extends AppCompatActivity {

    private static int currentPageIndex=-1;
    private ViewPager mViewPager;
    private List<Food> mFoodList;
    private FoodDetailPagerAdapter mFoodDetailPagerAdapter;
    private Data orderFoods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detailed);

        initViews();
    }
    private void initViews(){

        //获取food列表......success
        Intent intent = getIntent();
        mFoodList=intent.getParcelableArrayListExtra("foods");
        currentPageIndex = intent.getIntExtra("position",0);
        String value = intent.getStringExtra("from");

        mViewPager = findViewById(R.id.food_detail);
        mFoodDetailPagerAdapter=new FoodDetailPagerAdapter(this,mFoodList,R.layout.fragment_food_detial);
        mViewPager.setAdapter(mFoodDetailPagerAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntent().putExtras(intent);
    }
    public void click(View v){

    }

}
