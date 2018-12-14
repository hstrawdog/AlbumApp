/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hqq.album.common;

/**
 * 在此写用途
 * 配置 静态文件
 * @version V1.0 <描述当前版本功能>
 * @FileName: cn.huangqiqiang.halbum.common.FunctionConfig.java
 * @author: 黄其强
 * @date: 2017-05-04 20:15
 */
public class FunctionConfig {
    /**
     *  图片
     */
    public static final int TYPE_IMAGE = 1;
    /**
     * // 视频
     */
    public static final int TYPE_VIDEO = 2;
    public final static int REQUEST_CAMERA = 99;

    public static final int READ_EXTERNAL_STORAGE = 0x01;
    public static final int CAMERA = 0x02;
    /**
     *  //详情
     */
    public static final String IMAGES = "images";
    public static final String FOLDER_NAME = "folderName";
    public static final String FOLDER_DETAIL_POSITION = "position";
    public static final String SELECT_IMAGE = "select_img";
}
