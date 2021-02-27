package com.persenlopro.wanandroid.Data;

import java.util.ArrayList;
import java.util.List;

//用于存放banner数据
public class Banners {

    private static List<Banners> BannersList=new ArrayList<>();
    public String imagePath;
    public String desc;
    public String title;
    public String url;
    public int viewType;



    public Banners(String imagePath, String desc, String title, String url){
        this.imagePath=imagePath;
        this.desc=desc;
        this.title=title;
        this.url=url;
    }

    public Banners(String imagePath,String title,String url){
        this.imagePath=imagePath;
        this.title=title;
        this.url=url;
    }

    public Banners(List<Banners> list){
        this.BannersList=list;
    }

    public String getImagePath(){
        return imagePath;
    }

    public String getDesc(){
        return desc;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return url;
    }

    public static List<Banners> getList(){return BannersList;}

}
