package com.hqq.album.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.hqq.album.R;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.entity.LocalMedia;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: cn.huangqiqiang.halbum.activity.AlbumPreviewFragment.java
 * @author: 黄其强
 * @date: 2017-05-08 17:56
 */
public class AlbumPreviewFragment extends Fragment {
    public static final String PATH = "path";
    public static final String Type = "type";
    private static String clickType = "clickType";

    public static AlbumPreviewFragment getInstance(LocalMedia path) {
        AlbumPreviewFragment fragment = new AlbumPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Type, path.getType());
        bundle.putString(PATH, path.getPath());
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AlbumPreviewFragment getInstance(String path) {
        AlbumPreviewFragment fragment = new AlbumPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Type, FunctionConfig.TYPE_IMAGE);
        bundle.putString(PATH, path);
        bundle.putBoolean(clickType, false);

        fragment.setArguments(bundle);
        return fragment;
    }

    public static AlbumPreviewFragment getInstance(String path, boolean type) {
        AlbumPreviewFragment fragment = new AlbumPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Type, FunctionConfig.TYPE_IMAGE);
        bundle.putBoolean(clickType, type);
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    PhotoView imageView;
    int localMedia;
    VideoView videoView;
    View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        // if (mRootView == null && (vid != 0 || getIRootView() != null || rootView != null)) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_album_preview, container, false);
        }


        return mRootView;
    }

    /**
     * fragment 是否创建
     */
    public boolean mIsCreate = false;
    /**
     * 延迟加载是否结束
     */
    boolean mLazyInitEnd = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mIsCreate && mRootView != null) {
            mIsCreate = true;
            if (getUserVisibleHint()) {
                mLazyInitEnd = true;
                initData();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mIsCreate && !mLazyInitEnd && isVisibleToUser) {
            mLazyInitEnd = true;
            initData();
        } else if (mIsCreate && mLazyInitEnd && !isVisibleToUser) {
            System.gc();
        }
    }


    private void initData() {

        imageView = mRootView.findViewById(R.id.preview_image);
        localMedia = getArguments().getInt(Type, FunctionConfig.TYPE_IMAGE);
        videoView = mRootView.findViewById(R.id.vv_view);
        boolean type = getArguments().getBoolean(clickType);
        if (type) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

        }

        String path = getArguments().getString(PATH);
        final ProgressBar progressBar = mRootView.findViewById(R.id.pb_bar);
        progressBar.setVisibility(View.VISIBLE);
        switch (localMedia) {
            case FunctionConfig.TYPE_IMAGE:
                imageView.setMaxScale(6);
                imageView.enable();
                Glide.with(getContext()).asBitmap()
                        .load(path)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                                progressBar.setVisibility(View.GONE);
                                imageView.setImageBitmap(resource);
                            }
                        });
                break;
            case FunctionConfig.TYPE_VIDEO:
                videoView.setMediaController(new MediaController(getContext()));
                videoView.setVideoURI(Uri.parse(path));
                videoView.start();
                videoView.requestFocus();
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                break;
            default:

        }
    }


}
