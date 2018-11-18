package es.source.code.model;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class Data extends Application {
    private List<Food> foodList=new ArrayList<Food>();

    public Food getFood(int index){
        return foodList.get(index);
    }

    public void addFood(Food food){
        if(!foodList.contains(food))
            this.foodList.add(food);
    }

    public int getSize(){
        return foodList.size();
    }

    public void removeFood(Food food){
        foodList.remove(food);
    }

    public boolean isOrdered(Food food){
        return foodList.contains(food);
    }
}
