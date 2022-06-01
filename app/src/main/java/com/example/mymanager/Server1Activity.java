package com.example.mymanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.UUID;

public class Server1Activity extends AppCompatActivity {

    ImageView imgVwSelected;
    Button btnImageSend, btnImageSelection;

    private static final String UPLOAD_URL="https://ryo-cnnapi.run.goorm.io/imgfile";
    private static final int STORAGE_PERMISSON_CODE=4655;
    private int PICK_IMAGE_RESULT=1;
    private Uri filepath;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server1);
        storagePermission();

        imgVwSelected=(ImageView)findViewById(R.id.imgVwSelected);

    }

    private void storagePermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSON_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==STORAGE_PERMISSON_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void selectImage(View view)
    {
        showFileChooser();
    }
    private void showFileChooser()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selcet Image"),PICK_IMAGE_RESULT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_RESULT && data != null && data.getData()!=null){
            filepath=data.getData();
            try{

                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);

                imgVwSelected.setImageBitmap(bitmap);

            }
            catch (Exception ex)
            {

            }
        }
    }

    private String getPath(Uri uri){
        Cursor cursor =getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Images.Media._ID+"=?",null,null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path= cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
public void sendImg(View view){
        uploadImage();
}
private void uploadImage()
{

         String path=getPath(filepath);

        try{
//            String uploadId= UUID.randomUUID().toString();
//            new MultipartUploadRequest(this,UPLOAD_URL)
//                    .addFileToUpload(path,"image")
//                    //.setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(3)
//                    .startUpload();
        }
        catch (Exception ex){

        }
}

}
