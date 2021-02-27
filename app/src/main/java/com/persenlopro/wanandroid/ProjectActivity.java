package com.persenlopro.wanandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.persenlopro.wanandroid.Fragment.ProjectFragment;

public class ProjectActivity extends AppCompatActivity {

    private boolean isOpen=true;

    private IntentFilter intentFilter=new IntentFilter();
    private BroadcastReceiver recyclerReceiver=new RecyclerReceiver();

    private TextView tips;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setTitle(R.string.project_project);


        //使toolbar与actionbar一致（actionbar已隐藏）
        Toolbar toolbar = findViewById(R.id.tb_project);
        setSupportActionBar(toolbar);

        //toolbar显示左侧菜单键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        }

        intentFilter.addAction("com.persenlopro.wanandroid.project.recyclerclose");
        intentFilter.addAction("com.persenlopro.wanandroid.project.recycleropen");
        registerReceiver(recyclerReceiver,intentFilter);

        progressBar=(ProgressBar) findViewById(R.id.pb_project);
        tips=(TextView)findViewById(R.id.tv_project);

        progressBar.setVisibility(View.GONE);
        tips.setText("选择项目");




    }

    public class RecyclerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.persenlopro.wanandroid.project.recyclerclose")){
                tips.setText("加载完毕");
                isOpen=false;
            }else if(intent.getAction().equals("com.persenlopro.wanandroid.project.recycleropen")){
                tips.setText("加载完毕");
                isOpen=true;
            }
        }
    }




    //toolbar菜单显示
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_project, menu);
        return true;
    }
    //toolbar菜单功能实现
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.toolbar_project_choose:
                if(isOpen){
                    Intent intent=new Intent("com.persenlopro.wanandroid.project.recyclerclose");
                    sendBroadcast(intent);
                    isOpen=false;
                }else {
                    Intent intent=new Intent("com.persenlopro.wanandroid.project.recycleropen");
                    sendBroadcast(intent);
                    isOpen=true;
                }
        }
        return true;
    }
}