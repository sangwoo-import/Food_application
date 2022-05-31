package com.example.mymanager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;

public class ServerActivity2 extends AppCompatActivity {

    String filePath = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadImage();
    }
    private void uploadImage() {
        File file = new File(filePath);

//        Retrofit retrofit = NetworkClient.getRetrofit(); //  원래 file 없음
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
//
//        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");
//
//        UploadApis uploadApis = retrofit.create(UploadApis.class);
//        Call call = uploadApis.uploadImage(parts, someData);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
    }
}
