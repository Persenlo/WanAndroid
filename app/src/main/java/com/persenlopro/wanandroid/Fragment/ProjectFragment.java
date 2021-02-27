package com.persenlopro.wanandroid.Fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.persenlopro.wanandroid.Adapter.ProjectAdapter;
import com.persenlopro.wanandroid.Data.Project;
import com.persenlopro.wanandroid.R;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectFragment extends Fragment {

    private List<Project> projectList=new ArrayList<>();
    private Context context;
    private ProjectAdapter projectAdapter;
    private RecyclerView recyclerView;
    private IntentFilter intentFilter=new IntentFilter();

    private static final int LOAD_COMPLETE=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getProjects();

        View view=inflater.inflate(R.layout.fragment_top,container,false);

        context=view.getContext();

        recyclerView=(RecyclerView)view.findViewById(R.id.rv_fragment_top);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        projectAdapter=new ProjectAdapter(projectList,context);
        recyclerView.setAdapter(projectAdapter);

        intentFilter.addAction("com.persenlopro.wanandroid.project.recyclerclose");
        intentFilter.addAction("com.persenlopro.wanandroid.project.recycleropen");

        RecyclerReceiver recyclerReceiver=new RecyclerReceiver();
        context.registerReceiver(recyclerReceiver,intentFilter);

        return view;

    }


    public class RecyclerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.persenlopro.wanandroid.project.recyclerclose")){
                recyclerView.setVisibility(View.GONE);
            }
            else if(intent.getAction().equals("com.persenlopro.wanandroid.project.recycleropen")){
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }


    public void getProjects(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/project/tree/json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    getJSON(responseData);
                }catch (Exception e){
                    Log.e("ERROR","NETWORK:"+e);
                }
            }
        }).start();
    }
    public void getJSON(String jsonData){
        try{
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String name=jsonObject1.getString("name");
                String id=jsonObject1.getString("id");
                projectList.add(new Project(name,id));
            }
            Message message=new Message();
            message.what=LOAD_COMPLETE;
            handler.sendMessage(message);


        }catch (Exception e){
            Log.e("ERROR","JSONa:"+e);
        }
    }





    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case LOAD_COMPLETE:
                    recyclerView.setAdapter(projectAdapter);
                    break;

            }
        }
    };


}
