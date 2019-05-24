package com.hqq.album.Adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import com.hqq.album.R;
import com.hqq.album.common.FunctionConfig;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.dialog.OptAnimationLoader;
import com.hqq.album.entity.LocalMedia;
import com.hqq.album.utils.Utils;

/**
 * Created by huangqiqiang on 17/5/7.
 */

public class AlbumDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LocalMedia> images = new ArrayList<>();
    private List<LocalMedia> selectImages = new ArrayList<>();
    private Context context;
    private int maxSelectNum;
    private OnPhotoSelectChangedListener imageSelectChangedListener;

    public void bindImagesData(List<LocalMedia> images) {
        this.images = images;
        selectImages = PictureConfig.getInstance().getSelectMedia();
        notifyDataSetChanged();
    }

    public AlbumDetailAdapter(Context context, int maxSelectNum) {
        this.context = context;
        this.maxSelectNum = maxSelectNum;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_image_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder contentHolder = (ViewHolder) holder;
        final LocalMedia image = images.get(position);
        image.position = contentHolder.getAdapterPosition();
        selectImage(contentHolder, isSelected(image), false);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop();
        if (image.getType() == FunctionConfig.TYPE_IMAGE) {
            Glide.with(holder.itemView.getContext())
                    .load(image.getPath())
                    .apply(options)
                    // .override(150, 150)
                    .into(contentHolder.picture);
        } else if (image.getType() == FunctionConfig.TYPE_VIDEO) {
            Glide.with(holder.itemView.getContext()).load(image.getPath()).thumbnail(0.5f).into(contentHolder.picture);
        }

        if (contentHolder.mRlDuration.getVisibility() == View.VISIBLE) {
            contentHolder.mRlDuration.setVisibility(View.GONE);
        }
        contentHolder.mLlCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isFastDoubleClick(200)) {
                    changeCheckboxState(contentHolder, image);
                }
            }
        });
        contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageSelectChangedListener != null) {
                    imageSelectChangedListener.onPictureClick(image, position);
                } else {
                    if (!Utils.isFastDoubleClick()) {
                        changeCheckboxState(contentHolder, image);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView check;
        TextView mTvDuration;
        View contentView;
        LinearLayout mLlCheck;
        RelativeLayout mRlDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.picture);
            check = (TextView) itemView.findViewById(R.id.check);
            mLlCheck = (LinearLayout) itemView.findViewById(R.id.ll_check);
            mTvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            mRlDuration = (RelativeLayout) itemView.findViewById(R.id.rl_duration);
        }
    }

    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    public void selectImage(ViewHolder holder, boolean isChecked, boolean isAnim) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            if (isAnim) {
                Animation animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
                holder.check.startAnimation(animation);
            }
            holder.picture.setColorFilter(ContextCompat.getColor(context, R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.picture.setColorFilter(ContextCompat.getColor(context, R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
    }


    /**
     * 改变图片选中状态
     *
     * @param contentHolder
     * @param image
     */

    private void changeCheckboxState(ViewHolder contentHolder, LocalMedia image) {
        boolean isChecked = contentHolder.check.isSelected();

        if (selectImages.size() >= maxSelectNum && !isChecked) {
            Toast.makeText(context, context.getString(R.string.picture_message_max_num, maxSelectNum + ""), Toast.LENGTH_LONG).show();
            return;
        }
        if (isChecked) {
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    subSelectPosition();
                    break;
                }
            }
        } else {
            selectImages.add(image);
            image.setNum(selectImages.size());
        }
        //通知点击项发生了改变
        //notifyItemChanged(contentHolder.getAdapterPosition());
        selectImage(contentHolder, !isChecked, true);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        // if (is_checked_num) {
        for (int index = 0, len = selectImages.size(); index < len; index++) {
            LocalMedia media = selectImages.get(index);
            media.setNum(index + 1);
            //     notifyItemChanged(media.position);
            //   }
        }
    }

    public interface OnPhotoSelectChangedListener {
        void onTakePhoto();

        void onChange(List<LocalMedia> selectImages);

        void onPictureClick(LocalMedia media, int position);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }
}
