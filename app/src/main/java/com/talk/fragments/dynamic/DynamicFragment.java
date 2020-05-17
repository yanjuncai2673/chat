package com.talk.fragments.dynamic;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.talk.R;
import com.talk.activity.dynamic.DynamicActivity;
import com.talk.adapter.dynamic.DyanmicListAdapter;
import com.talk.base.BaseFragment;
import com.talk.interfaces.dynamic.Dynamicstract;
import com.talk.models.bean.dynamic.DynamicBean;
import com.talk.persenter.dynamic.DynamicListPresenter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DynamicFragment extends BaseFragment<Dynamicstract.DynamicPresenter> implements Dynamicstract.DynamicView {
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.con_title)
    ConstraintLayout conTitle;
    @BindView(R.id.rv_dynamic)
    RecyclerView rvDynamic;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    private List<DynamicBean.DataBean> dynamicBeans;
    private DyanmicListAdapter dyanmicListAdapter;

    //初始化页码 每页数量 当前动态id
    int page = 0,size = 21,trendsid = 0;
    boolean ismore = true;//是否加载更多数据

    @Override
    protected int getLayout() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView() {
        //初始化view
        dynamicBeans = new ArrayList<>();
        rvDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDynamic.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        dyanmicListAdapter = new DyanmicListAdapter(dynamicBeans, getContext());
        rvDynamic.setAdapter(dyanmicListAdapter);

        /**
         * 上拉加载  下拉刷新 监听
         */
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                if (dynamicBeans.size()>0) {
                    trendsid = dynamicBeans.get(0).getId();
                }else {
                    trendsid = 0;
                }
                persenter.getDynamicList(page,size,trendsid);
            }
        });
        //加载更多监听
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (dynamicBeans.size() == 0) {
                    srl.finishLoadMore();//没有数据直接关闭加载更多图画
                    return;
                }
                if (ismore ) {
                    page++;
                    trendsid = dynamicBeans.get(dynamicBeans.size()-1).getId();
                    persenter.getDynamicList(page,size,trendsid);
                }else {
                    Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    @Override
    protected void initData() {
        //访问网络接口请求数据
        persenter.getDynamicList(page,size,trendsid);

    }

    @Override
    protected Dynamicstract.DynamicPresenter createPersenter() {
        return new DynamicListPresenter();
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                openDynamicInfo();
                break;
        }
    }

    private void openDynamicInfo() {
        Intent intent = new Intent(getContext(), DynamicActivity.class);
        startActivity(intent);
    }

    //访问动态列表请求数据返回
    @Override
    public void getDynamicListReturn(DynamicBean dynamicBean) {
            if(dynamicBean.getErr() == 200){//请求成功
                if (dynamicBean.getData() != null && dynamicBean.getData().size()>0) {
                    //当 page 为1 刷新页面数据
                    if (page == 0) {
                        srl.finishRefresh();
                        //向列表的头插入数据
                        dyanmicListAdapter.refreshList(dynamicBean.getData());
                    } else{
                        //数据上拉加载更多  判断是否加载完成
                        if (dynamicBean.getData().size()<size) {
                            ismore = false;
                        }
                        srl.finishLoadMore();//关闭加载更多图画
                        dyanmicListAdapter.addDataFooter(dynamicBean.getData());
                    }
                }
            }else if(dynamicBean.getErr() == 30002){//没有更多数据
                Toast.makeText(context, dynamicBean.getErrmsg(), Toast.LENGTH_SHORT).show();
                if (page != 0) {//上拉加载没有更多数据
                    srl.finishLoadMore();
                    ismore = false;
                }else {
                    srl.finishRefresh();
                }
            }

    }
}
