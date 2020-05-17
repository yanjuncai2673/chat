package com.talk;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.talk.adapter.ChatTalkAdapter;
import com.talk.adapter.FaceDeatilListAdapter;
import com.talk.adapter.FaceTabAdapter;
import com.talk.apps.MyApp;
import com.talk.common.ChatTalk;
import com.talk.fragments.FaceFragment;
import com.talk.models.bean.ChatTalkBean;
import com.talk.models.bean.FaceListItemVo;
import com.talk.models.bean.FaceTabVo;
import com.talk.utils.SpUtils;
import com.talk.utils.SystemUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//聊天界面
public class ChatTalkActivity extends AppCompatActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.con_title)
    ConstraintLayout conTitle;
    @BindView(R.id.rv_talk)
    RecyclerView rvTalk;
    @BindView(R.id.et_talk)
    EditText etTalk;
    @BindView(R.id.iv_face)
    ImageView ivFace;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.con_bottom)
    ConstraintLayout conBottom;
    @BindView(R.id.rv_facelist)
    RecyclerView rvFacelist;
    @BindView(R.id.vp_face)
    ViewPager vpFace;
    @BindView(R.id.con_face)
    ConstraintLayout conFace;
    @BindView(R.id.iv_talk)
    ImageView ivTalk;
    @BindView(R.id.tv_and)
    TextView tvAnd;
    @BindView(R.id.con_send)
    ConstraintLayout conSend;
    @BindView(R.id.vp_function)
    ViewPager vpFunction;
    @BindView(R.id.con_function)
    ConstraintLayout conFunction;

    private List<FaceTabVo> tabVoList;
    private FaceTabAdapter faceTabAdapter;
    List<FaceFragment> faceFragments;
    private String uid;
    //消息列表对象
    private List<ChatTalkBean> chatTalkBeans;
    private ChatTalkAdapter chatTalkAdapter;//聊天消息列表适配器
    /**
     * 《
     */
    private TextView mTvBack;
    /**
     * 张含韵
     */
    private TextView mTvName;
    private ConstraintLayout mConTitle;
    private RecyclerView mRvTalk;
    private ImageView mIvTalk;
    private EditText mEtTalk;
    private ImageView mIvFace;
    /**
     * 发送
     */
    private TextView mTvSend;
    private ConstraintLayout mConBottom;
    private RecyclerView mRvFacelist;
    private ViewPager mVpFace;
    private ConstraintLayout mConFace;
    private StringBuilder chatInput;
    private int beforeTv;
    private CharSequence tvChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_talk);
        initView();
        ButterKnife.bind(this);
        initSmailTab();
        initFaceDetailList();
        addListener();
        initTalk();

        chatInput = new StringBuilder();

        uid = SpUtils.getInstance().getString("uid");
    }

    //文字聊天内容  初始化消息列表
    private void initTalk() {
        chatTalkBeans = new ArrayList<>();
        chatTalkAdapter = new ChatTalkAdapter(chatTalkBeans, this);
        rvTalk.setLayoutManager(new LinearLayoutManager(this));
        rvTalk.setAdapter(chatTalkAdapter);
    }

    /**
     * 初始化表情tab导航
     *
     * @param
     */
    private void initSmailTab() {
        tabVoList = SmileyParser.getInstance(MyApp.myApp).getFaceTabList();
        faceTabAdapter = new FaceTabAdapter(tabVoList, this);
        rvFacelist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFacelist.setAdapter(faceTabAdapter);
    }

    /**
     * 初始化表情详情列表集合
     */
    private void initFaceDetailList() {
        faceFragments = new ArrayList<>();//所有的表情列表存储faceFragments
        int size = SmileyParser.getInstance(MyApp.myApp).getFaceListSize();
        for (int i = 0; i < size; i++) {//创建fargment复佣
            FaceFragment faceFragment = FaceFragment.getInstance(i, new FaceDeatilListAdapter.FaceDetailListClick() {
                @Override
                public void listClick(FaceListItemVo fl) {
                    if (fl.getFaceType() == SmileyParser.FACE_TYPE_1) {//先判断一下表情类型是图文混排的
                        addSmiledFace(fl);
                    } else if (fl.getFaceType() == SmileyParser.FACE_TYPE_2) {//常规表情  纯表情

                    }
                }
            });
            faceFragments.add(faceFragment);
        }
        //  Viewpager绑定fragment
        vpFace.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return faceFragments.get(position);
            }

            @Override
            public int getCount() {
                return faceFragments.size();
            }
        });

    }

    //将图文混排显示到编辑框中   添加表情图片
    private void addSmiledFace(FaceListItemVo fl) {
        String s = null;
        int selectedPos;
        //获取编辑框的光标位置
        int start = etTalk.getSelectionStart();
        //在输入文本的后边插入
        if (etTalk.getText().length() == 0 || start >= etTalk.getText().toString().length()) {
            s = etTalk.getText().toString() + fl.getTag();
            selectedPos = s.length();
            //在输入文本的开头插入
        } else if (start == 0 && etTalk.getText().length() > 0) {
            s = fl.getTag() + etTalk.getText().toString();
            selectedPos = fl.getTag().length();
        } else {//在输入文本中间插入
            String temp = etTalk.getText().toString();
            //光标前面字符截取
            String subbef = temp.substring(0, start);
            //光标后面截取
            String subafter = temp.substring(start, temp.length());
            s = subbef + fl.getTag() + subafter;
            selectedPos = s.length() - subafter.length();
        }
        CharSequence charSequence = SmileyParser.getInstance(MyApp.myApp).replace(s);
        etTalk.setText(charSequence);//但是删除需要删除好几下  ？？？
        etTalk.setSelection(selectedPos);
    }


    private void addListener() {
        //对编辑框进行监听
        etTalk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTv = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvChanged = s;

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etTalk.getText().toString())) {
                    if (tvSend.getVisibility() == View.GONE) {
                        tvSend.setVisibility(View.VISIBLE);
                        conFunction.setVisibility(View.GONE);
                    }
                } else {
                    tvSend.setVisibility(View.GONE);
                    tvAnd.setVisibility(View.VISIBLE);
                }
            }
        });

        /*//对软键盘编辑框进行监听   没起作用？？？？？？
        etTalk.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    int length = chatInput.length();//编辑框的文字
                    if (length > 0) {
                        chatInput.deleteCharAt(length - 1);
                        etTalk.setText(chatInput.toString());
                    }

                }
                return false;
            }
        });*/
    }

    //点击监听
    @OnClick({R.id.tv_back, R.id.iv_face, R.id.tv_send, R.id.tv_and})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_face:
                showfacelist();
                break;
            case R.id.tv_send:
                sendMes();
                break;
            case R.id.tv_and:
                showFunction();
                break;
        }
    }

    //点击添加  显示功能
    private void showFunction() {
        if (conFace.getVisibility() == View.GONE) {
            conFace.setVisibility(View.VISIBLE);
            SystemUtils.setKeyBroad(this,false,etTalk);

            conFunction.setVisibility(View.VISIBLE);
            vpFunction.setVisibility(View.VISIBLE);
            rvFacelist.setVisibility(View.GONE);
            vpFace.setVisibility(View.GONE);
        } else {
            conFace.setVisibility(View.GONE);
            //如果功能区已显示
            SystemUtils.setKeyBroad(this,true,etTalk);

        }
    }

    /**
     * 发送消息  通过软键盘发送消息显示的界面
     */
    private void sendMes() {//随机发送消息

        String content = etTalk.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        int own = (int) (Math.random() * 3);
        String _uid = own == 1 ? uid : "200";
        //构建消息对象
        ChatTalkBean chatMsgBean = new ChatTalkBean();
        chatMsgBean.setUid(_uid);


        chatMsgBean.setContent(content);
        chatMsgBean.setType(ChatTalk.MSG_TYPE_WORD);

        int time = (int) (new Date().getTime() / 1000);
        chatMsgBean.setTime(time);

        chatTalkBeans.add(chatMsgBean);

        //清空输入框
        etTalk.setText("");
        chatInput.delete(0, chatInput.length());//清空后删掉数据  下次重新输入  清理编辑框

        chatTalkAdapter.notifyDataSetChanged();
    }

    private void showfacelist() {
        if (conFace.getVisibility() == View.GONE) {
            conFace.setVisibility(View.VISIBLE);
            ivFace.setBackgroundResource(R.mipmap.icon_softpan);
            //关闭软键盘
            SystemUtils.setKeyBroad(this,false,etTalk);
            rvFacelist.setVisibility(View.VISIBLE);
            vpFace.setVisibility(View.VISIBLE);
            conFunction.setVisibility(View.GONE);
            vpFunction.setVisibility(View.GONE);

        }else {
            conFace.setVisibility(View.GONE);
            ivFace.setBackgroundResource(R.mipmap.silme);
            //软键盘显示和隐藏
            SystemUtils.setKeyBroad(this,true,etTalk);
        }
    }

    private void initView() {
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mConTitle = (ConstraintLayout) findViewById(R.id.con_title);
        mRvTalk = (RecyclerView) findViewById(R.id.rv_talk);
        mIvTalk = (ImageView) findViewById(R.id.iv_talk);
        mEtTalk = (EditText) findViewById(R.id.et_talk);
        mIvFace = (ImageView) findViewById(R.id.iv_face);
        mTvSend = (TextView) findViewById(R.id.tv_send);
        mConBottom = (ConstraintLayout) findViewById(R.id.con_bottom);
        mRvFacelist = (RecyclerView) findViewById(R.id.rv_facelist);
        mVpFace = (ViewPager) findViewById(R.id.vp_face);
        mConFace = (ConstraintLayout) findViewById(R.id.con_face);
    }

}
