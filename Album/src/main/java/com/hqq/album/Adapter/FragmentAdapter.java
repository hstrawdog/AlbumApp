package com.hqq.album.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hqq.album.activity.AlbumPreviewFragment;
import com.hqq.album.entity.LocalMedia;

import java.util.List;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.album.Adapter
 * @FileName :   FragmentAdapter
 * @Date : 2019/5/28 0028  上午 10:08
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class FragmentAdapter<T> extends FragmentPagerAdapter {
    List<T> mLocalMediaList;

    public FragmentAdapter(FragmentManager fm, List<T> localMediaList) {
        super(fm);
        mLocalMediaList = localMediaList;
    }

    @Override
    public Fragment getItem(int position) {
        AlbumPreviewFragment fragment;
        if (mLocalMediaList.get(position) instanceof String) {
            fragment = AlbumPreviewFragment.getInstance((String) mLocalMediaList.get(position));
        } else {
            fragment = AlbumPreviewFragment.getInstance((LocalMedia) mLocalMediaList.get(position));
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mLocalMediaList.size();
    }


}
