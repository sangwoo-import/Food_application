package com.example.mymanager;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodAddActivity  extends AppCompatActivity {



    private static final String TAG = "testactivity"; //  이거안되면 CameraActivity
    //public static final String KEY="photo";

    public static final int REQUEST_TAKE_PHOTO = 10;
    public static final int REQUEST_PERMISSION = 11;
    private static final int FROM_ALBUM = 1;

    private Button btnCamera,btn_gallery, btnSave;
    private ImageView imageView2;
    private String mCurrentPhotoPath;
    private TextView tv_output1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_edit);


         imageView2 =findViewById(R.id.imageView2);
       // ivCapture = findViewById(R.id.ivCapture); //ImageView 선언
        btnCamera = findViewById(R.id.btnCapture); //Button 선언
        btnSave = findViewById(R.id.btnSave); //Button 선언
       //tv_output = findViewById(R.id.tv_output);
        tv_output1 = findViewById(R.id.tv_output1);

        btn_gallery= findViewById(R.id.btn_gallery);


        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setType("image/*");                      // 이미지만
                intent.setAction(Intent.ACTION_GET_CONTENT);    // 카메라(ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, FROM_ALBUM);

            }
        });








        checkPermission(); //권한체크


        //loadImgArr();

        //촬영
        btnCamera.setOnClickListener(v -> captureCamera());

        //저장
        btnSave.setOnClickListener(v -> {

            try {

                BitmapDrawable drawable = (BitmapDrawable) imageView2.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                //찍은 사진이 없으면
                if (bitmap == null) {
                    Toast.makeText(this, "저장할 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    //저장
                    saveImg();
                    mCurrentPhotoPath = ""; //initialize
                }

            } catch (Exception e) {
                Log.w(TAG, "SAVE ERROR!", e);
            }
        });
    }




    private void captureCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 인텐트를 처리 할 카메라 액티비티가 있는지 확인
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // 촬영한 사진을 저장할 파일 생성
            File photoFile = null;

            try {
                //임시로 사용할 파일이므로 경로는 캐시폴더로
                File tempDir = getCacheDir();

                //임시촬영파일 세팅
                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String imageFileName = "Capture_" + timeStamp + "_"; //ex) Capture_20201206_

                File tempImage = File.createTempFile(
                        imageFileName,  /* 파일이름 */
                        ".jpg",         /* 파일형식 */
                        tempDir      /* 경로 */
                );

                // ACTION_VIEW 인텐트를 사용할 경로 (임시파일의 경로)
                mCurrentPhotoPath = tempImage.getAbsolutePath();

                photoFile = tempImage;

            } catch (IOException e) {
                //에러 로그는 이렇게 관리하는 편이 좋다.
                Log.w(TAG, "파일 생성 에러!", e);
            }

            //파일이 정상적으로 생성되었다면 계속 진행
            if (photoFile != null) {
                //Uri 가져오기
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);
                //인텐트에 Uri담기
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                //인텐트 실행
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //이미지저장 메소드
    private void saveImg() {

        try {
            //저장할 파일 경로
            File storageDir = new File(getFilesDir() + "/capture");
            if (!storageDir.exists()) //폴더가 없으면 생성.
                storageDir.mkdirs();

            String filename = "캡쳐파일" + ".jpg";

            // 기존에 있다면 삭제
            File file = new File(storageDir, filename);
            boolean deleted = file.delete();
            Log.w(TAG, "Delete Dup Check : " + deleted);
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(file);
                BitmapDrawable drawable = (BitmapDrawable) imageView2.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output); //해상도에 맞추어 Compress
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert output != null;
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e(TAG, "Captured Saved");
            Toast.makeText(this, "Capture Saved ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.w(TAG, "Capture Saving Error!", e);
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();

        }
    }

//    private void loadImgArr() {
//        try {
//
//            File storageDir = new File(getFilesDir() + "/capture");
//            String filename = "캡쳐파일" + ".jpg";
//
//            File file = new File(storageDir, filename);
//            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
//            ivCapture.setImageBitmap(bitmap);
//
//        } catch (Exception e) {
//            Log.w(TAG, "Capture loading Error!", e);
//            Toast.makeText(this, "load failed", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);






//
//        float[][][][] input = new float[1][224][224][3];
//        float[][][] output = new float[1][12543][4];




        try { //촬영 이미지
            //after capture
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {

                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));

                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);


