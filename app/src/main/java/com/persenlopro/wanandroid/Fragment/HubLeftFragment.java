package com.persenlopro.wanandroid.Fragment;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Adapter.HubAdapter;
import com.persenlopro.wanandroid.Adapter.WebsiteAdapter;
import com.persenlopro.wanandroid.Data.Hubs;
import com.persenlopro.wanandroid.R;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HubLeftFragment extends Fragment {
    private List<Hubs> hubsList=new ArrayList<>();
    private HubAdapter hubAdapter;
    private Context context;

    private RecyclerView recyclerView;

    private static final int LOAD_COMPLETE=0;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getFirst();
        View view=inflater.inflate(R.layout.fragment_left,container,false);

        context=view.getContext();

        recyclerView=view.findViewById(R.id.rv_fragment_left);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        hubAdapter=new HubAdapter(hubsList,context);
        recyclerView.setAdapter(hubAdapter);
        view.setBackgroundResource(R.drawable.border);

        return view;

    }


    //获取一级列表
    public void getFirst(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/tree/json")
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
        try {
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String firstName=jsonObject1.getString("name");
                hubsList.add(new Hubs(firstName));
            }
            Message message=new Message();
            message.what=LOAD_COMPLETE;
            handler.sendMessage(message);

        }catch (Exception e){
            Log.e("ERROR","JSONa:"+e);
        }
    }




    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LOAD_COMPLETE:
                    recyclerView.setAdapter(hubAdapter);
                default:
                    break;
            }
        }
    };


















}
