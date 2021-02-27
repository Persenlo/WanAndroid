package com.persenlopro.wanandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.persenlopro.wanandroid.Adapter.WebsiteAdapter;
import com.persenlopro.wanandroid.Data.Websites;
import com.persenlopro.wanandroid.Fragment.RightFragment;

import java.util.ArrayList;
import java.util.List;

public class WebsiteActivity extends AppCompatActivity {

    private List<Websites> websitesList=new ArrayList<>();
    private WebsiteAdapter websiteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        //使toolbar与actionbar一致（actionbar已隐藏）
        setTitle(R.string.app_website);
        Toolbar toolbar = findViewById(R.id.wb_toolbar);
        setSupportActionBar(toolbar);

        //toolbar显示左侧菜单键
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }








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
        }
        return true;
    }













}