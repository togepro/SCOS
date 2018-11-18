package es.source.code.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.source.code.adapter.FragPagerAdapter;
import es.source.code.fragment.ColdFoodFragment;
import es.source.code.fragment.DranksFragment;
import es.source.code.fragment.HotFoodFragment;
import es.source.code.fragment.SeaFoodFragment;
import es.source.code.model.Food;
import es.source.code.model.User;
import es.source.code.service.ServerObserverService;
import es.source.code.service.UpdateService;

public class FoodView extends AppCompatActivity {
        private User user;
        private TabLayout mTabLayout;
        private ViewPager mViewPager;
        private FragPagerAdapter adapter;
        private ServiceConnection conn;
        private ServerObserverService mserverObserverService;
        private  boolean isBound=false;
        private Menu menu;
        private Messenger sMessenger,mMessenger;
        private List<Fragment> fragments = new ArrayList<>();
        private Timer timer;

        private ArrayList<Food> coldfoodlist;
        private ColdFoodFragment coldfood;

        private ReceiveMessageHandler receiveMessageHandler = new ReceiveMessageHandler();
        private class ReceiveMessageHandler extends Handler{
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==10){
                    Bundle bundle  = msg.getData();
                    coldfoodlist = bundle.getParcelableArrayList("food");
                    updataFragment();
                }
                super.handleMessage(msg);
            }
        }

        public void updataFragment(){
            ((ColdFoodFragment)fragments.get(0)).foodlist=coldfoodlist;
            Log.i("Tag",coldfoodlist.get(0).getStore()+"");
            //adapter.notifyDataSetChanged();
        }

        public static final String[] tabTitle = new String[]{"冷菜","热菜","海鲜","酒水"};

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.food_view);
            //初始化视图
            initViews();

        }
        private void initViews(){

            Intent intent =getIntent();
            user= intent.getParcelableExtra("user");

            mTabLayout = findViewById(R.id.tabLayout);
            mViewPager = findViewById(R.id.viewPager);

            timer = new Timer();

            coldfood = new ColdFoodFragment();
            HotFoodFragment hotfood = new HotFoodFragment();
            SeaFoodFragment seafood = new SeaFoodFragment();
            DranksFragment drinks = new DranksFragment();
            fragments.add(coldfood);
            fragments.add(hotfood);
            fragments.add(seafood);
            fragments.add(drinks);

            adapter = new FragPagerAdapter(getSupportFragmentManager(), fragments,tabTitle);
            //给ViewPager设置适配器
            mViewPager.setAdapter(adapter);
            //将TabLayout和ViewPager关联起来。
            mTabLayout.setupWithViewPager(mViewPager);
            //设置可以滑动
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    sMessenger= new Messenger(service);
                    mMessenger = new Messenger(receiveMessageHandler);
                    Log.i("Log", "连接服务器");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mserverObserverService = null;
                }
            };
            Intent serverIntent = new Intent(this, ServerObserverService.class);
            bindService(serverIntent, conn, BIND_AUTO_CREATE);
            Intent upintent = new Intent(this, UpdateService.class);
            startService(upintent);
        }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ordered_food: {
                //跳转到已点菜品
                Intent intent = new Intent(this, FoodOrderView.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);// 序列化
                intent.putExtras(bundle);
                intent.putExtra("default",0);
                startActivity(intent);
                break;
            }

            case R.id.scan_order: {
                //跳转到查看订单
                Intent intent = new Intent(this, FoodOrderView.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);// 序列化
                intent.putExtras(bundle);
                intent.putExtra("default",1);
                startActivity(intent);
                break;
            }
            case R.id.service: {
                //跳转到呼叫服务
                Intent intent = new Intent(this, SCOSHelper.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);// 序列化
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.flash: {

                MenuItem menuItem = menu.findItem(id);
                if (isBound) {
                    isBound = false;
                    menuItem.setTitle("启动实时更新");
                    timer.cancel();
                    Message message = Message.obtain();
                    message.what=0;
                    try {
                        sMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                } else {
                    isBound = true;
                    menuItem.setTitle("停止实时更新");
                    setTimerTask();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    * 基于Timer实现实时发送数据
    *
    * */
    private void setTimerTask(){
        timer.schedule(new TimerTask() {
            Bundle bundle = new Bundle();
            int count=0;
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what=1;
                message.replyTo = mMessenger;
                bundle.putParcelableArrayList("food", ((ColdFoodFragment) fragments.get(0)).foodlist);
                bundle.putString("msg", "hello 服务器");
                Log.i("计数:",count+"");
                count++;
                message.setData(bundle);
                try {
                    sMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },1000,1000);
    }
}
