package com.hqq.ihalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.ihalbum
 * @FileName :   Main2Activity
 * @Date : 2020/6/29 0029  下午 4:58
 * @Email : qiqiang213@gmail.com
 * @Descrive :
 */
public class Main2Activity extends MainActivity {
    public static void open(Activity context) {
        Intent starter = new Intent(context, Main2Activity.class);
        context.startActivityForResult(starter, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            @Override
            public void run() {
                //执行异步处理
                SystemClock.sleep(240000);
            }
        }.start();
    }
}
