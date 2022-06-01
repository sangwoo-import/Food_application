package com.example.mymanager;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FileUploadUtils {


    //String path = "";
//    public static void send2Server(File file) {
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", file.getName(), RequestBody.create(MultipartBody.FORM, file))
//                .build();
//        Request request = new Request.Builder()
//                .url("https://ryo-cnnapi.run.goorm.io/imgfile") // Server URL 은 본인 IP를 입력
//                .post(requestBody)
//                .build();
//
//        OkHttpClient client = new OkHttpClient();
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("TEST : ", response.body().string());
//            }
//        });
//    }

    public static void send2Server(File file) {
//        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .build();
        Request request = new Request.Builder()
                .url("https://ryo-cnnapi.run.goorm.io/imgfile") // Server URL 은 본인 IP를 입력
                .post(requestBody)
                .build();

           OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    Log.d("test: " , response.body().string());
                }
            }
        });
    }
}
