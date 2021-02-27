package com.persenlopro.wanandroid.Data;


//用于存放hub数据
public class Hubs {

    private String firstName;
    private String secondName;
    private int realId;

    public Hubs(String firstName,String secondName,int realId){
        this.firstName=firstName;
        this.secondName=secondName;
        this.realId=realId;
    }

    public Hubs(String firstName){
        this.firstName=firstName;
    }

    public Hubs(String secondName,int realId){
        this.secondName=secondName;
        this.realId=realId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getSecondName(){
        return secondName;
    }

    public int getRealId(){
        return realId;
    }

}
