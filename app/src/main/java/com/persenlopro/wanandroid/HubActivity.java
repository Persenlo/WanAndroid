package com.persenlopro.wanandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HubActivity extends AppCompatActivity {

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);


        //使toolbar与actionbar一致（actionbar已隐藏）
        setTitle(R.string.hub_hub);
        Toolbar toolbar = findViewById(R.id.hub_toolbar);
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
        getMenuInflater().inflate(R.menu.toolbar_hub, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_hub_search);
        mSearchView=(SearchView)menuItem.getActionView();
        mSearchView.setQueryHint("按作者名搜索");
        setSearchViewListener();
        return true;
    }

    //toolbar菜单功能实现
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
                Intent intent=new Intent(HubActivity.this,ArticleViewAbility.class);
                Bundle b=new Bundle();
                b.putString("author",query);
                b.putInt("id",0);
                intent.putExtras(b);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}