package com.talk.adapter.dynamic;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.talk.R;
import com.talk.base.BaseAdapter;

import java.util.List;

public class DynamicImgAdapter extends BaseAdapter {

    public DynamicImgAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_img_item;
    }

    @Override
    public void bindData(BaseViewHolder holder, Object o) {
        String url = (String) o;
        ImageView iv = (ImageView) holder.getView(R.id.img);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(mContext).load(url).into(iv);
        }

    }
}
