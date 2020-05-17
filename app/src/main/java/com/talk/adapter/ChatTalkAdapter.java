package com.talk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talk.R;
import com.talk.SmileyParser;
import com.talk.apps.MyApp;
import com.talk.common.ChatTalk;
import com.talk.common.Constant;
import com.talk.models.bean.ChatTalkBean;
import com.talk.utils.SpUtils;

import java.util.List;

public class ChatTalkAdapter extends RecyclerView.Adapter<ChatTalkAdapter.Vh> {
    String uid;
    List<ChatTalkBean> lists;
    Context context;

    public ChatTalkAdapter(List<ChatTalkBean> lists, Context context) {
        this.uid = SpUtils.getInstance().getString("uid");
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int peopleid;
        if (viewType == ChatTalk.MSG_SELF) {
            peopleid = R.layout.chat_right_item;//右边自己发送编辑的信息
        }else {
            peopleid = R.layout.chat_left_item;//左边接收的信息
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(peopleid,parent,false);
        Vh chatVH = new Vh(view);
        return chatVH;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        ChatTalkBean chatTalkBean = lists.get(position);//获取条目得到对象
        Vh chatVH = (Vh) holder;
        //分支判断当前的消息类型
        if(chatTalkBean.getType() == ChatTalk.MSG_TYPE_WORD){//这条消息是文字
            //将输入的char字符转化成对应的图标
            CharSequence chat = SmileyParser.getInstance(MyApp.myApp).replace(chatTalkBean.getContent());
            chatVH.txtContent.setText(chat);
            chatVH.imgContent.setVisibility(View.GONE);
            chatVH.txtContent.setVisibility(View.VISIBLE);
        }else if(chatTalkBean.getType() == ChatTalk.MSG_TYPE_IMAGE){//对象的类型是图片
            chatVH.imgContent.setVisibility(View.VISIBLE);
            chatVH.txtContent.setVisibility(View.GONE);
            Glide.with(context).load(chatTalkBean.getContent()).into(chatVH.imgContent);
        }
        //判断当前这条消息是否是自己
        String _uid = chatTalkBean.getUid();
        if(_uid.equals(uid)){
            chatVH.txtName.setVisibility(View.GONE);
            Glide.with(context).load(Constant.self_avater).into(chatVH.imgHeader);
        }else{
            chatVH.txtName.setText("张含韵");
            chatVH.txtName.setVisibility(View.VISIBLE);
            Glide.with(context).load(Constant.other_avater).into(chatVH.imgHeader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String uidlist = lists.get(position).getUid();
        if (uidlist.equals(uid)) {
            return ChatTalk.MSG_SELF;//判断消息类型 是自己还是别人的
        }
        return ChatTalk.MSG_OTHER;

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Vh extends RecyclerView.ViewHolder {
        ImageView imgHeader;
        TextView txtName;
        TextView txtContent;
        ImageView imgContent;
        public Vh(@NonNull View itemView) {
            super(itemView);
            imgHeader = itemView.findViewById(R.id.img_header);
            txtName = itemView.findViewById(R.id.txt_name);
            txtContent = itemView.findViewById(R.id.txt_content);
            imgContent = itemView.findViewById(R.id.img_content);
        }
    }
}
