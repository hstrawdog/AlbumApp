package com.hqq.album.common;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: huangqiqiang
 * 在此写用途
 * @FileName: cn.huangqiqiang.halbum.common.FunctionOptions.java
 * @emain: 593979591@qq.com
 * @date: 2017-05-07 22:19
 */
public class FunctionOptions {
    /**
     * 多选最大可选数量
     */
    private int maxSelectNum = 9;
    /**
     * 是否显示拍照图片
     */
    private boolean displayCamera = false;
    /**
     * 启动相机 拍照
     */
    private boolean startUpCamera = false;
    /**
     * 默认显示的图片
     */
    private int albumType = FunctionKey.VALUE_TYPE_IMAGE;

    private boolean isStartUpCamera() {
        return startUpCamera;
    }

    private void setStartUpCamera(boolean startUpCamera) {
        this.startUpCamera = startUpCamera;
    }

    private void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    private int getMaxSelectNum() {
        return maxSelectNum;
    }

    private boolean isDisplayCamera() {
        return displayCamera;
    }

    private void setDisplayCamera(boolean displayCamera) {
        this.displayCamera = displayCamera;
    }

    private int getAlbumType() {
        return albumType;
    }

    private void setAlbumType(int albumType) {
        this.albumType = albumType;
    }

    public static class Builder {
        private FunctionOptions options;

        public Builder() {
            options = new FunctionOptions();
        }

        public FunctionOptions getOptions() {
            return options;
        }

        /**
         * 设置 选择的最大数量
         *
         * @param maxSelectNum
         * @return
         */
        public Builder setMaxSelectNum(int maxSelectNum) {
            options.setMaxSelectNum(maxSelectNum);
            return this;
        }

        /**
         * 隐藏相机图标
         *
         * @param
         * @return
         */
        public Builder setDisplayCamera() {
            options.setDisplayCamera(false);
            return this;
        }

        /**
         * 是否显示相机图标
         *
         * @return
         */
        public boolean isDisplayCamera() {
            return options.isDisplayCamera();
        }

        /**
         * 设置直接启动相机拍照
         *
         * @return
         */
        public Builder setStartCamera() {
            options.setStartUpCamera(true);
            return this;
        }

        /**
         * 判断是否启动相机
         *
         * @returnStartUp
         */
        public boolean isStartUpCamera() {
            return options.isStartUpCamera();
        }

        /**
         * 启动相册
         *
         * @param
         * @return
         */
        public Builder setStartAlbum() {
            options.startUpCamera = false;
            return this;
        }


        /**
         * 获取相册类型
         *
         * @return 类型
         */
        public int getAlbumType() {
            return options.getAlbumType();
        }

        /**
         * 设置相册显示的类型
         *
         * @param typeVideo
         */
        public void setAlbumType(int typeVideo) {
            options.setAlbumType(typeVideo);
        }

        /**
         * 获取选择的最大数
         *
         * @return
         */
        public int getMaxSelectNum() {
            return options.getMaxSelectNum();
        }
    }
}
