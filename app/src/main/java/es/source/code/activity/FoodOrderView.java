package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.adapter.FragPagerAdapter;
import es.source.code.fragment.OrderedFoodFragment;
import es.source.code.fragment.UnorderFoodFragment;
import es.source.code.model.Data;
import es.source.code.model.Food;
import es.source.code.model.User;
import es.source.code.utils.MyTask;

public class FoodOrderView extends AppCompatActivity {
    private User user;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragPagerAdapter adapter;
    private Button mButton;
    private  Data  orderFoods;
    private TextView mtextView;
    private MyTask myTask;
    private ProgressBar progressBar;

    public static final String[] tabTitle = new String[]{"未下单菜","已下单菜"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏掉整个ActionBar
        setContentView(R.layout.food_order_view);

        //初始化视图
        initViews();
    }
    private void initViews(){
        orderFoods= (Data)this.getApplication();
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mButton = findViewById(R.id.pay);
        mtextView = findViewById(R.id.total_price);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        int defaultTab = intent.getIntExtra("default",0);
        user = intent.getParcelableExtra("user");

        List<Fragment> fragments = new ArrayList<>();
        final OrderedFoodFragment orderedFoodFragment= new OrderedFoodFragment();
        final UnorderFoodFragment unorderFoodFragment = new UnorderFoodFragment();
        fragments.add(unorderFoodFragment);
        fragments.add(orderedFoodFragment);

        final FragmentManager fragmentManager =getSupportFragmentManager();
        adapter = new FragPagerAdapter(fragmentManager, fragments,tabTitle);
        //给ViewPager设置适配器
        mViewPager.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //设置可以滑动
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.setCurrentItem(defaultTab);
        mTabLayout.getTabAt(defaultTab).select();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(position==0) {
                    mtextView.setVisibility(View.INVISIBLE);
                    mButton.setText("提交订单");
                }
                else if(position==1) {
                    mtextView.setVisibility(View.VISIBLE);
                    int length = orderFoods.getSize();
                    int totalprice=0;
                    for(int i=0;i<length;i++) {
                        totalprice+=orderFoods.getFood(i).getPrice();
                    }
                    mtextView.setText("总价："+totalprice);
                    mButton.setText("结账");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButton.getText().equals("提交订单")){
                    progressBar.setVisibility(View.INVISIBLE);
                    mButton.setEnabled(true);
                    int length = orderFoods.getSize();
                    for(int i=0;i<length;i++){
                        orderFoods.getFood(i).setOrderState(2);
                    }
                    Intent intent = new Intent(FoodOrderView.this,FoodOrderView.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", user);// 序列化
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    //adapter.notifyDataSetChanged();
                }
                else if(mButton.getText().equals("结账")){
                    //结账逻辑
                    progressBar.setVisibility(View.VISIBLE);
                    if((user!=null)&&user.getoldUser())
                        Toast.makeText(FoodOrderView.this,"您好，老顾客，本次你可享受 7 折优惠",Toast.LENGTH_SHORT).show();
                    Log.i("user",user+""+user.getoldUser());
                    myTask = new MyTask(FoodOrderView.this,progressBar,mtextView.getText()+"");
                    myTask.execute();

                    mButton.setEnabled(false);
                }
            }
        });
    }
}
