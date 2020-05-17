package com.talk.models.bean.dynamic;


import java.util.List;

/**
 * 将未发布的动态txt 及图片 保存下来方便下次使用  bean类
 */
public class DynamicSaveDataBean {
    /**
     * word : xxxxxxxx
     * imgs : [{"path":"xxxx/xx.jpg"},{"path":"xxxxx/xx1.jpg"}]
     */
    private String word;//发布文字
    private List<ImageBean> imgs;//图片集合（多张）

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<ImageBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImageBean> imgs) {
        this.imgs = imgs;
    }

    public static class ImageBean {
        /**
         * path : xxxx/xx.jpg
         */

        private String path;//图片路径

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
