package es.source.code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String  userName;
    private String password;
    private boolean oldUser;

    public User(String userName,String password,boolean oldUser){
        this.userName=userName;
        this.password=password;
        this.oldUser=oldUser;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setOldUser(boolean oldUser){
        this.oldUser=oldUser;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public boolean getoldUser(){
        return this.oldUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeByte(this.oldUser ? (byte) 1 : (byte) 0);
    }
    protected User(Parcel in) {
            this.userName = in.readString();
            this.password = in.readString();
            this.oldUser = in.readByte() != 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
}
