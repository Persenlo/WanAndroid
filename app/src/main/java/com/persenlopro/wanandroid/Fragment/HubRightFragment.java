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

import com.persenlopro.wanandroid.Adapter.HubAdapter;
import com.persenlopro.wanandroid.Adapter.HubAdapter2;
import com.persenlopro.wanandroid.Data.Hubs;
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

public class HubRightFragment extends Fragment {

    private List<Hubs> hubsList=new ArrayList<>();
    private HubAdapter2 hubAdapter2;
    private Context context;
    private RecyclerView recyclerView;

    private IntentFilter intentFilter;
    private BroadcastReceiver refreshReceiver;

    private String firstName;
    private int LoadCount=0;
    private static final int LOAD_COMPLETE=0;
    private static final int REFRESH_COMPLETE=1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getHubs();
        View view=inflater.inflate(R.layout.fragment_right,container,false);

        context=view.getContext();

        recyclerView=view.findViewById(R.id.rv_fragment_right);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        hubAdapter2=new HubAdapter2(hubsList,context,firstName);
        recyclerView.setAdapter(hubAdapter2);
        view.setBackgroundResource(R.drawable.border);

        return view;

    }


    //广播接收器
    class RefreshReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            firstName=intent.getAction().substring(26);
            hubsList.clear();
            getHubs();
        }
    }

    //获取二级体系数据
    public void getHubs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
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
        StringEscapeUtils.unescapeJson(jsonData);
        try {


            if (LoadCount==0){
                JSONObject jsonObject=new JSONObject(jsonData);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String firstName=jsonObject1.getString("name");
                    hubsList.add(new Hubs(firstName));
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
                    String mFirstName=jsonObject1.getString("name");
                    if (mFirstName.equals(firstName)){
                        JSONArray jsonArray1=jsonObject1.getJSONArray("children");
                        for(int j=0;j<jsonArray1.length();j++){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                            String secondName=jsonObject2.getString("name");
                            int realId=jsonObject2.getInt("id");
                            hubsList.add(new Hubs(secondName,realId));
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
                    for(int i=0;i<hubsList.size();i++){
                        Hubs hubs=hubsList.get(i);
                        String firstName=hubs.getFirstName();
                        intentFilter=new IntentFilter();
                        intentFilter.addAction("com.persenlopro.wanandroid"+firstName);
                        refreshReceiver=new HubRightFragment.RefreshReceiver();
                        context.registerReceiver(refreshReceiver,intentFilter);
                        recyclerView.setVisibility(View.GONE);

                    }
                    break;
                case REFRESH_COMPLETE:
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(hubAdapter2);
                    break;
            }
        }
    };



    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(refreshReceiver);
    }

}