//                            int batchNum = 0;
//                            for (int x = 0; x < 224; x++) {
//                                for (int y = 0; y < 224; y++) {
//                                    int pixel = bitmap.getPixel(x, y);
//                                    input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
//                                    input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
//                                    input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
//                                }
//                            }
//
//                            Interpreter lite = getTfliteInterpreter("output226.tflite");
//
//                            lite.run(input,output);


//                            //사진해상도가 너무 높으면 비트맵으로 로딩
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 8; //8분의 1크기로 비트맵 객체 생성
//                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                            Bitmap rotatedBitmap = null;
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }

                            //Rotate한 bitmap을 ImageView에 저장
                            imageView2.setImageBitmap(rotatedBitmap);

                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Log.w(TAG, "onActivityResult Error !", e);
        }


       // int j;

//        for (j = 0; j <4; j++) {
//            if (output[0][0][j] * 100 > 30) {
//                if (j == 0) {
//                    tv_output.setText(String.format("bg  %d %.5f", j, output[0][0][0] * 100));
//                } else if (j == 1) {
//                    tv_output.setText(String.format("bab  %d  %.5f", j, output[0][0][1] * 100));
//                } else if (j == 2) {
//                    tv_output.setText(String.format("kim %d, %.5f", j, output[0][0][2] * 100));
//                }
//                else if (j == 3) {
//                    tv_output.setText(String.format("nothing, %d, %.5f", j, output[0][0][3] * 100));
//                }
//                else {
//                    tv_output.setText(String.format("아무 음식, %d, %.5f", j));
//                    //output[0][0][4] * 100)
//                }
//            } else
//                continue;
//        }






        if (requestCode != FROM_ALBUM || resultCode != RESULT_OK)// 갤러리 사용
        return ;

        try {  // 갤러리 사용

            int batchNum = 0;
            InputStream buf = getContentResolver().openInputStream(intent.getData()); // intent대신 data였음 tensor에는
            Bitmap bitmap = BitmapFactory.decodeStream(buf);



            buf.close();





            //이미지 뷰에 선택한 사진 띄우기
          ImageView iv = findViewById(R.id.imageView2);

            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageBitmap(bitmap);
















            // x,y 최댓값 사진 크기에 따라 달라짐 (조절 해줘야함)
//            for (int x = 0; x < 224; x++) {
//                for (int y = 0; y < 224; y++) {
//                    int pixel = bitmap.getPixel(x, y);
//                    input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
//                    input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
//                    input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
//                }
//            }
//
//            //자신의 tflite 이름 써주기
//            Interpreter lite = getTfliteInterpreter("output226.tflite");
//
//            lite.run(input,output); //output


        } catch (IOException e) {
            e.printStackTrace();
        }


        //인식한부분 출력하기!!!!

//        int i;
//        for (i = 1; i <4; i++) {
//            if (output[0][0][i] * 100 > 90) {
//                if (i == 1) {
//                    tv_output.setText(String.format("bg  %d %.2f",i,  output[0][0][1] * 100));
//
//                }
//                else if (i == 2) {
//                    tv_output.setText(String.format("kim  %d  %.5f", i, output[0][0][2] * 100));
//                }
//                if (i == 3) {
//                    tv_output.setText(String.format("bab "));
//                }
//                else {
//                    tv_output.setText(String.format("아무 음식, %d, %.5f", i));
//
//                }
//              } else
//                continue;
//        }





//        Intent myintent = new Intent(FoodAddActivity.this,ResultListActivity.class);
//        myintent.putExtra("name",tv_output1.getText().toString());



        //tv_output.setText(String.format("ccc %f", output[0][0][3] * 100));
          //tv_output.setText(String.valueOf(output[1][48][4]));
        // tv_output1.setText(String.valueOf(output[0][0][3]));



    }





    //카메라에 맞게 이미지 로테이션
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission(); //권한체크
    }

    //권한 확인
    public void checkPermission() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //권한이 없으면 권한 요청
        if (permissionCamera != PackageManager.PERMISSION_GRANTED
                || permissionRead != PackageManager.PERMISSION_GRANTED
                || permissionWrite != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "이 앱을 실행하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // 권한이 취소되면 result 배열은 비어있다.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "권한 확인", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
                    finish(); //권한이 없으면 앱 종료
                }
            }
        }


    }








    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(FoodAddActivity.this, modelPath));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


}
