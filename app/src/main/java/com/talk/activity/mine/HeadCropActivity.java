package com.talk.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.talk.R;
import com.talk.base.BaseActivity;
import com.talk.interfaces.IBasePersenter;
import com.talk.models.HttpManager;
import com.talk.models.apis.UploadApi;
import com.talk.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 图片修剪界面
 */
public class HeadCropActivity extends BaseActivity {


    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_true)
    TextView tvTrue;
    @BindView(R.id.con_title)
    ConstraintLayout conTitle;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    private String img_path;

    @Override
    protected int getLayout() {
        return R.layout.activity_head_crop;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        //获取上个页面传进来的图片
        img_path = getIntent().getStringExtra("imgUrl");
        if (!TextUtils.isEmpty(img_path)) {
            Glide.with(this).load(img_path).into(ivHead);
        }
    }

    @Override
    protected IBasePersenter createPersenter() {
        return null;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_true:
                updateLoadImage();
                break;
        }
    }

    //上传头像图片到服务器
    private void updateLoadImage() {
        String img_format = "image/jpg";
        String key = SpUtils.getInstance().getString("username");
        //sd卡图片文件
        File file = new File(img_path);
        if(file.exists()){
            //创建一个RequestBody 封装文件格式以及文件内容
            RequestBody requestFile = MultipartBody.create(MediaType.parse(img_format),file);
            //创建一个MultipartBody.Part 封装的文件数据（文件流） file参数是给后台接口读取文件用，file.getName() 保存到后台的文件名字
            MultipartBody.Part part = MultipartBody.Part.createFormData("file",file.getName(),requestFile);
            //设置对应的key application/json; charset=utf-8
            RequestBody key_file = RequestBody.create(MediaType.parse("multipart/form-data"),key);
            //通过requestbody传值到后台接口
            //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),key);
            //创建retrofit
            UploadApi uploadApi = HttpManager.getInstance().getUploadApi();
            retrofit2.Call<ResponseBody> call = uploadApi.uploadFile(key_file,part);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.i("onResponse",response.body().toString());
                    try {
                        String result = response.body().string();
                        //更新到数据服务器 逻辑服务器 把得到的图片的外网路径回传到详情页面，由详情页面处理用户数据的接口更新
                        JSONObject jsonObj = new JSONObject(result);
                        String imgUrl = jsonObj.getJSONObject("data").getString("url");
                        Intent intent = getIntent();
                        intent.putExtra("imgUrl",imgUrl);
                        setResult(230,intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.i("onFailure",t.getMessage());
                }
            });
        }else{
            Toast.makeText(this,"找不到本地文件",Toast.LENGTH_SHORT).show();
        }

    }
}
