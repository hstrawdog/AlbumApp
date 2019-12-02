package com.hqq.album.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.MediaController;
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
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.dialog.OptAnimationLoader;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.AlbumUtils;
import com.hqq.album.weight.FilterImageView;

import java.util.List;

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
    List<LocalMedia> mLocalMediaList;
    int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview_v2);
        initView();

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
        } else if (i == R.id.album_finish) {
            OnSelectResultCallback resultCallback = PictureConfig.getInstance().getResultCallback();
            if (resultCallback != null) {
                resultCallback.onSelectSuccess(PictureConfig.getInstance().getSelectMedia());
            }
            setResult(Activity.RESULT_OK);
            AppManager.getAppManager().finishAllActivity();
            PictureConfig.getInstance().setResultCallback(null);

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
                    mTvCheck.startAnimation(OptAnimationLoader.loadAnimation(mContext, R.anim.modal_in));
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.picture_message_max_num, PictureConfig.getInstance().getBuilder().getMaxSelectNum() + ""), Toast.LENGTH_LONG).show();
                }
            }
            updateSelectMenu();

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

        //  RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mAlbumTitleBar.getLayoutParams();
        //    layoutParams.setMargins(0, AlbumUtils.getStatusBarHeight(this), 0, 0);
        //   mAlbumTitleBar.setLayoutParams(layoutParams);

        mAlbumBack.setOnClickListener(this);
        mLlCheck.setOnClickListener(this);
        mAlbumFinish.setOnClickListener(this);

        mLocalMediaList = PictureConfig.getInstance().getSelectLocalMedia();
        mAlbumTitle.setText(getIntent().getIntExtra(FunctionKey.KEY_POSITION, 1) + "/" + mLocalMediaList.size());

    }

    private void updateSelectMenu() {
        if ((PictureConfig.getInstance().getSelectMedia().size() > 0)) {
            mAlbumFinish.setVisibility(View.VISIBLE);
            mAlbumFinish.setText("完成(" + PictureConfig.getInstance().getSelectMedia().size() + "/" + PictureConfig.getInstance().getBuilder().getMaxSelectNum() + ")");
        } else {
            mAlbumFinish.setVisibility(View.GONE);
        }
        mAlbumTitle.setText(mPosition + 1 + "/" + mLocalMediaList.size());
        mTvCheck.setSelected(isSelected(mLocalMediaList.get(mPosition)));

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


    class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_album_preview, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            initData(AlbumPreviewV2Activity.this, viewHolder, mLocalMediaList.get(i));
        }

        @Override
        public int getItemCount() {
            return mLocalMediaList.size();
        }


        private void initData(final Context context, final ViewHolder viewHolder, LocalMedia localMedia) {


            viewHolder.progressBar.setVisibility(View.VISIBLE);
            switch (localMedia.getLocalMediaType()) {
                case FunctionKey.VALUE_TYPE_IMAGE:
                    Glide.with(context)
                            .load(localMedia.getPath())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
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
                    break;
                case FunctionKey.VALUE_TYPE_VIDEO:
                    viewHolder.videoView.setMediaController(new MediaController(context));
                    viewHolder.videoView.setVideoURI(Uri.parse(localMedia.getPath()));
                    viewHolder.videoView.start();
                    viewHolder.videoView.requestFocus();
                    viewHolder.imageView.setVisibility(View.GONE);
                    viewHolder.progressBar.setVisibility(View.GONE);

                    break;
                default:

            }
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
