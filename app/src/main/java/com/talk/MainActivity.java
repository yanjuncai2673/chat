package com.talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.talk.apps.MyApp;
import com.talk.utils.SpannableUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//最基础的富文本  表情 文字显示
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txt_show)
    TextView txtShow;
    @BindView(R.id.txt_link)
    TextView txtLink;
    @BindView(R.id.txt_bgcolor)
    TextView txtBgcolor;
    @BindView(R.id.txt_face)
    TextView txtFace;
    @BindView(R.id.btn_jump)
    Button btnJump;
    /**
     * Hello World!
     */
    private TextView mTxtShow;
    private TextView mTxtLink;
    private TextView mTxtBgcolor;
    private TextView mTxtFace;
    private SmileyParser smileyParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        String span = "[Image]这是第一个简单的图文混排的富文本";
        SpannableString spannableString = SpannableUtils.stringtoSpannableString(this, span);
        if (spannableString != null) {
            mTxtShow.setText(spannableString);
        }

        String span_url = "<杨幂>这是一个杨幂的超链接";
        SpannableString spanUrl = SpannableUtils.getSpanUrl(this, span_url);
        if (spanUrl != null) {
            mTxtLink.setText(spanUrl);
            //点击超链接跳转
            mTxtLink.setMovementMethod(LinkMovementMethod.getInstance());
            //设置超链接高亮颜色
            mTxtLink.setHighlightColor(Color.parseColor("#0000FF"));
        }

        String bgcolor = "图文混排前置背景红色";
        SpannableString spanBackground = SpannableUtils.getSpanBackground(this, bgcolor);
        if (spanBackground != null) {
            mTxtBgcolor.setText(spanBackground);
        }

        //表情图标的显示   文字 表情显示   图文混排
        String face_show = "[抱抱]滚球[闭嘴]";
        CharSequence charSequence = smileyParser.replace(face_show);
        mTxtFace.setText(charSequence);
    }

    private void initView() {
        mTxtShow = (TextView) findViewById(R.id.txt_show);
        mTxtLink = (TextView) findViewById(R.id.txt_link);
        mTxtBgcolor = (TextView) findViewById(R.id.txt_bgcolor);
        mTxtFace = (TextView) findViewById(R.id.txt_face);

        smileyParser = SmileyParser.getInstance(MyApp.myApp);
    }


    @OnClick(R.id.btn_jump)
    public void onViewClicked() {
        startActivity(new Intent(MainActivity.this,ChatTalkActivity.class));
    }
}
