package com.talk.activity.loginregist;


import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.talk.R;
import com.talk.activity.WebActivity;
import com.talk.base.BaseActivity;
import com.talk.interfaces.login.LoginConstract;
import com.talk.models.bean.login.LoginInfoBean;
import com.talk.persenter.login.LoginPersenter;
import com.talk.utils.SecretUtils;
import com.talk.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginConstract.Presenter> implements LoginConstract.View {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.rb)
    RadioButton rb;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.registe)
    TextView registe;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        String s1 = "已阅读并同意";
        String s2 = "《用户服务协议》";
        String s3 = "和";
        String s4 = "《隐私政策》";

        //相当于Stringbuilder  字符串组合对象  富文本
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(s1);

        SpannableString spannableString = new SpannableString(s2);//s2富文本格式
        //字符串S2的点击监听事件  点击s2跳转webView
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("url", "https://blog.csdn.net/weixin_44794785/article/details/90514416");
                startActivity(intent);

            }
        }, 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //对S2文字颜色设置
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(spannableString);

        SpannableString spannableString3 = new SpannableString(s3);
        stringBuilder.append(spannableString3);

        SpannableString spannableString4 = new SpannableString(s4);
        //字符串S4的点击监听事件  点击s4跳转webView
        spannableString4.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("url", "https://blog.csdn.net/weixin_44794785/article/details/90514416");
                startActivity(intent);

            }
        }, 0, s4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //对S4文字颜色设置
        spannableString4.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(spannableString4);
        tvAgreement.setText(stringBuilder);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());


    }

    @Override
    protected void initData() {

    }

    @Override
    protected LoginConstract.Presenter createPersenter() {
        return new LoginPersenter();
    }


    //登录数据的返回
    @Override
    public void loginReturn(LoginInfoBean loginInfoBean) {
        if (loginInfoBean.getErr() == 200) {///登录返回的结果有值登录成功
            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            //把接收到的值存进sp
            SpUtils.getInstance().setValue("token", loginInfoBean.getData().getToken());
            SpUtils.getInstance().setValue("phone", loginInfoBean.getData().getPhone());
            SpUtils.getInstance().setValue("username", loginInfoBean.getData().getUsername());
            SpUtils.getInstance().setValue("avater", loginInfoBean.getData().getAvater());//头像
            //登录成功后关闭本界面
            finish();
        } else {//登录失败将错误信息吐司
            Toast.makeText(context, loginInfoBean.getErrmsg(), Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick({R.id.tv_login,R.id.registe})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                login();
                break;
            case R.id.registe:
                registe();
                break;
        }
    }
    //注册
    private void registe() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent,410);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 410&&resultCode ==420) {
                String name = data.getStringExtra("username");
                String pwd = data.getStringExtra("password");
                etUsername.setText(name);
                etPassword.setText(pwd);
            }
        }
    }

    //登录
    private void login() {
        String username = etUsername.getText().toString();
        String pwd = etPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(context, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        //点击登录前先判断是否已勾选同意协议
        if (!rb.isChecked()) {
            Toast.makeText(context, "请先勾选用户协议", Toast.LENGTH_SHORT).show();
            return;
        }

        //对密码进行加密   点击登录 加密密码
        pwd = SecretUtils.getStrtoSha(pwd);
        //正常业务逻辑情况下
        persenter.login(username, pwd);
    }

}
