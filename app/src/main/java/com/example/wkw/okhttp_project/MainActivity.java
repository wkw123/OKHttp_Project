package com.example.wkw.okhttp_project;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button mButton;

    private ImageView mImageView;

    private final static int SUCCESS_STATUS = 1;
    private final static int FAIL_STATUS = 0;
    private final static String TAG = MainActivity.class.getSimpleName();
    private String image_path = "https://www.baidu.com/img/bd_logo1.png";

    private OkHttpClient mClient;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS_STATUS:
                    byte[] result = (byte[]) msg.obj;
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                    Bitmap bitmap = new CropSquareTrans().transform(BitmapFactory.decodeByteArray(result, 0, result.length));
                    mImageView.setImageBitmap(bitmap);
                    break;
                case FAIL_STATUS:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mClient = new OkHttpClient();
        final Request request = new Request.Builder().get().url(image_path).build();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message message = handler.obtainMessage();
                        if(response.isSuccessful()){
                            message.what = SUCCESS_STATUS;
                            message.obj = response.body().bytes();
                            handler.sendMessage(message);
                        }else {
                            handler.sendEmptyMessage(FAIL_STATUS);
                        }
                    }
                });
            }
        });
    }
}
