package com.talk.models.bean;

/**
 * 聊天信息列表
 */
public class ChatTalkBean {
    private int type;//消息类别
    private String uid;//消息id
    private int time;//消息产生的时间
    private int isRead;//消息是否已读
    private String content;//消息内容

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
