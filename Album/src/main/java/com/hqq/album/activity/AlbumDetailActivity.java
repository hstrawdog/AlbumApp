package com.hqq.album.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import com.hqq.album.Adapter.AlbumDetailAdapter;
import com.hqq.album.AppManager;
import com.hqq.album.BaseActivity;
import com.hqq.album.R;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.decoration.GridSpacingItemDecoration;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.ScreenUtils;

/**
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.activity
 * @FileName :   AlbumDetailActivity
 * @Date : 2018/10/8 0008
 * @Descrive :
 * @Email :
 */
public class AlbumDetailActivity extends BaseActivity implements View.OnClickListener, AlbumDetailAdapter.OnPhotoSelectChangedListener {


    private static final int CODE_CLOSE = 0x9910;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        initViews();
    }


    TextView mTvFinish;
    TextView mTvTile;
    AlbumDetailAdapter mAlbumDetailAdapter;

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_album_detail);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, ScreenUtils.dip2px(this, 2), false));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAlbumDetailAdapter = new AlbumDetailAdapter(this, PictureConfig.getInstance().getBuilder().getMaxSelectNum());

        mRecyclerView.setAdapter(mAlbumDetailAdapter);
        mAlbumDetailAdapter.setOnPhotoSelectChangedListener(this);


        findViewById(R.id.album_back).setOnClickListener(this);
        findViewById(R.id.album_finish).setOnClickListener(this);
        mTvFinish = (TextView) findViewById(R.id.album_finish);
        mTvTile = (TextView) findViewById(R.id.album_title);
        mTvTile.setText(getIntent().getStringExtra(FunctionConfig.FOLDER_NAME));

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<LocalMedia> list = PictureConfig.getInstance().getSelectLocalMedia();
        mAlbumDetailAdapter.bindImagesData(list);
        mTvFinish.setText("完成(" + PictureConfig.getInstance().getSelectMedia().size() + "/" + PictureConfig.getInstance().getBuilder().getMaxSelectNum() + ")");

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.album_back) {
            AppManager.getAppManager().finishActivity();
        }
        if (i == R.id.album_finish) {
            OnSelectResultCallback resultCallback = PictureConfig.getInstance().getResultCallback();
            if (resultCallback != null) {
                resultCallback.onSelectSuccess(PictureConfig.getInstance().getSelectMedia());
            }
            AppManager.getAppManager().finishAllActivity();

        }
    }

    @Override
    public void onTakePhoto() {// 打开相机

    }

    @Override
    public void onChange(List<LocalMedia> selectImages) {
        mTvFinish.setText("完成(" + selectImages.size() + "/" + PictureConfig.getInstance().getBuilder().getMaxSelectNum() + ")");
    }

    @Override
    public void onPictureClick(LocalMedia media, int position) {
        startActivityForResult(new Intent(this, AlbumPreviewActivity.class)
                        .putExtra(FunctionConfig.FOLDER_DETAIL_POSITION, position + 1)
                        .putExtra(FunctionConfig.FOLDER_NAME, getIntent().getStringExtra(FunctionConfig.FOLDER_NAME))
                , CODE_CLOSE

        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CLOSE && resultCode == Activity.RESULT_OK) {
            finish();
        }

    }
}
