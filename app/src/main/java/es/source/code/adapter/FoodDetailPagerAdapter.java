package es.source.code.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.activity.R;
import es.source.code.model.Data;
import es.source.code.model.Food;

public class FoodDetailPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Food> foodList;
    private int resId;
    private List<View> viewList;

    public  FoodDetailPagerAdapter(Context context,List<Food> foodlist,int resId){
        this.mContext=context;
        this.foodList=foodlist;
        this.resId=resId;
        initViewList();


    }
    private void initViewList() {
        viewList = new ArrayList<>();
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        for (Food food : foodList) {
            viewList.add(inflater.inflate(resId, null));
        }
    }


    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View pageView = viewList.get(position);
        final Food food = foodList.get(position);
        final Data orderFoods= (Data)pageView.getContext().getApplicationContext();

        TextView tvFoodName = pageView.findViewById(R.id.food_name_detail);
        TextView tvFoodPrice =  pageView.findViewById(R.id.food_price_detail);
        ImageView ivFood =  pageView.findViewById(R.id.food_image_detail);
        final Button mButton = pageView.findViewById(R.id.take_order_detail);
        if(orderFoods.isOrdered(food)) {
            mButton.setText("退点");
        }
        tvFoodName.setText(food.getFoodName());
        String price = String.valueOf(food.getPrice()) + "元";
        tvFoodPrice.setText(price);
        ivFood.setImageResource(food.getImageId());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButton.getText().equals("点菜")){
                    food.setOrderState(1);
                    orderFoods.addFood(food);
                    Toast.makeText(pageView.getContext(),"点菜成功", Toast.LENGTH_SHORT).show();
                    mButton.setText("退点");
                }
                else if(mButton.getText().equals("退点")){
                    food.setOrderState(0);
                    orderFoods.removeFood(food);
                    Toast.makeText(pageView.getContext(),"退点成功", Toast.LENGTH_SHORT).show();
                    mButton.setText("点菜");
                }
            }
        });

        container.addView(pageView);

        return pageView;
    }
}
