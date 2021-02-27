package com.persenlopro.wanandroid.Data;

//用于存放首页数据
public class Messages {

    private String link;
    private String niceDate;
    private String shareUser;
    private String title;
    private String superChapterName;

    public Messages(String link, String niceDate, String shareUser, String title, String superChapterName){
        this.link=link;
        this.niceDate=niceDate;
        this.shareUser=shareUser;
        this.title=title;
        this.superChapterName=superChapterName;
    }

    public String getLink(){
        return link;
    }

    public String getNiceDate(){
        return niceDate;
    }

    public String getShareUser(){
        return shareUser;
    }

    public String getTitle(){
        return title;
    }

    public String getSuperChapterName(){
        return superChapterName;
    }
}
