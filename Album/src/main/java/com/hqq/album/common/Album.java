package com.hqq.album.common;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.hqq.album.activity.AlbumDirectoryActivity;

import java.lang.ref.WeakReference;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.album.common
 * @FileName :   Album
 * @Date : 2020/6/30 0030  上午 10:13
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class Album {
    /**
     *
     */
    private final WeakReference<Activity> mContext;
    /**
     *
     */
    private final WeakReference<Fragment> mFragment;

    public Album(Activity activity) {
        this(activity, null);
    }

    public Album(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    public Album(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static Album from(Activity activity) {
        return new Album(activity);
    }


    public FunctionOptions choose(int valueTypeImage) {
        return FunctionOptions.getInstance()
                .setAlbum(this)
                .setAlbumType(valueTypeImage);
    }

    public Activity getActivity() {
        return mContext.get();
    }

    public Fragment getFragment() {
        if (mFragment != null) {
            return mFragment.get();
        }
        return null;
    }
}
