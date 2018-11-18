package es.source.code.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.activity.R;
import es.source.code.model.Data;
import es.source.code.model.Food;


public class FoodAdapter extends ArrayAdapter implements View.OnClickListener {
    private final int resourceId;
    private Food food;
    private Context context;
    private  Data data;
    private ArrayList<Food> foodlist;

    public FoodAdapter(@NonNull Context context, int resource, List<Food> objects) {
        super(context, resource,objects);
        this.context=context;
        resourceId=resource;
        data =  (Data)this.getContext().getApplicationContext();
        foodlist = (ArrayList<Food>)objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        food = (Food) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView foodImage =  view.findViewById(R.id.food_image);
        TextView foodName = view.findViewById(R.id.food_name);
        TextView foodPrice =  view.findViewById(R.id.food_price) ;
        TextView foodStore = view.findViewById(R.id.food_store);
        foodImage.setImageResource(food.getImageId());
        foodName.setText(food.getFoodName());
        foodPrice.setText(food.getPrice()+"元");
        foodStore.setText("库存："+food.getStore());
        Button btn = view.findViewById(R.id.take_order);
        int orderstate = food.getOrderState();
        switch (orderstate){
            case 0: {
                btn.setText("点菜");
                btn.setVisibility(View.VISIBLE);
                break;
            }
            case 1: {
                btn.setText("退点");
                btn.setVisibility(View.VISIBLE);
                break;
            }
            case 2:
                btn.setVisibility(View.INVISIBLE);
                break;
        }

        btn.setTag(position);
        btn.setOnClickListener(this);
        return view;

    }

    public void onClick(View v) {
        final Data orderFoods= (Data)getContext().getApplicationContext();
        Button btn =  v.findViewById(R.id.take_order);
        int position = (int)v.getTag();
        food = (Food)getItem(position);

        if(btn.getText().equals("点菜")){
            food.setOrderState(1);
            orderFoods.addFood(food);
            Toast.makeText(getContext(),"点菜成功", Toast.LENGTH_SHORT).show();
            btn.setText("退点");
            int store = food.getStore();
            store--;
            foodlist.get(foodlist.indexOf(food)).setStore(store);
        }
        else if(btn.getText().equals("退点")){
            food.setOrderState(0);
            orderFoods.removeFood(food);
            Toast.makeText(getContext(),"退点成功", Toast.LENGTH_SHORT).show();
            btn.setText("点菜");
            int store = food.getStore();
            store++;
            foodlist.get(foodlist.indexOf(food)).setStore(store);
        }
    }
}
