package com.talk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talk.R;
import com.talk.SmileyParser;
import com.talk.adapter.FaceDeatilListAdapter;
import com.talk.apps.MyApp;
import com.talk.models.bean.FaceListItemVo;

import java.util.List;

/**
 * 点击表情后进入表情图片列表
 */
public class FaceFragment extends Fragment {


    private RecyclerView rvFaceDetail;
    private List<FaceListItemVo> faceItemlist;
    private FaceDeatilListAdapter faceDeatilListAdapter;
    public FaceDeatilListAdapter.FaceDetailListClick click;


    /**
     * 初始化fragment
     *
     * @return
     */
    public static FaceFragment getInstance(int position, FaceDeatilListAdapter.FaceDetailListClick click) {
        FaceFragment faceFragment = new FaceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        faceFragment.setArguments(bundle);
        faceFragment.click = click;//初始化表情fragment时将监听传进去  回调监听
        return faceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = getArguments().getInt("position");
        faceItemlist = SmileyParser.getInstance(MyApp.myApp).getFaceItemListByPos(position);
        faceDeatilListAdapter = new FaceDeatilListAdapter(faceItemlist, getActivity());
        if (faceItemlist.size()>0) {
            FaceListItemVo faceListItemVo = faceItemlist.get(0);
            if (faceListItemVo.getFaceType() ==SmileyParser.FACE_TYPE_1) {
                rvFaceDetail.setLayoutManager(new GridLayoutManager(getContext(),6));
            }else {
                rvFaceDetail.setLayoutManager(new GridLayoutManager(getContext(),4));
            }
        }
        rvFaceDetail.setAdapter(faceDeatilListAdapter);
        faceDeatilListAdapter.addDetailFaceOnClickListener(this.click);//适配器添加回调监听
    }

    private void initView(View view) {
        rvFaceDetail = view.findViewById(R.id.rv_face_detail);
    }
}
