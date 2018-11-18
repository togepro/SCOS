package es.source.code.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Food implements Parcelable{
    private String foodname;// 菜名
    private int price;// 价格
    private int store;// 库存;
    //private boolean order;// 是否点单
    /*
    * 0：未点
    * 1：未下单
    * 2：下单
    * */
    private int orderstate;//下单状态

    private int imageid;// 图片资源ID;

/*    public Food(String foodname, int price, int store, boolean order, int imageid) {
        this.foodname = foodname;
        this.price = price;
        this.store = store;
        this.order = order;
        this.imageid = imageid;
    }*/
    public Food(){}
    public Food(String foodname, int price, int store, int orderstate, int imageid) {
        this.foodname = foodname;
        this.price = price;
        this.store = store;
        this.orderstate = orderstate;
        this.imageid = imageid;
    }

    public String getFoodName() {
        return foodname;
    }

    public void setFoodName(String foodname) {
        this.foodname = foodname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int  getOrderState() {
        return orderstate;
    }

    public void setOrderState(int orderstate) {
        this.orderstate = orderstate;
    }

    public int getImageId() {
        return imageid;
    }


    public void setImageId(int imageid) {
        this.imageid = imageid;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }


    protected Food(Parcel in) {
        this.foodname = in.readString();
        this.price = in.readInt();
        this.store = in.readInt();
        this.orderstate = in.readInt();
        this.imageid = in.readInt();
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodname);
        dest.writeInt(price);
        dest.writeInt(store);
        dest.writeInt(orderstate);
        dest.writeInt(imageid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Food) {
            Food food= (Food) obj;
            return foodname.equalsIgnoreCase(food.getFoodName().trim());
        }
        return false;
    }
}
