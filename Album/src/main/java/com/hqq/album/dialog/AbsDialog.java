package com.hqq.album.dialog;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hqq.album.R;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.blibrary
 * @FileName :   AbsDialog
 * @Date : 2018/6/19 0019  上午 11:33
 * @Descrive : TODO
 * @Email :
 */
public abstract class AbsDialog extends DialogFragment {

    boolean mLoaded = false;
   protected AbsDialogClickListener mAbsDialogClickListener;

    public void setAbsDialogClickListener(AbsDialogClickListener absDialogClickListener) {
        mAbsDialogClickListener = absDialogClickListener;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    protected View mRootView = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(setAnimation());
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        if (mRootView == null) {
            mRootView = inflater.inflate(setView(), container,false);
        }

        return mRootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mLoaded && mRootView != null) {
            mLoaded = true;
            initView();
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getBackground()));
        getDialog().getWindow().setLayout(setWeight(), setHeight());
        getDialog().getWindow().setGravity(setGravity());
    }


    public void show(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        Fragment prev = manager.findFragmentByTag(getClass().getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        super.show(manager, getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
  //      unbinder.unbind();
    //    mLoadingView = null;
    }

    /**
     * 关联主界面
     */
    public abstract int setView();

    /**
     * 初始化
     */
    protected abstract void initView();

    /**
     * @return 背景颜色
     */
    public int getBackground() {
        return 0x00000000;
    }

    public int setGravity() {
        return Gravity.CENTER;
    }

    protected int setWeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int setHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int setAnimation() {
        return R.style.DialogAnimation_bottom2top;
    }


}
