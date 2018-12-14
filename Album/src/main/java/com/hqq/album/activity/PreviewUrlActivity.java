/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

/*
 * Create on 2017-1-22 下午2:10
 * FileName: PreviewActivity.java
 * Author: huang qiqiang
 * Contact: http://www.huangqiqiang.cn
 * Email 593979591@QQ.com
 *
 */

package com.hqq.album.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.hqq.album.BaseActivity;
import com.hqq.album.R;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.entity.LocalMedia;

/**
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.activity
 * @FileName :   PreviewUrlActivity
 * @Date : 2018/9/3 0003
 * @Descrive :
 * @Email :
 */
public class PreviewUrlActivity extends BaseActivity implements View.OnClickListener {
    ViewPager mViewPager;
    TextView mTvCheck;
    TextView mTvTttle;
    int mPosition = 0;
    List<String> mLocalMediaList;
    FragmentAdapte mFragmentAdapte;

    public static void goPreviewUrlActivity2LocalMedia(Activity context, List<LocalMedia> data, int position) {

        List<String> list = new ArrayList<>();
        for (LocalMedia localMedia : data) {
            list.add(localMedia.getPath());
        }
        Intent intent = new Intent(context, PreviewUrlActivity.class);
        intent.putStringArrayListExtra("data", (ArrayList<String>) list);
        intent.putExtra("position", position + 1);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void goPreviewUrlActivity(Activity context, List<String> list, int mPosition) {
        Intent intent = new Intent(context, PreviewUrlActivity.class);
        intent.putStringArrayListExtra("data", (ArrayList<String>) list);
        intent.putExtra("position", mPosition + 1);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void goPreviewUrlActivity(Activity context, String url) {
        Intent intent = new Intent(context, PreviewUrlActivity.class);
        List<String> list = new ArrayList<>();
        list.add(url);
        intent.putStringArrayListExtra("data", (ArrayList<String>) list);
        intent.putExtra("position", 1);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview);
        mTvTttle = findViewById(R.id.album_title);
        findViewById(R.id.ll_complete).setVisibility(View.GONE);
        findViewById(R.id.ll_check).setVisibility(View.GONE);
        mViewPager = findViewById(R.id.vp_preview);
        mViewPager.setOffscreenPageLimit(3);
        findViewById(R.id.album_back).setOnClickListener(this);

        mLocalMediaList = getIntent().getStringArrayListExtra("data");
        mTvTttle.setText(getIntent().getIntExtra("position", 1) + "/" + mLocalMediaList.size());


        mFragmentAdapte = new FragmentAdapte(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapte);

        mViewPager.setCurrentItem(getIntent().getIntExtra(FunctionConfig.FOLDER_DETAIL_POSITION, 1) - 1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvTttle.setText(position + 1 + "/" + mLocalMediaList.size());
                mPosition = position;

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_back) {
            finish();
        }
    }


    public class FragmentAdapte extends FragmentPagerAdapter {

        SparseArray<Fragment> mSparseArray = new SparseArray<>();

        public FragmentAdapte(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            Fragment fragment = mSparseArray.get(position);
            if (fragment == null) {
                fragment = AlbumPreviewFragment.getInstance(mLocalMediaList.get(position), true);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mLocalMediaList.size();
        }
    }


}
