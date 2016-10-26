package com.example.wkw.okhttp_project;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by wkw on 2016/10/25.
 * 封装工具类
 */

public class OKHttpManager {
    private OkHttpClient mClient;
    private volatile static OKHttpManager okHttpManager;
    private final String TAG = OKHttpManager.class.getSimpleName();
    private Handler handler;
    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private OKHttpManager(){
        mClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }
    //采用单例模式获取对象
    public static OKHttpManager getInstance(){
        OKHttpManager instance = null;
        if (okHttpManager == null){
            synchronized (OKHttpManager.class){
                if (instance == null){
                    instance = new OKHttpManager();
                    okHttpManager = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 请求返回的结果是json字符串
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final Fun1 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    try {
                        callBack.onResponse(jsonValue);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onSuccessJsonObjectMethod(final String jsonValue, final Fun4 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null){
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onSuccessByteMethod(final byte[] data, final Fun2 callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null){
                    try {
                        callBack.onResponse(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    interface Fun1{
        void onResponse(String result);
    }

    interface Fun2{
        void onResponse(byte[] result);
    }

    interface Fun3{
        void onResponse(Bitmap bitmap);
    }

    interface Fun4{
        void onResponse(JSONObject jsonObject);
    }

}
