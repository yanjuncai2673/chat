package com.talk;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.talk.models.bean.FaceListItemVo;
import com.talk.models.bean.FaceTabVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileyParser {

    private Context mContext;
    private String[] faceTabArr;
    private String[] mSmileyTexts;
    private String[] mSmileyIcons;
    private Pattern mPattern;
    private HashMap<String, Integer> mSmileyToRes;
    private List<FaceTabVo> faceTabList;
    //将所有的表情列表
    private HashMap<Integer,List<FaceListItemVo>> allFaceMap;

    public static int FACE_TYPE_1 = 1; //图文混排的表情
    public static int FACE_TYPE_2 = 2; //常规的表情

    //表情列表里的tab 图片id
    public static final int[] FACE_TABS = {
            R.mipmap.baobao,
            R.mipmap.icon_010_cover,
    };

    //小表情资源
    public static final int[] DEFAULT_SMILEY_RES_IDS = {
            //raw 流的资源文件
            R.raw.aini,
            R.raw.aoteman,
            R.raw.baibai,
            R.raw.baobao,
            R.raw.beiju,
            R.raw.beishang,
            R.raw.bianbian,
            R.raw.bishi,
            R.raw.bizui,
            R.raw.buyao,
            R.raw.chanzui,
    };

    //大表情资源
    public static final int[] DEFAULT_SMILEY_RES_ICONS = {
            R.raw.icon_002,
            R.raw.icon_007,
            R.raw.icon_010,
            R.raw.icon_012,
            R.raw.icon_013,
            R.raw.icon_018,
            R.raw.icon_019,
            R.raw.icon_020,
            R.raw.icon_021,
            R.raw.icon_022,
            R.raw.icon_024,
            R.raw.icon_027,
            R.raw.icon_029,
            R.raw.icon_030,
            R.raw.icon_035,
            R.raw.icon_040,
    };

    //利用单例  每次只能创建一个
    private static SmileyParser smileyParser;
    public static SmileyParser getInstance(Context context) {
        synchronized (SmileyParser.class) {
            if (smileyParser == null) {
                synchronized (SmileyParser.class) {
                    smileyParser = new SmileyParser(context);
                }
            }
        }
        return smileyParser;
    }
//最先执行构造方法
    public SmileyParser(Context context) {
        mContext = context;
        faceTabArr = mContext.getResources().getStringArray(R.array.smile_tab);
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mSmileyIcons = mContext.getResources().getStringArray(R.array.default_smiley_icon);
        mPattern = buildPattern();
        //表情列表
        buildFace();
        //tab数据初始化
        initFaceTab();

    }

    public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;

    private HashMap<String,Integer>buildSmileyToRes(){
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(mSmileyTexts.length);
        for (int i = 0; i < mSmileyTexts.length; i++) {
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
        }

        return smileyToRes;
    }


    /**
     * 表情正则表达式的初始化  把表情文字初始化正则表达式
     * @return
     */
    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder((mSmileyTexts.length+mSmileyIcons.length) * 3);
        patternString.append('(');
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        for (String s : mSmileyIcons){
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }


    /**
     * 初始化face资源
     */
    private void buildFace(){
        allFaceMap = new HashMap<>();
        List<FaceListItemVo> list_small = getFaceItemList(mSmileyTexts,DEFAULT_SMILEY_RES_IDS,FACE_TYPE_1);
        allFaceMap.put(0,list_small);
        List<FaceListItemVo> list_big = getFaceItemList(mSmileyIcons,DEFAULT_SMILEY_RES_ICONS,FACE_TYPE_2);
        allFaceMap.put(1,list_big);
    }

    private List<FaceListItemVo> getFaceItemList(String[] arr,int[] ids,int faceType){
        List<FaceListItemVo> list = new ArrayList<>();
        for(int i=0; i<arr.length; i++){
            String temp = arr[i];
            int end = temp.lastIndexOf("]");
            String name = temp.substring(1,end-1);
            FaceListItemVo itemVo = new FaceListItemVo();
            itemVo.setFaceId(ids[i]);
            itemVo.setName(name);
            itemVo.setTag(temp);
            itemVo.setPosition(i);
            itemVo.setFaceType(faceType);
            list.add(itemVo);
        }
        return list;
    }

    private void  initFaceTab(){
        faceTabList = new ArrayList<>();
        for(int i=0; i<faceTabArr.length; i++){
            FaceTabVo tabVo = new FaceTabVo();
            tabVo.setFaceId(FACE_TABS[i]);
            tabVo.setPostion(i);
            faceTabList.add(tabVo);
        }
    }

    public List<FaceTabVo> getFaceTabList(){
        return faceTabList;
    }

    /**
     * 获取对应tab分类下的表情
     * @param pos
     * @return
     */
    public List<FaceListItemVo> getFaceItemListByPos(int pos){
        return allFaceMap.get(pos);
    }

    public int getFaceListSize(){
        return allFaceMap.size();
    }

    /**
     * 解析表情内容
     * @param text  将表情对应的文字转化成表情显示出来
     * @return
     */
    public  CharSequence replace(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);//创建富文本
        Matcher matcher = mPattern.matcher(text);//正则表达式匹配  表情char字符 对应表情图
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            builder.setSpan(new ImageSpan(mContext, resId),matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }


    public CharSequence addSmaile(int resId){
        String str = "[0]";
        SpannableString span = new SpannableString(str);
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        ImageSpan img = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
        int start = str.indexOf("[");
        int end = str.indexOf("]")+1;
        span.setSpan(img,start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return span;
    }




}
