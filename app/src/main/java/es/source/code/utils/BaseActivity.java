package es.source.code.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.MotionEvent;




public abstract class BaseActivity extends Activity {

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //1 初始化  手势识别器
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {// e1: 第一次按下的位置   e2   当手离开屏幕 时的位置  velocityX  沿x 轴的速度  velocityY： 沿Y轴方向的速度
                if(Math.abs(e1.getRawY() - e2.getRawY())>100){
                    return true;
                }
                if(Math.abs(velocityX)<150){
                    return true;
                }
                if((e1.getRawX() - e2.getRawX()) >200){
                    next(null);
                    return true;
                }
                if((e2.getRawX() - e1.getRawX()) >200){
                    pre(null);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }
    /**
     * 下一个页面
     * @param view
     */
    public abstract void next(View view);
    /**
     * 上一个页面
     * @param view
     */
    public abstract void pre(View view);

    //重写activity的触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //2.让手势识别器生效
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
