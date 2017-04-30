package com.example.lijian.myapplication.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一个能在AIDL中使用的类示例
 * Created by LIJIAN on 2017/4/24.
 */

public class Book implements Parcelable {
    private String name;
    private float price;

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readFloat();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(price);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        price = in.readFloat();
    }
}
