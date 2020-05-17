package com.talk.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talk.R;
import com.talk.SmileyParser;
import com.talk.apps.MyApp;
import com.talk.common.Constant;
import com.talk.models.bean.FaceListItemVo;
import com.talk.utils.DpTools;

import java.io.InputStream;
import java.util.List;

/**
 * 各个表情图片列表 的适配器
 */
public class FaceDeatilListAdapter extends RecyclerView.Adapter<FaceDeatilListAdapter.Vh> {
    List<FaceListItemVo>lists;
    Context context;
    private ViewGroup.LayoutParams params;
    private FaceDetailListClick faceDetailListClick;

    public FaceDeatilListAdapter(List<FaceListItemVo> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.face_detail_item, parent, false);
        Vh vh = new Vh(view);
        ViewGroup.LayoutParams params = vh.iv.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //区分大图小图
        if (viewType == SmileyParser.FACE_TYPE_1) {
            vh.tv.setVisibility(View.GONE);
            params.width = DpTools.dptopx(MyApp.myApp,Constant.FACE_SMALL_W);
            params.height = DpTools.dptopx(MyApp.myApp, Constant.FACE_SMALL_H);
        }else {
            vh.tv.setVisibility(View.VISIBLE);
            params.width = DpTools.dptopx(MyApp.myApp,Constant.FACE_BIG_W);
            params.height = DpTools.dptopx(MyApp.myApp, Constant.FACE_BIG_H);
        }
        vh.iv.setLayoutParams(params);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        Vh vh = holder;
        FaceListItemVo faceListItemVo = lists.get(position);
        InputStream inputStream = MyApp.myApp.getResources().openRawResource(faceListItemVo.getFaceId());
        BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
        if (faceListItemVo.getFaceType() == SmileyParser.FACE_TYPE_1) {
            bitmapDrawable.setBounds(0,0,40,40);
        }else {
            bitmapDrawable.setBounds(0,0,60,60);
        }
        vh.iv.setImageDrawable(bitmapDrawable);
        vh.iv.setTag(faceListItemVo);

        vh.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceListItemVo fl = (FaceListItemVo) v.getTag();//获取存放到Tag里的变量对象
                if (faceDetailListClick != null) {
                    Constant.curItemVo = fl;//将此对象点击回调存到全局变量curItemVo里
                    faceDetailListClick.listClick(fl);
                }
            }
        });
    }

    public void addDetailFaceOnClickListener(FaceDetailListClick faceDetailListClick){
        this.faceDetailListClick = faceDetailListClick;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Vh extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        public Vh(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_face_name);
            iv = itemView.findViewById(R.id.iv_face_item);
        }
    }

    @Override//获取条目类型 是大图   小图
    public int getItemViewType(int position) {
        return lists.get(position).getFaceType();
    }

    //表情条码的监听回调
    public  interface FaceDetailListClick{
        void listClick(FaceListItemVo fl);//回调监听也可以将这个条目的对象传出去  不一定是条目的位置
    }
}
