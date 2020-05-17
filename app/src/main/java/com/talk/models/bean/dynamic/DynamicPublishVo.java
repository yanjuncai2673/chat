package com.talk.models.bean.dynamic;
//发布动态图片类型  和 图片
public class DynamicPublishVo {

    private int type;//0没用加号  显示正常图片类型  1显示加好 添加图片
    private String path;//图片本地路径

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
