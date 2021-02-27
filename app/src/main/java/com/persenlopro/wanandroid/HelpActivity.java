package com.persenlopro.wanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.persenlopro.wanandroid.Adapter.MessageAdapter2;
import com.persenlopro.wanandroid.Data.Messages;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelpActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Messages> messagesList=new ArrayList<>();
    private MessageAdapter2 messageAdapter2;


    private int count=1;
    private static final int LOAD_COMPLETE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getHelp();


        //使toolbar与actionbar一致（actionbar已隐藏）
        setTitle(R.string.help_help);
        Toolbar toolbar = findViewById(R.id.tb_help);
        setSupportActionBar(toolbar);

        //toolbar显示左侧菜单键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }


        recyclerView=(RecyclerView)findViewById(R.id.rv_help);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter2=new MessageAdapter2(messagesList,HelpActivity.this);
        recyclerView.setAdapter(messageAdapter2);

        //加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<0) return ;
                if(layoutManager.findLastVisibleItemPosition()==messageAdapter2.getItemCount()-1){
                    count+=1;
                    getHelp();

                };
            }
        });
    }




    //toolbar菜单显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_common, menu);
        return true;
    }
    //toolbar菜单功能实现
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_common_about:
                Intent intent=new Intent(this,aboutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }




    //获取文章列表
    public void getHelp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url("https://wanandroid.com/wenda/list/"+count+"/json")
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
            JSONObject zero=new JSONObject(jsonData).getJSONObject("data");
            JSONArray jsonArray=zero.getJSONArray("datas");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject first=jsonArray.getJSONObject(i);
                String link=first.getString("link");
                String niceDate=first.getString("niceDate");
                String shareUser=first.getString("author");
                String title=first.getString("title");
                String superChapterName=first.getString("superChapterName");
                messagesList.add(new Messages(link,niceDate,shareUser,title,superChapterName));
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
                    messageAdapter2.notifyDataSetChanged();
                    break;
            }
        }
    };

}