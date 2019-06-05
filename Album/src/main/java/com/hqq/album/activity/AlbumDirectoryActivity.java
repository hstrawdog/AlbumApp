/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hqq.album.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hqq.album.Adapter.AlbumDirectoryAdapter;
import com.hqq.album.AppManager;
import com.hqq.album.R;
import com.hqq.album.activity.base.BaseActivity;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.common.LocalMediaLoader;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.decoration.RecycleViewDivider;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.entity.LocalMediaFolder;
import com.hqq.album.utils.AlbumUtils;
import com.hqq.album.utils.AlbumFileUtils;
import com.hqq.album.weight.FilterImageView;

/**
 * 文件夹 选择界面
 *
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.activity
 * @FileName :   AlbumDirectoryActivity
 * @Date : 2018/6/8 0008  下午 3:14
 * @Descrive :
 * @Email :  593979591@qq.com
 */
public class AlbumDirectoryActivity extends BaseActivity implements AlbumDirectoryAdapter.OnItemClickListener, View.OnClickListener {

    FilterImageView mLocalAlbumCamera;

    RecyclerView mRecyclerView;
    List<LocalMediaFolder> mFolderList = new ArrayList<>();
    private AlbumDirectoryAdapter mAdapte;
    ImageView mIvProgressBar;
    TextView mTvProgress;
    private String cameraPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFolderList = null;
        PictureConfig.getInstance().setSelectLocalMedia(null);
    }


    @Override
    public void onItemClick(String folderName, List<LocalMedia> images) {

        PictureConfig.getInstance().setSelectLocalMedia(images);
        startActivity(new Intent(this, AlbumDetailActivity.class)
                        .putExtra(FunctionConfig.FOLDER_NAME, folderName)
                // .putParcelableArrayListExtra(FunctionConfig.IMAGES, (ArrayList<? extends Parcelable>) images)
        );
        // ToastUtils.show(this,folderName);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_back) {
            AppManager.getAppManager().finishAllActivity();
        } else if (i == R.id.loacal_album_camera) {
            startUpCamera();

        }
    }

    private void startUpCamera() {
        // 启动相机拍照,先判断手机是否有拍照权限
        if (hasPermission(Manifest.permission.CAMERA)) {
            startOpenCamera();
        } else {
            requestPermission(FunctionConfig.CAMERA, Manifest.permission.CAMERA);
        }
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.rcv_album_list);
        mIvProgressBar = findViewById(R.id.iv_progress_bar);
        mTvProgress = findViewById(R.id.tv_progress);
        findViewById(R.id.album_back).setOnClickListener(this);

        // 判断是否显示相机
        if (PictureConfig.getInstance().getBuilder().isDisplayCamera()) {
            mLocalAlbumCamera = findViewById(R.id.loacal_album_camera);
            mLocalAlbumCamera.setOnClickListener(this);
            mLocalAlbumCamera.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AlbumUtils.dip2px(this, 0.5f), ContextCompat.getColor(this, R.color.line_color)));
        mRecyclerView.setLayoutManager(manager);
        mAdapte = new AlbumDirectoryAdapter(this);
        mAdapte.bindFolderData(mFolderList);
        mRecyclerView.setAdapter(mAdapte);
        mAdapte.setOnItemClickListener(this);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        mIvProgressBar.startAnimation(animation);


        // 第一次启动ImageActivity，没有获取过相册列表
        // 先判断手机是否有读取权限，主要是针对6.0已上系统
        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            initData();
        } else {
            requestPermission(FunctionConfig.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }


    }

    private void initData() {
        // 判断是否直接打开相机
        if (PictureConfig.getInstance().getBuilder().isStartUpCamera()) {
            startUpCamera();
            return;
        }
        // 否则再去 读书内存中的数据
        LocalMediaLoader localMediaLoader = new LocalMediaLoader(this, PictureConfig.getInstance().getBuilder().getAlbumType(), true);
        localMediaLoader.loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                mAdapte.bindFolderData(folders);
                mIvProgressBar.clearAnimation();
                ((View) mIvProgressBar.getParent()).setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

            }
        });

    }

    /**
     * start to camera、preview、crop
     */
    public void startOpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = AlbumFileUtils.createCameraFile(this, 1);
            cameraPath = cameraFile.getAbsolutePath();
            Uri imageUri;
            String authority = getPackageName() + ".provider";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //通过FileProvider创建一个content类型的Uri
                imageUri = FileProvider.getUriForFile(this, authority, cameraFile);
            } else {
                imageUri = Uri.fromFile(cameraFile);
            }
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, FunctionConfig.REQUEST_CAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // on take photo success
            if (requestCode == FunctionConfig.REQUEST_CAMERA) {
                // 拍照返回
                File file = new File(cameraPath);
                //  int degree = AlbumFileUtils.readPictureDegree(file.getAbsolutePath());
                //rotateImage(degree, file);  旋转图片

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                // takePhotoSuccess = true;


                // 生成新拍照片或视频对象
                LocalMedia media = new LocalMedia();
                media.setPath(cameraPath);
                media.setType(1);
                List<LocalMedia> result;
                // 去压缩
                result = new ArrayList<>();
                result.add(media);
                OnSelectResultCallback resultCallback = PictureConfig.getInstance().getResultCallback();
                if (resultCallback != null) {
                    // 暂时不去判断是否 单选
                    resultCallback.onSelectSuccess(result);
                }
                AppManager.getAppManager().finishAllActivity();
                PictureConfig.getInstance().setResultCallback(null);

            }
        } else if (requestCode == FunctionConfig.REQUEST_CAMERA && resultCode != RESULT_OK) {
            // 这边是直接打开相册的 那返回的话 直接到上个界面
            if (PictureConfig.getInstance().getBuilder().isStartUpCamera()) {
                PictureConfig.getInstance().getResultCallback().onSelectSuccess(new ArrayList<LocalMedia>());
                AppManager.getAppManager().finishAllActivity();

            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FunctionConfig.READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {
                    Toast.makeText(mContext, "读取内存卡权限已被拒绝,请在系统设置中开启权限", Toast.LENGTH_SHORT).show();
                    mTvProgress.setText("哎呀!没有获取到读取内存卡权限已被拒绝,\n请在系统设置中开启权限");
                    //应用程序详情页面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
                break;
            case FunctionConfig.CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startOpenCamera();
                } else {
                    Toast.makeText(mContext, "拍照权限已被拒绝,请在系统设置中开启权限", Toast.LENGTH_SHORT).show();
                    mTvProgress.setText("哎呀!没有获取到拍照权限,\n请在系统设置中开启权限");
                    //应用程序详情页面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
                break;
            default:

                break;
        }
    }
}
