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
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.hqq.album.Adapter.FragmentAdapter;
import com.hqq.album.R;
import com.hqq.album.activity.base.BaseActivity;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.AlbumUtils;

/**
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.activity
 * @FileName :   PreviewUrlActivity
 * @Date : 2018/9/3 0003
 * @Descrive :
 * @Email :
 */
public class PreviewUrlActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ViewPager mViewPager;
    TextView mTvCheck;
    TextView mTvTttle;
    int mPosition = 0;
    List<String> mLocalMediaList;
    FragmentAdapter mFragmentAdapte;
    ConstraintLayout mRelativeLayout;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullWindow();
        setContentView(R.layout.activity_album_preview);
        mRelativeLayout = findViewById(R.id.album_title_bar);
        findViewById(R.id.ll_complete).setVisibility(View.GONE);
        findViewById(R.id.ll_check).setVisibility(View.GONE);
        mTvTttle = findViewById(R.id.album_title);
        mViewPager = findViewById(R.id.vp_preview);
        mViewPager.setOffscreenPageLimit(3);
        findViewById(R.id.album_back).setOnClickListener(this);
        mViewPager.setCurrentItem(getIntent().getIntExtra(FunctionConfig.FOLDER_DETAIL_POSITION, 1) - 1);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOnClickListener(this);

        mLocalMediaList = getIntent().getStringArrayListExtra("data");
        mFragmentAdapte = new FragmentAdapter(getSupportFragmentManager(),mLocalMediaList);
        mViewPager.setAdapter(mFragmentAdapte);
        mTvTttle.setText(getIntent().getIntExtra("position", 1) + "/" + mLocalMediaList.size());

        mRelativeLayout.setPadding(0, AlbumUtils.getStatusBarHeight(this), 0, 0);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_back) {
            finish();
        } else if (i == R.id.vp_preview) {
            finish();
        }
    }

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





}
