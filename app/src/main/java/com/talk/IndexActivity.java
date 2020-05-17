package com.talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.talk.activity.WebActivity;
import com.talk.fragments.dynamic.DynamicFragment;
import com.talk.fragments.home.HomeFragment;
import com.talk.fragments.mine.MineFragment;
import com.talk.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页  动态  我的界面
 */
public class IndexActivity extends AppCompatActivity {

    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.bnv)
    BottomNavigationView bnv;
    private HomeFragment homeFragment;
    private DynamicFragment dynamicFragment;
    private MineFragment mineFragment;
    private FragmentTransaction fragmentTransaction;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        initView();

        //进入第一页先判断是否是首次进入
        Boolean first = SpUtils.getInstance().getBoolean("first");
        if (!first) {
            showProvicyInfo();
        }

        //底部导航切换监听
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        fragmentTransaction.replace(R.id.fl,homeFragment).commit();
                        break;
                    case R.id.menu_dynamic:
                        fragmentTransaction.replace(R.id.fl,dynamicFragment).commit();
                        break;
                    case R.id.menu_mine:
                        fragmentTransaction.replace(R.id.fl,mineFragment).commit();
                        break;
                }
                return false;
            }
        });

        //默认初始化显示首页
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl,homeFragment).commit();

    }

    //第一次进入软件弹出隐私协议对话框
    private void showProvicyInfo() {
        if (alertDialog == null) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pop, null);
        alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)//点击空白区域不消失
                .create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            TextView tv_content = view.findViewById(R.id.tv_content);
            TextView tv_agree = view.findViewById(R.id.tv_agree);
            TextView tv_see_only = view.findViewById(R.id.tv_see_only);
            tv_see_only.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    alertDialog = null;
                }
            });
            tv_agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    alertDialog = null;
                    SpUtils.getInstance().setValue("first",true);
                }
            });
            String s1 = "欢迎您使用虎牙直播！\n我们将通过";
            String s2 = "《虎牙直播App隐私政策》";
            String s3 ="帮助您了解我们收集、使用、存储和共享个人信息的情况，以及您所享有的相关去权利。";

            //相当于Stringbuilder  字符串组合对象  富文本
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            stringBuilder.append(s1);
            SpannableString spannableString = new SpannableString(s2);//s2富文本格式
            //字符串S2的点击监听事件  点击s2跳转webView
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(IndexActivity.this, WebActivity.class);
                    intent.putExtra("url","https://blog.huya.com/regulation/1905/422290057455.html");
                    startActivity(intent);

                }
            },0,s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //对S2文字颜色设置
            spannableString.setSpan(new ForegroundColorSpan(Color.BLUE),0,s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.append(spannableString);
            SpannableString spannableString3 = new SpannableString(s3);
            stringBuilder.append(spannableString3);
            tv_content.setText(stringBuilder);
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            alertDialog.show();
        }
    }

    private void initView() {
        homeFragment = new HomeFragment();
        dynamicFragment = new DynamicFragment();
        mineFragment = new MineFragment();

    }

    //
}
