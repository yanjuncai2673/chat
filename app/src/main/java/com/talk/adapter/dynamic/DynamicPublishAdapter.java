package com.talk.adapter.dynamic;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talk.R;
import com.talk.apps.GlideEngine;
import com.talk.models.bean.dynamic.DynamicPublishVo;

import java.util.List;

//发布动态图片列表  多布局
public class DynamicPublishAdapter extends RecyclerView.Adapter {

    OpenPhoto openphoto;
    private Context context;
    private List<DynamicPublishVo> list;

    public DynamicPublishAdapter(Context context, List<DynamicPublishVo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 0) {//条目图片正常图片
            view = LayoutInflater.from(context).inflate(R.layout.layout_normal_picture, parent, false);
            viewHolder = new NormalVH(view);
        } else {//类型1 为带加号的图片
            view = LayoutInflater.from(context).inflate(R.layout.layout_addmarket_picture, parent, false);
            viewHolder = new AddMarketVH(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddMarketVH) {
            AddMarketVH addMarketVH = (AddMarketVH) holder;
            addMarketVH.ivadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击加号访问本地相册
                    if (getItemCount() >= 9) {
                        Toast.makeText(context, "最多只能添加9张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        if (openphoto != null) {
                            openphoto.addImageClick();//图片不够9章添加
                        }
                    }
                }
            });
        } else if (holder instanceof NormalVH) {
            NormalVH normalVH = (NormalVH) holder;
            if (!TextUtils.isEmpty(list.get(position).getPath())) {
                Glide.with(context).load(list.get(position).getPath()).into(normalVH.ivpublish);
            }
            normalVH.tvdelete.setTag(position);
            normalVH.tvdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = (int) v.getTag();
                    if (posi < getItemCount()) {
                        list.remove(posi);//列表移除此条目
                        notifyDataSetChanged();//刷新
                        if (openphoto != null) {
                            openphoto.deleteImage(posi);
                        }

                    }
                }
            });

            //点击列表中的单张图片，进行预览
            normalVH.ivpublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    /**
     * 设置接口回掉的监听
     *
     * @return
     */
    public void addOnClickListener(OpenPhoto openphoto) {
        this.openphoto = openphoto;
    }

    //定义打开本地相册回调监听
    public interface OpenPhoto {
        void addImageClick();//添加图片方法

        void deleteImage(int position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }


    private class NormalVH extends RecyclerView.ViewHolder {

        TextView tvdelete;
        ImageView ivpublish;

        public NormalVH(View view) {
            super(view);
            tvdelete = view.findViewById(R.id.tv_delete);
            ivpublish = view.findViewById(R.id.iv_publish);
        }
    }

    private class AddMarketVH extends RecyclerView.ViewHolder {
        ImageView ivadd;

        public AddMarketVH(View view) {
            super(view);
            ivadd = view.findViewById(R.id.add);
        }

    }
}
