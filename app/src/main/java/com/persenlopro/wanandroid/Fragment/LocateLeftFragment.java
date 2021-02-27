package com.persenlopro.wanandroid.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Adapter.LocateAdapter;
import com.persenlopro.wanandroid.Adapter.WebsiteAdapter;
import com.persenlopro.wanandroid.Data.Locate;
import com.persenlopro.wanandroid.Data.Websites;
import com.persenlopro.wanandroid.R;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocateLeftFragment extends Fragment {
    private List<Locate> locateList=new ArrayList<>();
    private LocateAdapter locateAdapter;
    private Context context;

    private RecyclerView recyclerView;

    private static final int LOAD_COMPLETE=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getLocate();

        View view=inflater.inflate(R.layout.fragment_left,container,false);

        context=view.getContext();

        recyclerView=view.findViewById(R.id.rv_fragment_left);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        locateAdapter=new LocateAdapter(locateList,context);
        recyclerView.setAdapter(locateAdapter);
        view.setBackgroundResource(R.drawable.border);

        return view;
    }




    //获取常用网站的JSON并解析(okHttp+JSONObject)
    public void getLocate(){
        new Thread(new Runnable(){
            public void run(){
                try {
                    OkHttpClient client =new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/navi/json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    getWebsiteJSON(responseData);


                }catch (Exception e){
                    Log.e("ERROR","NETWORK:"+e);
                }
            }
        }).start();
    }
    public void getWebsiteJSON(String jsonData) {
        try {
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String name = jsonObject1.getString("name");
                locateList.add(new Locate(name));
            }


            Message message=new Message();
            message.what=LOAD_COMPLETE;
            handler.sendMessage(message);

        } catch (Exception e) {
            Log.e("ERROR", "JSONa:" + e);
        }
    }
    //异步刷新
    public Handler handler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case LOAD_COMPLETE:
                    recyclerView.setAdapter(locateAdapter);
            }
        }
    };
}
