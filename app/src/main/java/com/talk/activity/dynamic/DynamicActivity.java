package com.talk.activity.dynamic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.talk.R;
import com.talk.adapter.dynamic.DynamicPublishAdapter;
import com.talk.apps.GlideEngine;
import com.talk.base.BaseActivity;
import com.talk.interfaces.IBasePersenter;
import com.talk.interfaces.dynamic.Dynamicstract;
import com.talk.models.HttpManager;
import com.talk.models.apis.UploadApi;
import com.talk.models.bean.dynamic.DynamicPublishVo;
import com.talk.models.bean.dynamic.DynamicSaveDataBean;
import com.talk.models.bean.dynamic.DynamicSendBean;
import com.talk.persenter.dynamic.DynamicPresenter;
import com.talk.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class DynamicActivity extends BaseActivity<Dynamicstract.Presenter> implements DynamicPublishAdapter.OpenPhoto, Dynamicstract.View {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.con_title)
    ConstraintLayout conTitle;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.rv_picture)
    RecyclerView rvPicture;
    //当前本地图片选择器的内容
    private List<LocalMedia> localMedia1;
    private List<DynamicPublishVo> list;
    private DynamicPublishAdapter dynamicPublishAdapter;
    private HashMap<String, String> upLoadImg;


    @Override
    protected int getLayout() {
        return R.layout.activity_dynamic;//发布动态布局界面
    }

    @Override
    protected void initView() {

    }

    //初始化截面数据
    @Override
    protected void initData() {

        //初始化界面数据
        localMedia1 = new ArrayList<>();//本地图片选择器
        list = new ArrayList<>();//图片多布局   动态发布bean 文字 图片
        //1.0先判断上次是否有未发布保存下来的动态   有  就解析出来
        //判断上次是否有保存的草稿信息
        String username = SpUtils.getInstance().getString("username");
        if (!TextUtils.isEmpty(username)) {//账号有值  已经登录
            String nickname = SpUtils.getInstance().getString("nickname");
            tvNickname.setText(nickname);
            //将存进账号里的json串解析出来
            String stringjson = SpUtils.getInstance().getString(username);
            if (!TextUtils.isEmpty(stringjson)) {
                DynamicSaveDataBean bean = new Gson().fromJson(stringjson, DynamicSaveDataBean.class);
                if (!TextUtils.isEmpty(bean.getWord())) {
                    etInfo.setText(bean.getWord());//发布的文字
                }
                //图片   bean 对象 图片条目   解析的图片
                for (DynamicSaveDataBean.ImageBean item : bean.getImgs()) {
                    DynamicPublishVo dynamicPublishVo = new DynamicPublishVo();
                    dynamicPublishVo.setPath(item.getPath());
                    dynamicPublishVo.setType(0);//0  正常图片   1带加号
                    list.add(dynamicPublishVo);
                }
            }

        }
        //添加加号按钮
        list.add(getAddDynamicItem());
        dynamicPublishAdapter = new DynamicPublishAdapter(this, list);
        rvPicture.setLayoutManager(new GridLayoutManager(this, 3));
        rvPicture.setAdapter(dynamicPublishAdapter);
        dynamicPublishAdapter.addOnClickListener(this);

    }

    //打开本地相册选择图片
    @Override
    public void addImageClick() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)//多选
                .selectionMedia(localMedia1)
                .maxSelectNum(9)
                .imageSpanCount(4)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .rotateEnabled(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    //删除图片  本地选中的图片
    @Override
    public void deleteImage(int position) {
        if (position < localMedia1.size()) {
            localMedia1.remove(position);
        }
    }

    //对本地相册图片返回处理 显示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            localMedia1 = PictureSelector.obtainMultipleResult(data);
            if (localMedia1.size() == 0) return;//如果返回没有图片 跳过
            updateList(localMedia1);//有图就刷新出来
        }
    }

    //更新列表                 本地媒体相册图片
    private void updateList(List<LocalMedia> localMedia1) {
        list.clear();
        //正常图片
        for (LocalMedia item : localMedia1) {
            DynamicPublishVo dynamicPublishVo = new DynamicPublishVo();
            dynamicPublishVo.setType(0);
            dynamicPublishVo.setPath(item.getPath());
            list.add(dynamicPublishVo);
        }
        //带加号添加的图片
        //每次数据更新时  在列表最后显示加号按钮
        list.add(getAddDynamicItem());
        dynamicPublishAdapter.notifyDataSetChanged();
    }

    /**
     * 处理本地相册图片返回
     *
     * @return
     */


    //加号按钮返回
    private DynamicPublishVo getAddDynamicItem() {
        DynamicPublishVo dynamicPublishVo = new DynamicPublishVo();
        dynamicPublishVo.setType(1);
        return dynamicPublishVo;
    }

    @Override
    protected Dynamicstract.Presenter createPersenter() {
        return new DynamicPresenter();
}

    //图片没有发布  或点击返回 取消  就保存下次发布
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            String content = etInfo.getText().toString();
            //判断当前的文本是否有输入内容，列表是否有图片数据
            if (!TextUtils.isEmpty(content) || this.list.size() > 1) {
                openSaveDialog();//有发布的 文本 或者 除了加号外有其他图片 就保存
                //不执行父类点击事件
                return true;
            }
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 打开提示保存草稿的弹框
     */
    private void openSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否保存此次未发布的草稿？");
        builder.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String saveData = getSaveData();
                String username = SpUtils.getInstance().getString("username");
                //保存草稿到本地
                if (!TextUtils.isEmpty(username)) {
                    SpUtils.getInstance().setValue(username, saveData);
                }
                finish();
            }
        });
        builder.show();
    }

    /**
     * 组装需要保存的草稿数据
     *
     * @return
     */
    private String getSaveData() {
        JSONObject jsonObject = new JSONObject();//最外层 json对象  jsonobject
        String word = etInfo.getText().toString();//文本
        try {
            //放入输入的文本内容
            jsonObject.put("word", word);
            //放入图片
            JSONArray imgs = new JSONArray();//图片数组
            for (DynamicPublishVo item : list) {
                JSONObject img = new JSONObject();
                img.put("path", item.getPath());
                imgs.put(img);
            }
            jsonObject.put("imgs", imgs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    //按钮点击监听
    @OnClick({R.id.tv_send,R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_send:
                checkSendData();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    //检查发布的动态数据
    private void checkSendData() {
        String word = etInfo.getText().toString();
        if (TextUtils.isEmpty(word ) && list.size() ==1) {
            Toast.makeText(context, "未输入任何发布内容", Toast.LENGTH_SHORT).show();
            return;
        }
        //有内容就上传图片
        upLoadImg = new HashMap<>();
        //图片上传
        if (list.size()>1) {
            for (int i = 0; i <list.size()-1 ; i++) {
                //多条线程执行上传
                String currentThreadId = "";
                int finalI = i;
                String finalCurrentThreadId = currentThreadId;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upLoadImg(finalCurrentThreadId,list.get(finalI).getPath());
                    }
                });
                currentThreadId = String.valueOf(thread.getId());
                thread.start();
            }
        }else {//无图片
            sendtxtDynamic();
        }
    }



    //上传图片                     多线程  线程id      图片路径
    private void upLoadImg(String currentThreadId, String path) {
        String img_format = "image/jpg";
        String key = SpUtils.getInstance().getString("username");
        //sd卡图片文件
        File file = new File(path);
        if (file.exists()) {
            //创建一个RequestBody 封装文件格式以及文件内容
            RequestBody requestFile = MultipartBody.create(MediaType.parse(img_format),file);
            String filename="head.jpg";
            try{
                filename = URLEncoder.encode(file.getName(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //创建一个MultipartBody.Part 封装的文件数据（文件流） file参数是给后台接口读取文件用，file.getName() 保存到后台的文件名字
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", filename,requestFile);
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
                        upLoadImg.put(currentThreadId,imgUrl);
                        //判断上传是否完成
                        checkUpLoadOver();
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

    /**
     * 判断上传是否完成
     */
    private void checkUpLoadOver(){
        if(upLoadImg.size() == list.size()-1){
            sendtxtDynamic();
        }
    }

    //发布文字动态
    private void sendtxtDynamic() {
        String content = etInfo.getText().toString();
        StringBuilder stringBuilder = new StringBuilder();
        for(String value : upLoadImg.values()){
            stringBuilder.append(value);
            stringBuilder.append("$");
        }
        //删除尾部多余的$
        if(stringBuilder.length() > 0) stringBuilder.deleteCharAt(stringBuilder.length()-1);
        persenter.sendDynamicData(content,stringBuilder.toString());
    }

    @Override
    public void sendDynamicReturn(DynamicSendBean dynamicSendBean) {
        if (dynamicSendBean.getErr() == 30000) {
            //获取动态发布返回成功
            finish();
        }

    }
}
