package com.example.mymanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
//public class NetworkClient {
//
//
//
//    private static Retrofit retrofit;
//    private static String BASE_URL = "https://ryo-cnnapi.run.goorm.io/html/imgfile/";
//
//    public static Retrofit getRetrofit() { // 원래는 File file 없음
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
//                    addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
//        }
//        return retrofit;
//    }
//
//}
public class NetworkClient {
    private static final String BASE_URL = "https://ryo-cnnapi.run.goorm.io/html/imgfile/";
    private static Retrofit retrofit;

    public static Retrofit  getRetrofit()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

}