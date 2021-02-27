package com.persenlopro.wanandroid.Data;

public class Project {
    private String name;
    private String id;
    private String author;
    private String imagePath;
    private String link;
    private String niceDate;
    private String title;
    private String projectLink;

    public Project(String name,String id){
        this.name=name;
        this.id=id;
    }

    public Project(String author,String imagePath,String link,String niceDate,String title,String projectLink){
        this.author=author;
        this.imagePath=imagePath;
        this.link=link;
        this.niceDate=niceDate;
        this.title=title;
        this.projectLink=projectLink;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public String getAuthor(){
        return author;
    }

    public String getImagePath(){
        return imagePath;
    }

    public String getLink(){
        return link;
    }

    public String getNiceData(){
        return niceDate;
    }

    public String getTitle(){
        return title;
    }

    public String getProjectLink(){
        return projectLink;
    }
}
