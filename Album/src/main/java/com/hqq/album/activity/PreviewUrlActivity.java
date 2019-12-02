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
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hqq.album.AppManager;
import com.hqq.album.R;
import com.hqq.album.activity.base.BaseActivity;
import com.hqq.album.common.FunctionKey;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.AlbumUtils;
import com.hqq.album.weight.FilterImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : huangqiqiang
 * @Package : cn.hqq.halbum.activity
 * @FileName :   PreviewUrlActivity
 * @Date : 2018/9/3 0003
 * @Descrive :
 * @Email :
 */
public class PreviewUrlActivity extends BaseActivity implements View.OnClickListener {
    List<String> mLocalMediaList;
    private RecyclerView mRcAlbumList;
    private ConstraintLayout mAlbumTitleBar;
    private FilterImageView mAlbumBack;
    private TextView mAlbumTitle;
    private LinearLayout mLlCheck;
    private ImageView mTvCheck;
    private LinearLayout mLlComplete;
    private TextView mAlbumFinish;
    int mPosition = 0;


    public static void goPreviewUrlActivity2LocalMedia(Activity context, List<LocalMedia> data, int position) {

        List<String> list = new ArrayList<>();
        for (LocalMedia localMedia : data) {
            list.add(localMedia.getPath());
        }
        Intent intent = new Intent(context, PreviewUrlActivity.class);
        intent.putStringArrayListExtra(FunctionKey.KEY_DATA, (ArrayList<String>) list);
        intent.putExtra(FunctionKey.KEY_POSITION, position + 1);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void goPreviewUrlActivity(Activity context, List<String> list, int mPosition) {
        Intent intent = new Intent(context, PreviewUrlActivity.class);
        intent.putStringArrayListExtra(FunctionKey.KEY_DATA, (ArrayList<String>) list);
        intent.putExtra(FunctionKey.KEY_POSITION, mPosition + 1);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void goPreviewUrlActivity(Activity context, String url) {
        Intent intent = new Intent(context, PreviewUrlActivity.class);
        List<String> list = new ArrayList<>();
        list.add(url);
        intent.putStringArrayListExtra(FunctionKey.KEY_DATA, (ArrayList<String>) list);
        intent.putExtra(FunctionKey.KEY_POSITION, 1);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview_v2);
        mLocalMediaList = getIntent().getStringArrayListExtra(FunctionKey.KEY_DATA);
        initView();
        mLlCheck.setVisibility(View.GONE);
        PreviewAdapter previewAdapter = new PreviewAdapter();
        mRcAlbumList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(mRcAlbumList);
        mRcAlbumList.setAdapter(previewAdapter);
        mRcAlbumList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (first == last && mPosition != last) {
                        mPosition = last;
                        updateSelectMenu();
                    }
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mPosition = getIntent().getIntExtra(FunctionKey.KEY_POSITION, 1) - 1;
        mRcAlbumList.scrollToPosition(mPosition);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_back) {
            AppManager.getAppManager().finishActivity();
        }
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

//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mAlbumTitleBar.getLayoutParams();
//        layoutParams.setMargins(0, AlbumUtils.getStatusBarHeight(this), 0, 0);
//        mAlbumTitleBar.setLayoutParams(layoutParams);

        mAlbumBack.setOnClickListener(this);
        mLlCheck.setOnClickListener(this);
        mAlbumFinish.setOnClickListener(this);

        mAlbumTitle.setText(getIntent().getIntExtra(FunctionKey.KEY_POSITION, 1) + "/" + mLocalMediaList.size());

    }

    private void updateSelectMenu() {

        mAlbumTitle.setText(mPosition + 1 + "/" + mLocalMediaList.size());

    }


    class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

        @NonNull
        @Override
        public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_album_preview, viewGroup, false);
            return new PreviewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder viewHolder, int i) {
            initData(PreviewUrlActivity.this, viewHolder, mLocalMediaList.get(i));
        }

        @Override
        public int getItemCount() {
            return mLocalMediaList.size();
        }


        private void initData(final Context context, final PreviewAdapter.ViewHolder viewHolder, String localMedia) {
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(localMedia)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(context, "图片预览失败", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(viewHolder.imageView);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            VideoView videoView;
            ImageView imageView;
            ProgressBar progressBar;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.preview_image);
                videoView = itemView.findViewById(R.id.vv_view);
                progressBar = itemView.findViewById(R.id.pb_bar);

            }
        }


    }


}
