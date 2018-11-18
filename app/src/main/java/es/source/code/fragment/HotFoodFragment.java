package es.source.code.fragment;

import es.source.code.activity.R;
import es.source.code.model.Food;

public class HotFoodFragment extends BaseFragment {
    @Override
    public void initFoods() {
        int store=1;
        foodlist.add(new Food("椒油素鸡",10, store, 0, R.drawable.food_cold_lbhg));
        foodlist.add(new Food("开胃泡菜",12, store, 0,R.drawable.food_cold_lbhg));
        foodlist.add(new Food("凉拌海带丝",10, store, 0,R.drawable.food_cold_lbhg));
        foodlist.add(new Food("凉拌黄瓜",10, store, 0,R.drawable.food_cold_lbhg));
        foodlist.add(new Food("卤牛肉",30, store, 0,R.drawable.food_cold_lbhg));
        foodlist.add(new Food("东北家拌凉菜",20, store, 0,R.drawable.food_cold_lbhg));
        foodlist.add(new Food("浇汁豆腐",15, store, 0,R.drawable.food_cold_lbhg));
        foodlist.add(new Food("青椒拌干丝",10, store, 0,R.drawable.food_cold_lbhg));
    }
}
