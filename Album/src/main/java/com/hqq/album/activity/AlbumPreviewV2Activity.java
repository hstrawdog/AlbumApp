package com.hqq.album.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hqq.album.Adapter.PreviewAdapter;
import com.hqq.album.AppManager;
import com.hqq.album.R;
import com.hqq.album.activity.base.BaseActivity;
import com.hqq.album.common.FunctionKey;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.dialog.OptAnimationLoader;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.customize.FilterImageView;

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
    PreviewAdapter mPreviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_preview_v2);
        initView();
        mRcAlbumList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(mRcAlbumList);
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
        mPreviewAdapter = new PreviewAdapter(this, mLocalMediaList);
        mRcAlbumList.setAdapter(mPreviewAdapter);

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


}
