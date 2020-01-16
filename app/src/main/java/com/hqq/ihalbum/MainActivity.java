package com.hqq.ihalbum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


import com.hqq.album.activity.PreviewUrlActivity;
import com.hqq.album.common.FunctionOptions;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.common.PictureConfig;
import com.hqq.album.dialog.PhotoDialog;
import com.hqq.album.entity.LocalMedia;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.hqq.album.annotation.LocalMediaType.VALUE_TYPE_VIDEO;

/**
  * @Author : huangqiqiang
  * @Package : com.hqq.ihalbum
  * @FileName :   MainActivity
  * @Date  : 2019/9/16 0016  下午 8:33
  * @Email :  qiqiang213@gmail.com
  * @Descrive :
  */
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

                PreviewUrlActivity.goPreviewUrlActivity(MainActivity.this, list, 0);


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
//                builder.setAlbumType(VALUE_TYPE_VIDEO);
                builder.setMaxSelectNum(10).setStartAlbum();
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

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSSLHandshake();
//                String gif = "https://images.shangwenwan.com/social/1d4mde3s3igo0000?imageMogr2/size-limit/681.2k!/thumbnail/300.0x300.0";
//                String gif = "http://img.soogif.com/VVbrYJ6dFaV3Ps3ZwVZFhUxC021s7uO1.gif";
                String gif = "https://images.shangwenwan.com/mall/6d392bfd-6273-4992-a24d-74f4b39b19d3?imageMogr2/size-limit/54.7k!/crop/!485x485a6a8";
                PreviewUrlActivity.goPreviewUrlActivity(MainActivity.this, gif);

            }
        });
    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


}
