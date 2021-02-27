package com.persenlopro.wanandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BrowserActivity extends AppCompatActivity {

    private String url=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        //获取传递的数据
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        url=b.getString("url");

        WebView webView=(WebView)findViewById(R.id.wv_bowser);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        //使toolbar与actionbar一致（actionbar已隐藏）
        Toolbar toolbar = findViewById(R.id.tb_browser);
        setSupportActionBar(toolbar);

        //toolbar返回键显示
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

    }

    //toolbar菜单显示
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_more,menu);
        return true;
    }


    //toolbar菜单功能实现
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.tb_more_like:
                Toast.makeText(this,R.string.app_test,Toast.LENGTH_SHORT).show();
                break;

            case R.id.tb_more_useBrowser:
                Uri uri=Uri.parse(url);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;

            case R.id.tb_more_share:
                Intent intent1=new Intent();
                intent1.setAction(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,url);
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1, "选择分享应用"));
                break;
        }
        return true;
    }
}