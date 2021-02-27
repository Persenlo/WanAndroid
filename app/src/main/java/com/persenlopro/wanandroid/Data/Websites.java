package com.persenlopro.wanandroid.Data;

import android.provider.ContactsContract;
import android.util.Log;

//用于存放常用网站
public class Websites {
    private String category;
    private String link;
    private String name;

    public Websites(String category,String link,String name){
        this.category=category;
        this.link=link;
        this.name=name;
    }

    public String getCategory(){
        return category;
    }

    public String getLink(){
        return link;
    }

    public String getName(){
        return name;
    }


    public String getTrueName(String category){
        if(this.category.equals(category)){
            return name;}
        else
            return "null";
    }
}
