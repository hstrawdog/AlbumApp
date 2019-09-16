package com.hqq.album.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqq.album.AppManager;
import com.hqq.album.R;
import com.hqq.album.activity.base.BaseActivity;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.utils.AlbumUtils;
import com.hqq.album.weight.FilterImageView;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.album.activity
 * @FileName :   AlbumPreviewV2Activity
 * @Date : 2019/9/16 0016  下午 8:42
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class AlbumPreviewV2Activity extends BaseActivity implements View.OnClickListener {


    private RecyclerView mRcAlbumList;
    private ConstraintLayout mAlbumTitleBar;
    private FilterImageView mAlbumBack;
    private TextView mAlbumTitle;
    private LinearLayout mLlCheck;
    private ImageView mTvCheck;
    private LinearLayout mLlComplete;
    private TextView mAlbumFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview_v2);
        initView();


    }

    private void initView() {
        mRcAlbumList = (RecyclerView) findViewById(R.id.rc_album_list);
        mAlbumTitleBar = (ConstraintLayout) findViewById(R.id.album_title_bar);
        mAlbumBack = (FilterImageView) findViewById(R.id.album_back);
        mAlbumTitle = (TextView) findViewById(R.id.album_title);
        mLlCheck = (LinearLayout) findViewById(R.id.ll_check);
        mTvCheck = (ImageView) findViewById(R.id.tv_check);
        mLlComplete = (LinearLayout) findViewById(R.id.ll_complete);
        mAlbumFinish = (TextView) findViewById(R.id.album_finish);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mAlbumTitleBar.getLayoutParams();
        layoutParams.setMargins(0, AlbumUtils.getStatusBarHeight(this), 0, 0);
        mAlbumTitleBar.setLayoutParams(layoutParams);


        mAlbumBack.setOnClickListener(this);
        mLlCheck.setOnClickListener(this);
        mAlbumFinish.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_back) {
            AppManager.getAppManager().finishActivity();
        } else if (i == R.id.album_finish) {
            OnSelectResultCallback resultCallback = PictureConfig.getInstance().getResultCallback();
            if (resultCallback != null) {
                resultCallback.onSelectSuccess(PictureConfig.getInstance().getSelectMedia());
            }
            setResult(Activity.RESULT_OK);
            AppManager.getAppManager().finishAllActivity();
            PictureConfig.getInstance().setResultCallback(null);

        }


    }
}
