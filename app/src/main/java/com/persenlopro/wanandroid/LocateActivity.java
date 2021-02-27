package com.persenlopro.wanandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LocateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);


        //使toolbar与actionbar一致（actionbar已隐藏）
        setTitle(R.string.locate_locate);
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