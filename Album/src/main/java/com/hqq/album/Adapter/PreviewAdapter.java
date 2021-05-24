package com.hqq.album.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hqq.album.R;
import com.hqq.album.annotation.LocalMediaType;
import com.hqq.album.entity.LocalMedia;

import java.util.List;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.album.Adapter
 * @FileName :   PreviewAdapter
 * @Date : 2020/1/16 0016  上午 11:42
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    Context mContext;
    List<LocalMedia> mLocalMediaList;


    public PreviewAdapter(Context context, List<LocalMedia> localMediaList) {
        mContext = context;
        mLocalMediaList = localMediaList;
    }

    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_album_preview, viewGroup, false);
        return new PreviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder viewHolder, int i) {
        initData(mContext, viewHolder, mLocalMediaList.get(i));
    }

    @Override
    public int getItemCount() {
        return mLocalMediaList.size();
    }


    private void initData(final Context context, final PreviewAdapter.ViewHolder viewHolder, LocalMedia localMedia) {
        switch (localMedia.getLocalMediaType()) {
            case LocalMediaType.VALUE_TYPE_IMAGE:
                viewHolder.photoDraweeView.setImage(ImageSource.uri(localMedia.getUri()));
                break;
            case LocalMediaType.VALUE_TYPE_VIDEO:
                viewHolder.videoView.setMediaController(new MediaController(context));
                viewHolder.videoView.setVideoURI(localMedia.getUri());
                viewHolder.videoView.start();
                viewHolder.videoView.requestFocus();
                viewHolder.videoView.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setVisibility(View.GONE);

                break;
            default:

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;
        ProgressBar progressBar;
        SubsamplingScaleImageView photoDraweeView;
        public ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.vv_view);
            progressBar = itemView.findViewById(R.id.pb_bar);
            photoDraweeView = itemView.findViewById(R.id.image_item);

        }
    }


}