package com.talk.activity.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.talk.R;
import com.talk.apps.GlideEngine;
import com.talk.base.BaseActivity;
import com.talk.interfaces.mine.UserDetailInfoConstract;
import com.talk.models.bean.mine.UserDetailInfoBean;
import com.talk.models.bean.mine.UserUpdateInfoBean;
import com.talk.persenter.mine.UserDetailInfoPresenter;
import com.talk.utils.CircleImgUtils;
import com.talk.utils.SexUtils;
import com.talk.utils.SpUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserDetailInfoActivity extends BaseActivity<UserDetailInfoConstract.DetailPresenter> implements UserDetailInfoConstract.DetailView {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_exitslogin)
    TextView tvExitslogin;
    @BindView(R.id.item_name)
    ConstraintLayout nameLayout;
    @BindView(R.id.item_age)
    ConstraintLayout ageLayout;
    @BindView(R.id.item_sex)
    ConstraintLayout sexLayout;
    @BindView(R.id.item_sign)
    ConstraintLayout signLayout;
    @BindView(R.id.item_leave)
    ConstraintLayout leaveLayout;
    @BindView(R.id.item_head)
    ConstraintLayout headLayout;

    UserDetailInfoBean userDetailInfoBean;
    private TextView curtv;//当前操作的信息文本
    private String et_alog_name;//弹框输入框修改内容
    public static final  int CODE_HEADCROP = 690;
    private String newheadurl;//新头像url;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_detail_info;
    }

    @Override
    protected void initView() {
       /*TextView nickname = nameLayout.findViewById(R.id.tv_name);
        TextView tvsign = signLayout.findViewById(R.id.tv_name);
        String name = nickname.getText().toString();
        String sign = tvsign.getText().toString();
        SpUtils.getInstance().setValue("nickname",name);
        SpUtils.getInstance().setValue("sign",sign);*/
        ((TextView) nameLayout.findViewById(R.id.tv_name)).setText("昵称");
        ((TextView) sexLayout.findViewById(R.id.tv_name)).setText("性别");
        ((TextView) ageLayout.findViewById(R.id.tv_name)).setText("年龄");
        ((TextView) signLayout.findViewById(R.id.tv_name)).setText("签名");
        ((TextView) leaveLayout.findViewById(R.id.tv_name)).setText("等级");

    }

    @Override
    protected void initData() {
        //获取详情信息
        persenter.userDetailInfo();


    }

    @Override
    protected UserDetailInfoConstract.DetailPresenter createPersenter() {
        return new UserDetailInfoPresenter();
    }

    @Override//将访问到的数据动态的显示
    public void getUserDetailInfoReturn(UserDetailInfoBean userDetailInfoBean) {
        this.userDetailInfoBean = userDetailInfoBean;//将返回的数据对象引用全局
        //头像返回刷新显示
        if (userDetailInfoBean.getData().getAvater() != null && !TextUtils.isEmpty((CharSequence) userDetailInfoBean.getData().getAvater())) {
            ImageView head = headLayout.findViewById(R.id.iv_head);
            CircleImgUtils.userHeaderCircle((String)userDetailInfoBean.getData().getAvater(),head);

        }
        //将访问获取到的昵称显示
        if (userDetailInfoBean.getData().getNickname() != null && !TextUtils.isEmpty((CharSequence) userDetailInfoBean.getData().getNickname())) {
            ((TextView) nameLayout.findViewById(R.id.tv_info)).setText((String) userDetailInfoBean.getData().getNickname());
            SpUtils.getInstance().setValue("nickname",(String) userDetailInfoBean.getData().getNickname());
        } else {
            ((TextView) nameLayout.findViewById(R.id.tv_info)).setText("请输入昵称");
        }

        //显示性别
        if (userDetailInfoBean.getData().getSex() == 0) {
            ((TextView) sexLayout.findViewById(R.id.tv_info)).setText("无");
        } else if (userDetailInfoBean.getData().getSex() == 1) {
            ((TextView) sexLayout.findViewById(R.id.tv_info)).setText("男");
        } else {
            ((TextView) sexLayout.findViewById(R.id.tv_info)).setText("女");
        }

        //动态显示年龄
        if (userDetailInfoBean.getData().getAge() != null && !TextUtils.isEmpty(""+userDetailInfoBean.getData().getAge())) {
            ((TextView)ageLayout.findViewById(R.id.tv_info)).setText(""+userDetailInfoBean.getData().getAge());
        } else {
            ((TextView) ageLayout.findViewById(R.id.tv_info)).setText("请设置年龄");
        }

        //动态显示签名
        if (userDetailInfoBean.getData().getSign() != null && !TextUtils.isEmpty((CharSequence) userDetailInfoBean.getData().getSign())) {
            ((TextView) signLayout.findViewById(R.id.tv_info)).setText((String) userDetailInfoBean.getData().getSign());
            SpUtils.getInstance().setValue("sign",(String) userDetailInfoBean.getData().getSign());
        } else {
            ((TextView) signLayout.findViewById(R.id.tv_info)).setText("请输入签名");
        }

    }

    //网络访问获取个人信息更新返回
    @Override
    public void getUserUpdateInfoReturn(UserUpdateInfoBean updateInfoBean) {
        if (updateInfoBean.getErr() == 200) {//更新成功后返回
            if (curtv != null && !TextUtils.isEmpty(et_alog_name)) {
                curtv.setText(et_alog_name);//将弹框修改的内容赋值给控件
                et_alog_name = null;
                curtv = null;
            }

            ImageView img = headLayout.findViewById(R.id.iv_head);
            if (!TextUtils.isEmpty(newheadurl)) {
                SpUtils.getInstance().setValue("avater",newheadurl);//将新头像保存
                CircleImgUtils.userHeaderCircle(newheadurl, img, new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        newheadurl = null;
                        return false;
                    }
                });
                
            }
        }

    }


    //点击控件的监听    点击条目弹出alertDlag修改内容
    @OnClick({R.id.iv_back, R.id.tv_exitslogin, R.id.item_head, R.id.item_name, R.id.item_sex, R.id.item_age, R.id.item_leave, R.id.item_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回上一页
                finish();
                break;
            case R.id.tv_exitslogin://退出登录
                break;
            case R.id.item_head://点击头像更换
                openLoadPicture();
                break;
            case R.id.item_name://更换昵称
                curtv = nameLayout.findViewById(R.id.tv_info);
                openChangeDataAlertdlag("修改昵称", (String) userDetailInfoBean.getData().getNickname(), "nickname");
                break;
            case R.id.item_sex://更换性别
                curtv = sexLayout.findViewById(R.id.tv_info);
                openChangeDataAlertdlag("修改性别", SexUtils.parseSex(userDetailInfoBean.getData().getSex()), "sex");
                break;
            case R.id.item_age://更年龄
                curtv = ageLayout.findViewById(R.id.tv_info);
                openChangeDataAlertdlag("修改年龄", userDetailInfoBean.getData().getAge() + "", "age");
                break;
            case R.id.item_sign://更换签名
                curtv = signLayout.findViewById(R.id.tv_info);
                openChangeDataAlertdlag("修改签名", (String) userDetailInfoBean.getData().getSign(), "sign");
                break;
        }
    }

    /**
     * 打开本地相册换取头像
     */
    private void openLoadPicture() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//换取所有图片
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)//单选
                .imageSpanCount(4)//每行显示个数
                .previewImage(true)//可预览
                .isCamera(true)//显示拍照按钮
                .enableCrop(true)//是否裁剪
                .compress(true)//是否压缩
                .rotateEnabled(true)//是否可以选转
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调
    }

    /**
     * 选择图片回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST://本地相机相册图片选取的结果
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                //判断是否回调为空  没选图片  空指针
                if (selectList.size() == 0)
                    return;
//点击头像条目选择图片  修剪后传到HeadCropActivity进行上传
                //跳转图片修剪界面
                Intent intent = new Intent(UserDetailInfoActivity.this, HeadCropActivity.class);
                String cutPath = selectList.get(0).getCutPath();
                intent.putExtra("imgUrl",cutPath);
                startActivityForResult(intent, CODE_HEADCROP);
                break;

            case CODE_HEADCROP://图片修剪完成返回
                if (resultCode == 230) {
                    String imgUrl = data.getStringExtra("imgUrl");
                    newheadurl = imgUrl;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("avater",imgUrl);
                    //更新当前头像
                    persenter.userUpdateInfo(map);
                    SpUtils.getInstance().setValue("avater",imgUrl);

                }

                break;
        }
    }

    /**
     * 点击响应的条目修改对应的个人信息
     *
     * @param title
     * @param nickname
     * @param s
     */
    //打开修改个人信息的弹框
    private void openChangeDataAlertdlag(String title, String nickname, String s) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_changedata, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view)
                // .setCancelable(false)//点击其它区域不消失
                .create();                             //弹框背景  黑色阴影
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView name = view.findViewById(R.id.et_content);//修改内容
        TextView titled = view.findViewById(R.id.change_title);//弹框标题
        LinearLayout sex = view.findViewById(R.id.con_sex);//弹框性别选项
        RadioGroup rg = view.findViewById(R.id.rg);//弹框单选框项
        ImageView ivhead = view.findViewById(R.id.iv_head);//弹框修改头像
        TextView save = view.findViewById(R.id.tv_save);//弹框保存
        if (s.equals("sex")) {//判断点击的是哪个条目
            sex.setVisibility(View.VISIBLE);
            ivhead.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
        } else if (s.equals("avater")) {
            sex.setVisibility(View.GONE);
            ivhead.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
        } else {
            sex.setVisibility(View.GONE);
            ivhead.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
        }
        //设置标题
        titled.setText(title);
        //输入框显示文字
        if (!TextUtils.isEmpty(nickname)) {
            name.setHint(nickname);
        } else {
            name.setHint("请输入修改后的内容");
        }
        //点击弹框的保存按钮监听
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.equals("sex")) {
                    int checkedRadioButtonId = rg.getCheckedRadioButtonId();
                    if (checkedRadioButtonId < 1) {
                        Toast.makeText(context, "请选择性别", Toast.LENGTH_SHORT).show();
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        if (checkedRadioButtonId == R.id.rb_man) {
                            map.put(s, "1");
                            et_alog_name = "男";
                        } else {
                            map.put(s, "2");
                            et_alog_name = "女";
                        }

                        //更新信息 退出弹框
                        persenter.userUpdateInfo(map);
                        alertDialog.dismiss();
                    }
                } else {
                    et_alog_name = name.getText().toString();
                    if (!TextUtils.isEmpty(et_alog_name)) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(s, et_alog_name);//将修改后的存进map
                        //更新信息
                        persenter.userUpdateInfo(map);
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(context, "未填写修改内容", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        alertDialog.show();
    }
}
