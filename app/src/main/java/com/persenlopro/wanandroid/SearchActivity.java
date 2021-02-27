package com.persenlopro.wanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.persenlopro.wanandroid.Adapter.HotKeysAdapter;
import com.persenlopro.wanandroid.Adapter.MessageAdapter;
import com.persenlopro.wanandroid.Adapter.MessageAdapter2;
import com.persenlopro.wanandroid.Data.HotKeys;
import com.persenlopro.wanandroid.Data.Messages;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private List<HotKeys> hotKeysList=new ArrayList<>();
    private List<Messages> messagesList=new ArrayList<>();

    private HotKeysAdapter hotKeysAdapter;
    private MessageAdapter2 messageAdapter2;

    private IntentFilter intentFilter;
    private BroadcastReceiver keyReceiver;

    private SearchView mSearchView;
    private TextView searchText;

    private static final int LOAD_COMPLETE=0;
    private static final int SEARCH_COMPLETE=1;
    private int searchCount=0;
    private int count=0;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getHotKeys();


        //使toolbar与actionbar一致（actionbar已隐藏）
        setTitle(R.string.search_search);
        Toolbar toolbar = findViewById(R.id.tb_search_search);
        setSupportActionBar(toolbar);

        //toolbar显示左侧菜单键
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        //RecyclerView初始化
        searchText=findViewById(R.id.tv_search_hotkeys);
        recyclerView1=(RecyclerView)findViewById(R.id.rv_search_hot);
        recyclerView2=(RecyclerView)findViewById(R.id.rv_search_result);
        StaggeredGridLayoutManager layoutManager1=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager2=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        hotKeysAdapter=new HotKeysAdapter(hotKeysList,SearchActivity.this);
        messageAdapter2=new MessageAdapter2(messagesList,SearchActivity.this);
        recyclerView1.setAdapter(hotKeysAdapter);
        recyclerView2.setAdapter(messageAdapter2);

        //加载更多
        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<0) return ;
                if(layoutManager2.findLastVisibleItemPosition()==messageAdapter2.getItemCount()-1){
                    count+=1;
                    getSearchResult(key);

                };
            }
        });


    }



    //广播接收器
    class KeyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            count=0;
            key=intent.getAction().substring(33);
            getSearchResult(key);
        }
    }


    //toolbar菜单显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_search_search);
        mSearchView=(SearchView)menuItem.getActionView();
        mSearchView.setIconified(false);
        setSearchViewListener();
        return true;
    }

    //菜单功能实现
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //设置搜索监听
    public void setSearchViewListener(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                key=query;
                count=0;
                messagesList.clear();
                getSearchResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    //获取搜索热词
    public void  getHotKeys(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client =new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com//hotkey/json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    getHotKeysJSON(responseData);
                }catch (Exception e){
                    Log.e("NETWORK","ERROR:"+e);}

            }
        }).start();
    }
    public void getHotKeysJSON(String jsonData){
        try {
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String name=jsonObject1.getString("name");
                hotKeysList.add(new HotKeys(name));
            }

            Message message=new Message();
            message.what=LOAD_COMPLETE;
            handler.sendMessage(message);
        }catch (Exception e){
            Log.e("ERROR","JSONa"+e);
        }

    }




    //提交文字内容并获取搜索结果
    public void getSearchResult(String key){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("k",key)
                            .build();

                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/article/query/"+count+"/json")
                            .post(requestBody)
                            .build();
                    Response response =client.newCall(request).execute();
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
            JSONObject zero=new JSONObject(jsonData).getJSONObject("data");
            JSONArray jsonArray=zero.getJSONArray("datas");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject first=jsonArray.getJSONObject(i);
                String link=first.getString("link");
                String niceDate=first.getString("niceDate");
                String shareUser=first.getString("author");
                String title=first.getString("title");
                String superChapterName=first.getString("superChapterName");
                messagesList.add(new Messages(link,niceDate,shareUser,title,superChapterName));}
                Message message=new Message();
                message.what=SEARCH_COMPLETE;
                handler.sendMessage(message);
        }catch (Exception e){
            Log.e("ERROR","JSONb:"+e);
        }
    }



    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LOAD_COMPLETE:
                    recyclerView1.setAdapter(hotKeysAdapter);
                    for(int i=0;i<hotKeysList.size();i++){
                        HotKeys hotKeys=hotKeysList.get(i);
                        String key=hotKeys.getName();
                        intentFilter=new IntentFilter("com.persenlopro.wanandroid.search"+key);
                        keyReceiver=new KeyReceiver();
                        registerReceiver(keyReceiver,intentFilter);
                    }
                case SEARCH_COMPLETE:
                    recyclerView2.setAdapter(messageAdapter2);
                    if(searchCount>0){
                        recyclerView1.setVisibility(View.GONE);
                        searchText.setText(R.string.search_result);
                    }
                    searchCount++;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(keyReceiver);
    }
}