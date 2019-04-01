package com.hqq.album.common;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import com.hqq.album.activity.AlbumDirectoryActivity;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.Utils;

/**
 * 把启动相关的东西 写到这里
 *
 * @version V1.0 <描述当前版本功能>
 * @author: huangqiqiang
 * 在此写用途
 * @FileName: cn.huangqiqiang.halbum.common.PictureConfig.java
 * @emain: 593979591@qq.com
 * @date: 2017-05-07 21:51
 */
public class PictureConfig {
    /**
     * 单例对象
     */
    public static PictureConfig sInstance;

    public static PictureConfig getInstance() {
        if (sInstance == null) {
            synchronized (PictureConfig.class) {
                if (sInstance == null) {
                    sInstance = new PictureConfig();
                }
            }
        }
        return sInstance;
    }

    /**
     * 缓存 相册的最基本 属性
     */
    public FunctionOptions.Builder sBuilder;
    /**
     * 全局回调
     * 使用完需要 置空 或者会内存泄漏
     */
    public OnSelectResultCallback mResultCallback;
    /**
     * 选择的图片集合
     */
    protected List<LocalMedia> mSelectLocalMedia;

    public List<LocalMedia> getSelectLocalMedia() {
        return mSelectLocalMedia;
    }

    public void setSelectLocalMedia(List<LocalMedia> selectLocalMedia) {
        mSelectLocalMedia = selectLocalMedia;
    }


    public FunctionOptions.Builder getBuilder() {
        if (sBuilder == null) {
            sBuilder = new FunctionOptions.Builder();
        }
        return sBuilder;
    }

    public void setBuilder(FunctionOptions.Builder builder) {
        sBuilder = builder;
    }

    /**
     * 启动相册
     */
    public void openPhoto(Activity activity, int maxSelectNum, OnSelectResultCallback resultCall) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        if (sBuilder == null) {
            sBuilder = new FunctionOptions.Builder();
        }
        Intent intent = new Intent(activity, AlbumDirectoryActivity.class);
        activity.startActivity(intent);
        // 绑定图片接口回调函数事件
        mResultCallback = resultCall;
        getBuilder().setMaxSelectNum(maxSelectNum);
        // 清除 上次选择过的数据
        if (getSelectMedia() != null) {
            getSelectMedia().clear();
        }
    }

    /**
     * 可配置的 启动方式  后面参数 要是多了  这种就比较靠谱了
     *
     * @param activity
     * @param builder
     * @param resultCall
     */
    public void openPhoto(Activity activity, FunctionOptions.Builder builder, OnSelectResultCallback resultCall) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        if (sBuilder == null) {
            sBuilder = new FunctionOptions.Builder();
        }
        Intent intent = new Intent(activity, AlbumDirectoryActivity.class);
        activity.startActivity(intent);
        // 绑定图片接口回调函数事件
        mResultCallback = resultCall;
        setBuilder(builder);
        // 清除 上次选择过的数据
        if (getSelectMedia() != null) {
            getSelectMedia().clear();
        }
    }


    /**
     * 选择的数据
     */

    private List<LocalMedia> mSelectMedia = new ArrayList<>();

    public List<LocalMedia> getSelectMedia() {
        return mSelectMedia;
    }

    public OnSelectResultCallback getResultCallback() {
        return mResultCallback;
    }

    public void setResultCallback(OnSelectResultCallback resultCallback) {
        this.mResultCallback = resultCallback;
    }
}
