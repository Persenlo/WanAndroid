package com.persenlopro.wanandroid.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Adapter.WebsiteAdapter;
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

public class LeftFragment extends Fragment {

    private List<Websites> websitesList=new ArrayList<>();
    private WebsiteAdapter websiteAdapter;
    private Context context;

    private RecyclerView recyclerView;

    private static final int LOAD_COMPLETE=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getWebsite();

        View view=inflater.inflate(R.layout.fragment_left,container,false);

        context=view.getContext();

        recyclerView=view.findViewById(R.id.rv_fragment_left);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        websiteAdapter=new WebsiteAdapter(websitesList,context);
        recyclerView.setAdapter(websiteAdapter);
        view.setBackgroundResource(R.drawable.border);

        return view;
    }




    //获取常用网站的JSON并解析(okHttp+JSONObject)
    public void getWebsite(){
        new Thread(new Runnable(){
            public void run(){
                try {
                    OkHttpClient client =new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/friend/json")
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
                String category = jsonObject1.getString("category");
                String link = jsonObject1.getString("link");
                String name = jsonObject1.getString("name");
                websitesList.add(new Websites(category, link, name));
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
                    recyclerView.setAdapter(websiteAdapter);


            }
        }
    };
    }
