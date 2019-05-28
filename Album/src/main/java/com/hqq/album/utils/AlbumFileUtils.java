package com.hqq.album.utils;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @version V1.0 <描述当前版本功能>
 * @author: huangqiqiang
 * 在此写用途
 * @FileName: cn.huangqiqiang.halbum.utils.AlbumFileUtils.java
 * @emain: 593979591@qq.com
 * @date: 2017-05-07 21:25
 */
public class AlbumFileUtils {
    public static final String POSTFIX = ".JPEG";
    public static final String POST_VIDEO = ".mp4";
    public static final String APP_NAME = "ImageSelector";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";

    public static File createCameraFile(Context context, int type) {
        return createMediaFile(context, CAMERA_PATH, type);
    }

    private static File createMediaFile(Context context, String parentPath, int type) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()) {

        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = null;
        switch (type) {
            case 1:
                tmpFile = new File(folderDir, fileName + POSTFIX);
                break;
            case 2:
                tmpFile = new File(folderDir, fileName + POST_VIDEO);
                break;
            default:
        }
        return tmpFile;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
