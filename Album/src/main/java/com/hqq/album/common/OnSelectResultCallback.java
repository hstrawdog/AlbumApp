package com.hqq.album.common;

import java.util.List;

import com.hqq.album.entity.LocalMedia;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: huangqiqiang
 * 在此写用途
 * @FileName: cn.hqq.halbum.common.OnSelectResultCallback.java
 * @emain: 593979591@qq.com
 * @date: 2018-11-02 15:14
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
