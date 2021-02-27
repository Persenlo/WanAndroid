package com.persenlopro.wanandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.persenlopro.wanandroid.Adapter.MessageAdapter;
import com.persenlopro.wanandroid.Data.Banners;
import com.persenlopro.wanandroid.Data.Messages;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private List<Messages> messages=new ArrayList<>();
    private List<Banners> banners=new ArrayList<>();
    private List<Banners> list=new ArrayList<>();
    Banners mbanners;

    MessageAdapter adapter=new MessageAdapter(messages, MainActivity.this);
    SwipeRefreshLayout swipeRefreshLayout;


    public static final int UPDATE_VIEW = 1;
    public static final int ADD_COMPLETE=2;
    private int count=0;

    private com.persenlopro.wanandroid.Adapter.BannersAdapter BannersAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //使toolbar与actionbar一致（actionbar已隐藏）
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar显示左侧菜单键
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //抽屉选项功能实现
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_hub:
                        Intent intent0=new Intent(MainActivity.this, HubActivity.class);
                        startActivity(intent0);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_locate:
                        Intent intent1=new Intent(MainActivity.this, LocateActivity.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_project:
                        Intent intent2=new Intent(MainActivity.this, ProjectActivity.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_sub:
                        Intent intent3=new Intent(MainActivity.this, SubActivity.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_help:
                        Intent intent4=new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(intent4);
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });





        //刷新功能实现
        swipeRefreshLayout=findViewById(R.id.sr_main);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh(){
                    list.clear();
                    messages.clear();
                    banners.clear();
                    getTop();
                    getBanners();
                    sendRequestWithOkHttp();


            }

        });

        //首次发送网络请求
        swipeRefreshLayout.setRefreshing(true);
        getTop();
        getBanners();
        sendRequestWithOkHttp();


        //RecyclerView初始化

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MessageAdapter(messages,this);
        recyclerView.setAdapter(adapter);

        //加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<0) return ;
                if(layoutManager.findLastVisibleItemPosition()==adapter.getItemCount()-1){
                    count+=1;
                    sendRequestWithOkHttp();

                };
            }
        });


    }



    //RecyclerView刷新(异步刷新）
    private Handler handler =new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_VIEW:
                    RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_main);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                    default:
                    break;
                case ADD_COMPLETE:
                    adapter.notifyDataSetChanged();
            }
        }
    };

    //toolbar菜单显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    //toolbar菜单功能实现
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.menu_search:
                Intent intent1=new Intent(this,SearchActivity.class);
                startActivity(intent1);
                break;

            case R.id.menu_website:
                Intent intent2=new Intent(this,WebsiteActivity.class);
                startActivity(intent2);
                break;

            case R.id.menu_about:
                Intent intent3=new Intent(this,aboutActivity.class);
                startActivity(intent3);
                break;

            case R.id.menu_setting:
                Toast.makeText(this,R.string.app_test,Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //FAB刷新功能实现
    public void onFabButtonClick(View view){
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        messages.clear();
        banners.clear();
        getTop();
        getBanners();
        sendRequestWithOkHttp();

    }

    public void getTop(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client =new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/article/top/json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    getTopJSON(responseData);
                }catch (Exception e){
                    Log.e("NETWORK","ERROR:"+e);}

            }
        }).start();
    }


    //获取主页json并解析（okHttp）
    public void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client =new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/article/list/"+count+"/json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    parseJSONWithJSONObject(responseData);
                }catch (Exception e){
                    Log.e("NETWORK","ERROR:"+e);
                }
            }
        }).start();

    }
    private void parseJSONWithJSONObject(String jsonData){
        try{
            //对JSON进行多次解析
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject zero=new JSONObject(jsonData).getJSONObject("data");
            //错误示范：JSONObject first=zero.getJSONObject("datas");
            JSONArray jsonArray=zero.getJSONArray("datas");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject first=jsonArray.getJSONObject(i);
                String link=first.getString("link");
                String niceDate=first.getString("niceDate");
                String shareUser=first.getString("author");
                String title=first.getString("title");
                String superChapterName=first.getString("superChapterName");
                messages.add(new Messages(link,niceDate,shareUser,title,superChapterName));
                //刷新RECYCLERVIEW(首次刷新由Banner的获取执行）
                    Message message=new Message();
                    message.what=ADD_COMPLETE;
                    handler.sendMessage(message);


            }
        }
        catch (Exception e){
            Log.e("JSONa","ERROR:"+e);
        }
    }

    //获取Banners的json并解析（okHttp）
    public void getBanners(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client =new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://www.wanandroid.com/banner/json")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    getBannersJson(responseData);
                }catch (Exception e){
                    Log.e("NETWORK","ERROR:"+e);
                }
            }
        }).start();

    }
    private void getBannersJson(String jsonData){
        try{
            //对JSON进行解析
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String imagePath=jsonObject1.getString("imagePath");
                String desc=jsonObject1.getString("desc");
                String title=jsonObject1.getString("title");
                String url=jsonObject1.getString("url");
                list.add(new Banners(imagePath,title,url));
                banners.add(new Banners(imagePath,desc,title,url));
                Message message=new Message();
                message.what=UPDATE_VIEW;
                handler.sendMessage(message);
            }
            new Banners(list);
        }
        catch (Exception e){
            Log.e("JSONb","ERROR:"+e);
        }
    }




    //获取置顶文章的json并解析
    private void getTopJSON(String jsonData){
        try{
            //对JSON进行解析
            StringEscapeUtils.unescapeJson(jsonData);
            JSONObject jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String link=jsonObject1.getString("link");
                String niceDate=jsonObject1.getString("niceDate");
                String shareUser=jsonObject1.getString("author");
                String title=jsonObject1.getString("title");
                String superChapterName=jsonObject1.getString("superChapterName");
                messages.add(new Messages(link,niceDate,shareUser,"[置顶]"+title,superChapterName));
                //刷新RECYCLERVIEW在普通文章解析完成后执行
            }
        }
        catch (Exception e){
            Log.e("JSONc","ERROR:"+e);
        }
    }











}