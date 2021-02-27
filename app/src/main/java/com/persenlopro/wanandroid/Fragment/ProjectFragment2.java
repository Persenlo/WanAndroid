package com.persenlopro.wanandroid.Fragment;

import android.animation.IntArrayEvaluator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.persenlopro.wanandroid.Adapter.ProjectAdapter2;
import com.persenlopro.wanandroid.Data.Hubs;
import com.persenlopro.wanandroid.Data.Project;
import com.persenlopro.wanandroid.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectFragment2 extends Fragment {

    private List<Project> projectList=new ArrayList<>();
    private ProjectAdapter2 projectAdapter2;
    private RecyclerView recyclerView;
    private Context context;
    private IntentFilter intentFilter;
    private BroadcastReceiver refreshReceiver;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;


    private int count=1;
    private String id="null";

    private static final int LOAD_COMPLETE=0;
    private static final int ADD_COMPLETE=1;
    private static final int REFRESH_COMPLETE=2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getProjects(id,"load");

        View view=inflater.inflate(R.layout.fragment_bottom,container,false);

        context=view.getContext();

        recyclerView=(RecyclerView)view.findViewById(R.id.rv_fragment_bottom);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        projectAdapter2=new ProjectAdapter2(projectList,context);
        recyclerView.setAdapter(projectAdapter2);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(projectAdapter2.getPosition()+1==projectAdapter2.getItemCount()){
                    count=count+1;
                    getProjects(id,"add");
                }
            }
        });






        return view;
    }

    class RefreshReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent) {
            id=intent.getAction().substring(26);
            projectList.clear();
            count=0;
            getProjects(id,"refresh");
        }
    }


    public void getProjects(String id,String type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(id.equals("null")){
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url("https://www.wanandroid.com/project/tree/json")
                                .build();
                        Response response=client.newCall(request).execute();
                        String responseData=response.body().string();
                        getFirstJSON(responseData);
                    }else {
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url("https://www.wanandroid.com/project/list/"+count+"/json?cid="+id)
                                .build();
                        Response response=client.newCall(request).execute();
                        String responseData=response.body().string();
                        getFinalJSON(responseData,type);
                    }

                }catch (Exception e){
                    Log.e("ERROR","NETWORK:"+e) ;
                }
            }
        }).start();
    }
    //首次加载
    public void getFirstJSON(String jsonData){
        try{
            JSONObject jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String id=jsonObject1.getString("id");
                String name=jsonObject1.getString("name");
                projectList.add(new Project(name,id));
            }
            Message message=new Message();
            message.what=LOAD_COMPLETE;
            handler.sendMessage(message);
        }catch (Exception  e){
            Log.e("ERROR","JSONa:"+e);
        }
    }
    //获取文章列表
    public void  getFinalJSON(String jsonData,String type){
        try{
            JSONObject jsonObject=new JSONObject(jsonData).getJSONObject("data");
            JSONArray jsonArray=jsonObject.getJSONArray("datas");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String author=jsonObject1.getString("author");
                String imagePath=jsonObject1.getString("envelopePic");
                String link=jsonObject1.getString("link");
                String niceDate=jsonObject1.getString("niceDate");
                String title=jsonObject1.getString("title");
                String projectLink=jsonObject1.getString("projectLink");
                projectList.add(new Project(author,imagePath,link,niceDate,title,projectLink));
            }
            if(type.equals("add")){
            Message message=new Message();
            message.what=ADD_COMPLETE;
            handler.sendMessage(message);
            }
            if(type.equals("refresh")){
                Message message=new Message();
                message.what=REFRESH_COMPLETE;
                handler.sendMessage(message);
            }
        }catch (Exception e){
            Log.e("ERROR","JSONb:"+e);
        }
    }









    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case LOAD_COMPLETE:
                    for(int i=0;i<projectList.size();i++){
                        Project project=projectList.get(i);
                        String id=project.getId();
                        intentFilter=new IntentFilter();
                        intentFilter.addAction("com.persenlopro.wanandroid"+id);
                        refreshReceiver=new ProjectFragment2.RefreshReceiver();
                        context.registerReceiver(refreshReceiver,intentFilter);
                        }
                    projectList.clear();
                    break;

                case ADD_COMPLETE:
                    projectAdapter2.notifyDataSetChanged();
                    break;

                case REFRESH_COMPLETE:
                    recyclerView.setAdapter(projectAdapter2);
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(refreshReceiver);
    }
}







