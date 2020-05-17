package com.talk.activity.loginregist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.talk.R;
import com.talk.base.BaseActivity;
import com.talk.interfaces.register.RegisterConstract;
import com.talk.models.bean.login.RegisterInfoBean;
import com.talk.persenter.register.RegisterPersenter;
import com.talk.utils.SecretUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterConstract.Presenter> implements RegisterConstract.View {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private String name;
    private String pwd;

    @Override
    protected int getLayout() {
        return R.layout.activity_registers;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected RegisterConstract.Presenter createPersenter() {
        return new RegisterPersenter();
    }

    @Override
    public void registerReturn(RegisterInfoBean registerInfoBean) {
        if (registerInfoBean.getErr() ==200) {
            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("username",name);
            intent.putExtra("password",pwd);
            setResult(420,intent);
            finish();
        }else {
            Toast.makeText(context, "账号或密码已注册", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.tv_register)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                goRegister();
                break;
        }
    }

    private void goRegister() {
        name = etUsername.getText().toString();
        pwd = etPassword.getText().toString();
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(pwd)) {
            Toast.makeText(context, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        //对密码进行sha1加密码
        String pd = SecretUtils.getStrtoSha(pwd);
        //正常业务逻辑访问
        persenter.goRegister(name, pd);
    }
}
