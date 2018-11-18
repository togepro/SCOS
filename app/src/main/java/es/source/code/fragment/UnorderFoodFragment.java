package es.source.code.fragment;

import android.util.Log;

import es.source.code.model.Data;

public class UnorderFoodFragment extends BaseFragment {
    private  Data orderFoods;
    @Override
    public void initFoods() {
        orderFoods= (Data)this.getContext().getApplicationContext();

        int length=orderFoods.getSize();
        if(length!=0){
            for(int i=0;i<length;i++) {
                if(orderFoods.getFood(i).getOrderState()==1) {
                    foodlist.add(orderFoods.getFood(i));
                    Log.i("ud", orderFoods.getFood(i).getOrderState() + "");
                }
            }
        }
    }
}
