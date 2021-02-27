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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.persenlopro.wanandroid.Adapter.HubAdapter2;
import com.persenlopro.wanandroid.Adapter.LocateAdapter;
import com.persenlopro.wanandroid.Adapter.LocateAdapter2;
import com.persenlopro.wanandroid.Data.Hubs;
import com.persenlopro.wanandroid.Data.Locate;
import com.persenlopro.wanandroid.R;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocateRightFragment extends Fragment {

    private List<Locate> locateList=new ArrayList<>();
    private LocateAdapter2 locateAdapter2;
    private Context context;
    private RecyclerView recyclerView;

    private IntentFilter intentFilter;
    private BroadcastReceiver refreshReceiver;

    private String ChapterName;
    private int LoadCount=0;
    private static final int LOAD_COMPLETE=0;
    private static final int REFRESH_COMPLETE=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getLocate();
        View view=inflater.inflate(R.layout.fragment_right,container,false);
        context=view.getContext();

        recyclerView=view.findViewById(R.id.rv_fragment_right);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        locateAdapter2=new LocateAdapter2(locateList,context);
        recyclerView.setAdapter(locateAdapter2);
        view.setBackgroundResource(R.drawable.border);

        return view;
    }




    //广播接收器
    class RefreshReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            ChapterName=intent.getAction().substring(26);
            locateList.clear();
            getLocate();
        }
    }

    //获取数据
    public void getLocate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/navi/json")
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
        StringEscapeUtils.unescapeJson(jsonData);
        try {


            if (LoadCount==0){
                JSONObject jsonObject=new JSONObject(jsonData);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String ChapterName=jsonObject1.getString("name");
                    locateList.add(new Locate(ChapterName));
                }
                Message message=new Message();
                message.what=LOAD_COMPLETE;
                handler.sendMessage(message);
                LoadCount++;
            }else {
                JSONObject jsonObject=new JSONObject(jsonData);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String mChapterName=jsonObject1.getString("name");
                    if (mChapterName.equals(ChapterName)){
                        JSONArray jsonArray1=jsonObject1.getJSONArray("articles");
                        for(int j=0;j<jsonArray1.length();j++){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                            String title=jsonObject2.getString("title");
                            String link=jsonObject2.getString("link");
                            locateList.add(new Locate(title,link));
                        }
                    }
                }
                Message message=new Message();
                message.what=REFRESH_COMPLETE;
                handler.sendMessage(message);
            }


        }catch (Exception e){
            Log.e("ERROR","JSONa:"+e);
        }
    }










    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case LOAD_COMPLETE:
                    for(int i=0;i<locateList.size();i++){
                        Locate locate=locateList.get(i);
                        String ChapterName=locate.getChapterName();
                        intentFilter=new IntentFilter();
                        intentFilter.addAction("com.persenlopro.wanandroid"+ChapterName);
                        refreshReceiver=new LocateRightFragment.RefreshReceiver();
                        context.registerReceiver(refreshReceiver,intentFilter);
                        recyclerView.setVisibility(View.GONE);

                    }
                    break;
                case REFRESH_COMPLETE:
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(locateAdapter2);
                    break;
            }
        }
    };



    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(refreshReceiver);
    }

}

