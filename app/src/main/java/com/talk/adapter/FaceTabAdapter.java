package com.talk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talk.R;
import com.talk.models.bean.FaceTabVo;

import java.util.List;

public class FaceTabAdapter extends RecyclerView.Adapter<FaceTabAdapter.ViewHolder> {
    private List<FaceTabVo>list;
    Context context;
    private TabClickListerner tabClickListerner;

    public FaceTabAdapter(List<FaceTabVo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.face_tab_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewHolder viewHolder = holder;
        FaceTabVo faceTabVo = list.get(position);
        viewHolder.facetab.setImageResource(faceTabVo.getFaceId());
        viewHolder.facetab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabClickListerner != null) {
                   FaceTabVo tabVo = (FaceTabVo) v.getTag();
                   tabClickListerner.onTabClick(tabVo.getPostion());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView facetab;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             facetab = itemView.findViewById(R.id.iv_face_tab);
        }
    }

    //点击回掉接口
    public interface TabClickListerner{
        void onTabClick(int position);
    }
//添加tab回调监听
    public void addTabClickListener(TabClickListerner tabClickListerner){
        this.tabClickListerner = tabClickListerner;
    }
}
