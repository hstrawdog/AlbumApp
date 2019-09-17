package com.hqq.album.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.hqq.album.Adapter.FragmentAdapter;
import com.hqq.album.AppManager;
import com.hqq.album.R;
import com.hqq.album.activity.base.BaseActivity;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.dialog.OptAnimationLoader;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.AlbumUtils;

/**
 * 预览界面
 *
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.activity
 * @FileName :   AlbumPreviewActivity
 * @Date : 2018/10/23 0023
 * @Descrive : TODO
 * @Email :
 */
@Deprecated
public class AlbumPreviewActivity extends BaseActivity implements View.OnClickListener {

    ViewPager mViewPager;
    ImageView mTvCheck;
    TextView mTvTitle;
    TextView mAlbumFinish;
    int mPosition = 0;
    private Animation mAnimation;
    ConstraintLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullWindow();
        setContentView(R.layout.activity_album_preview);
        mViewPager = findViewById(R.id.vp_preview);
        findViewById(R.id.album_back).setOnClickListener(this);
        findViewById(R.id.ll_check).setOnClickListener(this);
        mAlbumFinish.setOnClickListener(this);
        mTvCheck = findViewById(R.id.tv_check);
        mAlbumFinish = findViewById(R.id.album_finish);
        mRelativeLayout = findViewById(R.id.album_title_bar);

        mAnimation = OptAnimationLoader.loadAnimation(mContext, R.anim.modal_in);
        mTvTitle = findViewById(R.id.album_title);


        mLocalMediaList = PictureConfig.getInstance().getSelectLocalMedia();
        mTvTitle.setText(getIntent().getIntExtra(FunctionConfig.FOLDER_DETAIL_POSITION, 1) + "/" + mLocalMediaList.size());

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mLocalMediaList);
        mViewPager.setAdapter(mFragmentAdapter);

        mViewPager.setCurrentItem(getIntent().getIntExtra(FunctionConfig.FOLDER_DETAIL_POSITION, 1) - 1);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRelativeLayout.getLayoutParams();
        layoutParams.setMargins(0, AlbumUtils.getStatusBarHeight(this), 0, 0);
        mRelativeLayout.setLayoutParams(layoutParams);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvTitle.setText(position + 1 + "/" + mLocalMediaList.size());
                mPosition = position;
                mTvCheck.setSelected(isSelected(mLocalMediaList.get(position)));

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        updateSelectMenu();

    }

    private void updateSelectMenu() {
        if ((PictureConfig.getInstance().getSelectMedia().size() > 0)) {
            mAlbumFinish.setVisibility(View.VISIBLE);
            mAlbumFinish.setText("完成(" + PictureConfig.getInstance().getSelectMedia().size() + "/" + PictureConfig.getInstance().getBuilder().getMaxSelectNum() + ")");
        } else {
            mAlbumFinish.setVisibility(View.GONE);

        }
    }

    List<LocalMedia> mLocalMediaList;
    FragmentAdapter mFragmentAdapter;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_back) {
            AppManager.getAppManager().finishActivity();

        } else if (i == R.id.ll_check) {
            if (mTvCheck.isSelected()) {
                mTvCheck.setSelected(false);
                for (LocalMedia media : PictureConfig.getInstance().getSelectMedia()) {
                    if (media.getPath().equals(mLocalMediaList.get(mPosition).getPath())) {
                        PictureConfig.getInstance().getSelectMedia().remove(media);
                        break;
                    }
                }
            } else {
                if (PictureConfig.getInstance().getSelectMedia().size() < PictureConfig.getInstance().getBuilder().getMaxSelectNum()) {
                    PictureConfig.getInstance().getSelectMedia().add(mLocalMediaList.get(mPosition));
                    mTvCheck.setSelected(true);
                    mTvCheck.startAnimation(mAnimation);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.picture_message_max_num, PictureConfig.getInstance().getBuilder().getMaxSelectNum() + ""), Toast.LENGTH_LONG).show();
                }
            }
            updateSelectMenu();

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


    /**
     * 当前图片是否选中
     *
     * @param image
     * @return
     */
    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : PictureConfig.getInstance().getSelectMedia()) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }
}
