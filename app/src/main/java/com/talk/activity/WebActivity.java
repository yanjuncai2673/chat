package com.talk.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.talk.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.web)
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        String url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            web.loadUrl(url);
        }else {
            Toast.makeText(this, "未找到相关连接", Toast.LENGTH_SHORT).show();
        }
    }
}
