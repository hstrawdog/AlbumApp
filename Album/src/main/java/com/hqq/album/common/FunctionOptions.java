package com.hqq.album.common;

import android.app.Activity;
import android.content.Intent;

import com.hqq.album.activity.AlbumDetailActivity;
import com.hqq.album.activity.AlbumDirectoryActivity;
import com.hqq.album.annotation.LocalMediaType;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: huangqiqiang
 * 在此写用途
 * @FileName: cn.huangqiqiang.halbum.common.FunctionOptions.java
 * @emain: 593979591@qq.com
 * @date: 2017-05-07 22:19
 */
public class FunctionOptions {
    Album mAlbum;
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
    private int albumType = LocalMediaType.VALUE_TYPE_IMAGE;

    public FunctionOptions(Album album) {
        mAlbum = album;
    }

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

    protected FunctionOptions setAlbumType(int albumType) {
        this.albumType = albumType;
        return this;
    }


    public void forResult(int requestCode) {
        Activity activity = mAlbum.getActivity();
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        activity.startActivityForResult(intent, requestCode);


    }
}
