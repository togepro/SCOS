package es.source.code.fragment;


import android.util.Log;

import es.source.code.model.Data;

public class OrderedFoodFragment extends BaseFragment {
    private  Data orderFoods;
    @Override
    public void initFoods() {
        orderFoods= (Data)this.getContext().getApplicationContext();

        int length=orderFoods.getSize();
        if(length!=0){
            for(int i=0;i<length;i++) {
                if(orderFoods.getFood(i).getOrderState()==2) {
                    foodlist.add(orderFoods.getFood(i));
                    Log.i("od", orderFoods.getFood(i).getOrderState() + "");
                }
            }
        }
    }
}
