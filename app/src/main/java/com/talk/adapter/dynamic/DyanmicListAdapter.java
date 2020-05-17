package com.talk.adapter.dynamic;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talk.R;
import com.talk.base.BaseAdapter;
import com.talk.models.bean.dynamic.DynamicBean;
import com.talk.utils.CircleImgUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 各个账号的动态  文字 图片  加关注  点赞
 */
public class DyanmicListAdapter extends BaseAdapter {

    public DyanmicListAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_dynamic_list_item;
    }

    @Override
    public void bindData(BaseViewHolder holder, Object o) {
        //绑定动态数据
        DynamicBean.DataBean data = (DynamicBean.DataBean) o;
        //找控件include
        ConstraintLayout layoutuser = (ConstraintLayout) holder.getView(R.id.con_username);
        //头像控件   显示头像
        ImageView head = layoutuser.findViewById(R.id.iv_head);
        if(!TextUtils.isEmpty((CharSequence) data.getAvater())){
            CircleImgUtils.userHeaderCircle(data.getAvater(),head);
        }

        //显示昵称
        TextView nickname = layoutuser.findViewById(R.id.tv_nickname);
        if (!TextUtils.isEmpty(data.getUsername())) {
            nickname.setText(data.getUsername());
        }

        //显示时间发布
        TextView time = layoutuser.findViewById(R.id.tv_time);
        if (!TextUtils.isEmpty(String.valueOf(data.getTime()))) {
            time.setText(String.valueOf(data.getTime()));
        }

        //显示发布内容
        TextView content = (TextView) holder.getView(R.id.tv_content);
        if (!TextUtils.isEmpty(data.getContent())) {
            content.setText(data.getContent());
        }

        //显示发布着的图片列表
        RecyclerView rvimgs = (RecyclerView) holder.getView(R.id.rv_img);
        if (!TextUtils.isEmpty(data.getResources())) {
            rvimgs.setVisibility(View.VISIBLE);
            String[] split = data.getResources().split("$");
            List<String> strings = Arrays.asList(split);//将图片字符串数组 Arrays.alist 转化成集合
            rvimgs.setLayoutManager(new GridLayoutManager(mContext,3));
            DynamicImgAdapter dynamicImgAdapter = new DynamicImgAdapter(strings, mContext);
            rvimgs.setAdapter(dynamicImgAdapter);
            content.setText(data.getContent());
        }else {
            rvimgs.setVisibility(View.GONE);
        }


    }
}
