package com.talk.fragments.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.talk.R;
import com.talk.activity.loginregist.LoginActivity;
import com.talk.activity.mine.SettingActivity;
import com.talk.activity.mine.UserDetailInfoActivity;
import com.talk.base.BaseFragment;
import com.talk.interfaces.IBasePersenter;
import com.talk.utils.CircleImgUtils;
import com.talk.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {
    public static final int REQUEST_LOGIN_CODE = 100;
    public static final int REQUEST_SETTING_CODE = 200;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.con_no_login)
    ConstraintLayout conNoLogin;
    @BindView(R.id.tv_nikname)
    TextView tvNikname;
    @BindView(R.id.tv_signture)
    TextView tvSignture;
    @BindView(R.id.con_logined)
    ConstraintLayout conLogined;
    @BindView(R.id.con_head)
    ConstraintLayout conHead;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.iv_editor)
    ImageView ivEditor;

    @Override
    protected int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        updateShowOrCover();
    }


    //刷新登录显示状态
    private void updateShowOrCover() {
        //初始化界面是先判断用户是否一登录   来判断显示那个隐藏那个
        String username = SpUtils.getInstance().getString("username");
        if (TextUtils.isEmpty(username)) {
            conNoLogin.setVisibility(View.VISIBLE);
            conLogined.setVisibility(View.GONE);
        } else {//已经登录  账号存在
            conNoLogin.setVisibility(View.GONE);
            conLogined.setVisibility(View.VISIBLE);
            //显示圆图
            String avater = SpUtils.getInstance().getString("avater");
            CircleImgUtils.userHeaderCircle(avater,ivHead);
            //显示昵称
            String nickname = SpUtils.getInstance().getString("nickname");
            tvNikname.setText(nickname);
            //显示签名
            String sign = SpUtils.getInstance().getString("sign");
            tvSignture.setText(sign);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserData();
    }

    @Override
    protected void initData() {
        updateUserData();
    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
    }

    @OnClick({R.id.tv_login, R.id.tv_setting,R.id.iv_editor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                goLogin();
                break;
            case R.id.tv_setting:
                goSetting();
                break;
            case R.id.iv_editor:
               editorinfo();
                break;
        }
    }

    //进入用户详情信息编辑界面
    private void editorinfo() {
        Intent intent = new Intent(getActivity(), UserDetailInfoActivity.class);
        startActivityForResult(intent,REQUEST_LOGIN_CODE);
    }

    /**
     * 跳转设置页面
     */
    private void goSetting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivityForResult(intent, REQUEST_SETTING_CODE);
    }

    /**
     * 跳转登录页面
     */
    private void goLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_CODE) {
            updateShowOrCover();
            updateUserData();

        } else if (requestCode == REQUEST_SETTING_CODE) {
            updateShowOrCover();
        }
    }

    /**
     * 刷新用户信息显示到界面
     */
    private void updateUserData() {
        //得到用户头像
        String avater = SpUtils.getInstance().getString("avater");
        if (!TextUtils.isEmpty(avater)) {
            CircleImgUtils.userHeaderCircle(avater,ivHead);
        }

        //得到用户账号
        String name = SpUtils.getInstance().getString("nickname");
        if (!TextUtils.isEmpty(name)) {
            tvNikname.setText(name);
        }
        //签名
        String sign = SpUtils.getInstance().getString("sign");
        if (!TextUtils.isEmpty(sign)) {
            tvSignture.setText(sign);
        }
    }

    @OnClick(R.id.tv_setting)
    public void onViewClicked() {
    }
}
