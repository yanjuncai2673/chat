package com.talk.activity.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.talk.R;
import com.talk.base.BaseActivity;
import com.talk.interfaces.IBasePersenter;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
    }
}
