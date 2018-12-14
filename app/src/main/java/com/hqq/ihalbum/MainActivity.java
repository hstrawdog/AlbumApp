package com.hqq.ihalbum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.hqq.album.common.FunctionOptions;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.dialog.PhotoDialog;
import com.hqq.album.entity.LocalMedia;

import static com.hqq.album.activity.PreviewUrlActivity.goPreviewUrlActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> list = new ArrayList<>();

                list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
                list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
                list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
                list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");

                goPreviewUrlActivity(MainActivity.this, list, 0);


            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDialog.getPhotoSelectDialog(1, new OnSelectResultCallback() {
                    @Override
                    public void onSelectSuccess(List<LocalMedia> resultList) {
                        if (resultList.size() > 0) {
                            Toast.makeText(MainActivity.this, resultList.get(0).getPath(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show(getSupportFragmentManager());
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FunctionOptions.Builder builder = new FunctionOptions.Builder();
                builder.setMaxSelectNum(1).setStartCamera();
                PictureConfig.getInstance().openPhoto(MainActivity.this, builder, new OnSelectResultCallback() {
                    @Override
                    public void onSelectSuccess(List<LocalMedia> resultList) {
                        if (resultList.size() > 0) {
                            Toast.makeText(MainActivity.this, resultList.get(0).getPath(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FunctionOptions.Builder builder = new FunctionOptions.Builder();
                builder.setMaxSelectNum(1).setStartAlbum();
                PictureConfig.getInstance().openPhoto(MainActivity.this, builder, new OnSelectResultCallback() {
                    @Override
                    public void onSelectSuccess(List<LocalMedia> resultList) {
                        if (resultList.size() > 0) {
                            Toast.makeText(MainActivity.this, resultList.get(0).getPath(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
