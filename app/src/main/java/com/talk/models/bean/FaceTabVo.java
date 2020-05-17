package com.talk.models.bean;

/**
 * 表情列表里的Tab数据
 *
 */
public class FaceTabVo {

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    private int faceId;  //tab id
    private int postion; //tab对应的下标  不同tab face


}
