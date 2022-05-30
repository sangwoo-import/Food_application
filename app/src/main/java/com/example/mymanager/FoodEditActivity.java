package com.example.mymanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class FoodEditActivity extends AppCompatActivity {
    private  ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food); // 원래 food_edit임


        Intent intent = getIntent();



        if(intent.getAction().equals("kr.co.hta.MyAction")){
            //호출한 인텐트가 보내온 이미지와 메시지 얻어오기
            Bitmap bitmap = (Bitmap)intent.getExtras().get("img");




          ImageView img = (ImageView)findViewById(R.id.imageView2);
            img.setImageBitmap(bitmap);
        }

    }




}
