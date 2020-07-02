package com.hqq.album.dialog;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


import com.hqq.album.R;
import com.hqq.album.annotation.LocalMediaType;
import com.hqq.album.common.Album;
import com.hqq.album.common.FunctionOptions;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.entity.LocalMedia;

import java.util.ArrayList;

/**
 * @Author : huangqiqiang
 * @Package :
 * @FileName :   PhotoDialog
 * @Date : 2018/7/4 0004  上午 9:24
 * @Descrive : TODO
 * @Email :
 */
public class PhotoDialog extends AbsDialog implements View.OnClickListener {
    PhotoDialogClick mPhotoDialogClick;
    OnSelectResultCallback mOnSelectResultCallback;
    int mSelectSize = 1;


    @Deprecated
    public static PhotoDialog getPhotoDialog(PhotoDialogClick photoDialogClick) {
        PhotoDialog photoDialog = new PhotoDialog();
        photoDialog.setPhotoDialogClick(photoDialogClick);
        return photoDialog;
    }

    /**
     * @param selectSize              图片选择 张数
     * @param mOnSelectResultCallback 图片选择回调
     * @return
     */
    public static PhotoDialog getPhotoSelectDialog(int selectSize, OnSelectResultCallback mOnSelectResultCallback) {
        PhotoDialog photoDialog = new PhotoDialog();
        photoDialog.setSelectSize(selectSize);
        photoDialog.setOnSelectResultCallback(mOnSelectResultCallback);
        return photoDialog;
    }


    public void setOnSelectResultCallback(OnSelectResultCallback onSelectResultCallback) {
        mOnSelectResultCallback = onSelectResultCallback;
    }

    public void setSelectSize(int selectSize) {
        mSelectSize = selectSize;
    }

    public void setPhotoDialogClick(PhotoDialogClick photoDialogClick) {
        mPhotoDialogClick = photoDialogClick;
    }

    @Override
    public int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int setWeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int setView() {
        return R.layout.dialog_photo;
    }

    @Override
    protected void initView() {
        mRootView.findViewById(R.id.btn_taking_pictures).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_album).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_taking_pictures) {

            Album.from(PhotoDialog.this)
                    .choose(LocalMediaType.VALUE_TYPE_IMAGE)
                    .setStartUpCamera(true)
                    .forResult(0x1)
            ;

        } else if (i == R.id.tv_album) {
            Album.from(PhotoDialog.this)
                    .choose(LocalMediaType.VALUE_TYPE_IMAGE)
                    .forResult(0x1)
            ;
        } else if (i == R.id.btn_cancel) {
            dismiss();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<LocalMedia> list = data.getParcelableArrayListExtra("data");
            Log.e("---------------------", "onActivityResult: ");
            dismiss();
        }
    }

    public interface PhotoDialogClick {
        void onClickPictures();

        void onClickAlbum();
    }
}
