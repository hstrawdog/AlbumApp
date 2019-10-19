package com.hqq.album.common;

import com.hqq.album.entity.LocalMedia;

import java.util.List;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: huangqiqiang
 * 在此写用途
 * @FileName: cn.hqq.halbum.common.OnSelectResultCallback.java
 * @emain: 593979591@qq.com
 * @date: 2018-11-02 15:14
 * 单利的回调会造成内存泄漏
 */
public interface OnSelectResultCallback {
    /**
     * 处理成功
     * 多选
     *
     * @param resultList
     */
    void onSelectSuccess(List<LocalMedia> resultList);

}
