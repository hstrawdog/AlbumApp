package com.hqq.album.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.hqq.album.AppManager;
import com.hqq.album.BaseActivity;
import com.hqq.album.R;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.dialog.OptAnimationLoader;
import com.hqq.album.entity.LocalMedia;

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
public class AlbumPreviewActivity extends BaseActivity implements View.OnClickListener {

    ViewPager mViewPager;
    TextView mTvCheck;
    TextView mTvTttle;
    TextView mAlbumFinish;
    int mPosition = 0;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview);
        mViewPager = (ViewPager) findViewById(R.id.vp_preview);
        findViewById(R.id.album_back).setOnClickListener(this);
        findViewById(R.id.ll_check).setOnClickListener(this);
        mTvCheck = (TextView) findViewById(R.id.tv_check);
        mAlbumFinish = (TextView) findViewById(R.id.album_finish);
        mAlbumFinish.setOnClickListener(this);

        mAnimation = OptAnimationLoader.loadAnimation(mContext, R.anim.modal_in);
        mTvTttle = ((TextView) findViewById(R.id.album_title));


        mLocalMediaList = PictureConfig.getInstance().getSelectLocalMedia();
        mTvTttle.setText(getIntent().getIntExtra(FunctionConfig.FOLDER_DETAIL_POSITION, 1) + "/" + mLocalMediaList.size());


        mFragmentAdapte = new FragmentAdapte(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapte);

        mViewPager.setCurrentItem(getIntent().getIntExtra(FunctionConfig.FOLDER_DETAIL_POSITION, 1) - 1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvTttle.setText(position + 1 + "/" + mLocalMediaList.size());
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
    FragmentAdapte mFragmentAdapte;

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

        }
    }

    public class FragmentAdapte extends FragmentPagerAdapter {

        public FragmentAdapte(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            AlbumPreviewFragment fragment = AlbumPreviewFragment.getInstance(mLocalMediaList.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return mLocalMediaList.size();
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
