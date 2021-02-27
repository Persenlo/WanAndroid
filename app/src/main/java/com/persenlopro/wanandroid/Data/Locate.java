package com.persenlopro.wanandroid.Data;

public class Locate {

    private String ChapterName;
    private String title;
    private String link;

    public Locate(String ChapterName,String title,String link){
        this.ChapterName=ChapterName;
        this.title=title;
        this.link=link;
    }

    public Locate(String title,String link) {
        this.title=title;
        this.link=link;
    }
    public Locate(String ChapterName){
        this.ChapterName=ChapterName;
    }

    public String getChapterName(){
        return ChapterName;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }


}
