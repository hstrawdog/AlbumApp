package com.hqq.album.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.utils
 * @FileName :   AlbumScreenUtils
 * @Date : 2018/6/20 0020  下午 5:56
 * @Descrive :
 * @Email :  593979591@qq.com
 */
public class AlbumScreenUtils {
    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
