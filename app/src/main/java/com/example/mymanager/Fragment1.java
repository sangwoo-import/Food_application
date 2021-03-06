package com.example.mymanager;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.tensorflow.lite.Interpreter;

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

public class Fragment1 extends Fragment {

    private static final String TAG = "MainActivity"; //  이거안되면 CameraActivity

    public static final int REQUEST_TAKE_PHOTO = 10;
    public static final int REQUEST_PERMISSION = 11;

    private Button btnCamera, btnSave,button_1;
    private ImageView ivCapture;
    private String mCurrentPhotoPath;
    private TextView tv_output;
    private static final int FROM_ALBUM = 1;
   
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View s = inflater.inflate(R.layout.fragment1, container, false);



        button_1 = s.findViewById(R.id.button_1);

        button_1.setOnClickListener(new View.OnClickListener() {  // 갤러리 가져오기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");                      // 이미지만
                intent.setAction(Intent.ACTION_GET_CONTENT);    // 카메라(ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, FROM_ALBUM);
            }

        });


        checkPermission(); //권한체크

         ivCapture =s.findViewById(R.id.ivCapture); //ImageView 선언
         btnCamera =s.findViewById(R.id.btnCapture); //Button 선언
         btnSave = s.findViewById(R.id.btnSave); //Button 선언
         tv_output = s.findViewById(R.id.tv_output);


        //loadImgArr();

        //촬영
        btnCamera.setOnClickListener(v -> captureCamera());

        //저장
        btnSave.setOnClickListener(v -> {

            try {

                BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                //찍은 사진이 없으면
                if (bitmap == null) {
                    Toast.makeText(getActivity(), "저장할 사진이 없습니다.", Toast.LENGTH_SHORT).show(); //프래그먼트에서는 getActivity를 사용
                } else {
                    //저장
                    saveImg();
                    mCurrentPhotoPath = ""; //initialize
                }

            } catch (Exception e) {
                Log.w(TAG, "SAVE ERROR!", e);
            }
        });

        return s; // 리턴을 무조건 해줘야함

        }

    
        private void captureCamera () {  //카메라 촬영
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



            // 인텐트를 처리 할 카메라 액티비티가 있는지 확인
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {  // 프래그먼트에서 getActivity. 를 붙여줘야함

                // 촬영한 사진을 저장할 파일 생성
                File photoFile = null;

                try {
                    //임시로 사용할 파일이므로 경로는 캐시폴더로
                    File tempDir = getContext().getCacheDir();  // getContext 를 붙여줘야한다.

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
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), // 원래는 this
                            getActivity().getPackageName() + ".fileprovider",
                            photoFile);
                    //인텐트에 Uri담기
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    //인텐트 실행
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }





        //이미지저장 메소드
        private void saveImg () {

            try {
                //저장할 파일 경로
                File storageDir = new File(getContext().getFilesDir() + "/capture"); // getContext를 추가하였다.
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
                    BitmapDrawable drawable = (BitmapDrawable) ivCapture.getDrawable();
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
                Toast.makeText(getActivity(), "Capture Saved ", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.w(TAG, "Capture Saving Error!", e);
                Toast.makeText(getActivity(), "Save failed", Toast.LENGTH_SHORT).show();

            }
        }

//    private void loadImgArr() { //이미지 로드
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
//


        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent intent){ //이미지 촬영 이미지뷰 및 갤러리 텐서플로우
            super.onActivityResult(requestCode, resultCode, intent);

            float[][][][] input = new float[1][224][224][3];
            float[][][] output = new float[1][12543][4];

            try {
                //after capture
                switch (requestCode) {
                    case REQUEST_TAKE_PHOTO: {
                        //갤러리 가져오는 구문 시작

                        if (resultCode == Activity.RESULT_OK  ) {
                            // Activity를 추가하였음



                            File file = new File(mCurrentPhotoPath);
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));//getActivity를 붙임

                            if (bitmap != null) {
                                ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                int batchNum = 0;
                                for (int x = 0; x < 224; x++) {
                                    for (int y = 0; y < 224; y++) {
                                        int pixel = bitmap.getPixel(x, y);
                                        input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
                                        input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
                                        input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
                                    }
                                }

                                Interpreter lite = getTfliteInterpreter("output224.tflite");

                                lite.run(input,output);

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
                                ivCapture.setImageBitmap(rotatedBitmap);

                            }
                        }
                        break;
                    }


                }

            } catch (Exception e) {
                Log.w(TAG, "onActivityResult Error !", e);
            }





            try { // 갤러리에서 가져오기
                //after capture
                switch (requestCode) {
                    case FROM_ALBUM: {
                        //갤러리 가져오는 구문 시작

                        if (resultCode == Activity.RESULT_OK  ) {
                            // Activity를 추가하였음



                            File file = new File(mCurrentPhotoPath);
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));//getActivity를 붙임

                            if (bitmap != null) {
                                ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                int batchNum = 0;
                                for (int x = 0; x < 224; x++) {
                                    for (int y = 0; y < 224; y++) {
                                        int pixel = bitmap.getPixel(x, y);
                                        input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
                                        input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
                                        input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
                                    }
                                }

                                Interpreter lite = getTfliteInterpreter("output224.tflite");

                                lite.run(input,output);

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
                                ivCapture.setImageBitmap(rotatedBitmap);

                            }
                        }
                        break;
                    }
                }

            }
            catch (Exception e) {
                Log.w(TAG, "onActivityResult Error !", e);
            }



            //인식한부본 출렫하기!!!!
            int i;
            for (i = 0; i <4; i++) {
                if (output[0][0][i] * 100 > 30) {
                    if (i == 0) {
                        tv_output.setText(String.format("bg  %d %.5f", i, output[0][1][0] * 100));
                    } else if (i == 1) {
                        tv_output.setText(String.format("kim  %d  %.5f", i, output[0][1][1] * 100));
                    } else if (i == 2) {
                        tv_output.setText(String.format("kimchi %d, %.5f", i, output[0][1][2] * 100));
                    }
                    else if (i == 3) {
                        tv_output.setText(String.format("bab, %d, %.5f", i, output[0][1][3] * 100));
                    }
                    else {
                        tv_output.setText(String.format("아무 음식, %d, %.5f", i));
                        //output[0][0][4] * 100)
                    }
                } else
                    continue;
            }
        }




        //카메라에 맞게 이미지 로테이션
        public static Bitmap rotateImage (Bitmap source,float angle){
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        }

        @Override
        public void onResume () {
            super.onResume();
            checkPermission(); //권한체크
        }

        //권한 확인
        public void checkPermission () {
            int permissionCamera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            int permissionRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWrite = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            //권한이 없으면 권한 요청
            if (permissionCamera != PackageManager.PERMISSION_GRANTED
                    || permissionRead != PackageManager.PERMISSION_GRANTED
                    || permissionWrite != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) { //ActivityCompat임
                    Toast.makeText(getActivity(), "이 앱을 실행하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }

                ActivityCompat.requestPermissions(getActivity(), new String[]{
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

                    Toast.makeText(getActivity(), "권한 확인", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "권한 없음", Toast.LENGTH_LONG).show();
                    getActivity().finish(); //권한이 없으면 앱 종료  //getActivity를 넣음
                }
            }
        }


    }


    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(getActivity(), modelPath)); //getActivity 아닐 수도있음

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


