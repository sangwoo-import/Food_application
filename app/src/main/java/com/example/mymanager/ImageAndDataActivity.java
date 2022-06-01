package com.example.mymanager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.load.engine.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ImageAndDataActivity  extends AppCompatActivity {
    //영양분 데이터 pasing
    private static String TAG = "Mymanager_ImageAndDataActivity";

    private static final String TAG_JSON="영양정보";
    //private static final String TAG_ID = "id";
    private static final String TAG_TAN = "탄수화물";
    //private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS ="열량(kcal)";
    private static final String TAG_DAN = "단백질";
    private static final String TAG_JI = "지방";



    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;


    private static String  name1;
    private static String TAG_FOOD;

    EditText foodname, fat, carb,calories,protein;


//갤러리 카메라 구문
    //private static final String TAG = "testActivity"; //  이거안되면 CameraActivity
    //public static final String KEY="photo";

    public static final int REQUEST_TAKE_PHOTO = 10;
    public static final int REQUEST_PERMISSION = 11;
    private static final int FROM_ALBUM = 1;

    private Button btnCamera, btn_gallery, btnSave ,btn_add;
    private ImageView imageView2;
    private String mCurrentPhotoPath;
    private TextView tv_output1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_edit);



        //EditText
        foodname=(EditText)findViewById(R.id.foodname);
        fat=(EditText)findViewById(R.id.fat);
        protein=(EditText)findViewById(R.id.protein);
        carb=(EditText)findViewById(R.id.carb);
        calories=(EditText)findViewById(R.id.calories);

        //pasring
        //mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        //mlistView.setVisibility(View.INVISIBLE); //리스트뷰 숨키기

        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("https://app-db-hdxqr.run.goorm.io/html/Nurt_return.php");


        //이미지 갤러리
        imageView2 =findViewById(R.id.imageView2);
        btnCamera = findViewById(R.id.btnCapture); //Button 선언
        btnSave = findViewById(R.id.btnSave); //Button 선언
        tv_output1 =(TextView) findViewById(R.id.tv_output1);
        btn_gallery= findViewById(R.id.btn_gallery);
        btn_add=(Button)findViewById(R.id.btn_add);

        tv_output1.setText("미역국");


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                String name = foodname.getText().toString();
//                String NUTR_CONT2= carb.getText().toString();
//                String NUTR_CONT3 = protein.getText().toString();
//                String NUTR_CONT4 = fat.getText().toString();
//                String NUTR_CONT1 = calories.getText().toString();
//                GetData task = new GetData();
//                task.execute("https://app-db-hdxqr.run.goorm.io/html/result_apply.php",name, NUTR_CONT2
//                ,NUTR_CONT3,NUTR_CONT4,NUTR_CONT1);

                private void showResult(){
                    try {
                        JSONObject jsonObject = new JSONObject(mJsonString);
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject item = jsonArray.getJSONObject(i);

                            String tan = item.getString(TAG_TAN);

                            //String food= item.getString(TAG_FOOD);
                            String name = item.getString(name1);
                            String address = item.getString(TAG_ADDRESS);
                            String dan = item.getString(TAG_DAN);
                            String ji = item.getString(TAG_JI);



                            HashMap<String,String> hashMap = new HashMap<>();

                            hashMap.put(TAG_TAN, tan);
                            hashMap.put(name1, name);
                            hashMap.put(TAG_ADDRESS, address);
                            hashMap.put(TAG_DAN,dan);
                            hashMap.put(TAG_JI,ji);

                            // hashMap.put(TAG_FOOD,food);

                            //mArrayList.add(hashMap);

                            //get하기
                            foodname.setText(hashMap.get(name1));
                            carb.setText(hashMap.get(TAG_TAN));
                            protein.setText(hashMap.get(TAG_DAN));
                            fat.setText(hashMap.get(TAG_JI));
                            calories.setText(hashMap.get(TAG_ADDRESS));

                        }

//                ListAdapter adapter = new SimpleAdapter(
//                        ImageAndDataActivity.this, mArrayList, R.layout.food_edit,
//                        new String[]{name1, TAG_TAN,TAG_DAN,TAG_JI,TAG_ADDRESS},
//                        new int[]{R.id.foodname, R.id.carb, R.id.protein,R.id.fat,R.id.calories}
//                );
//
//                mlistView.setAdapter(adapter);

                    } catch (JSONException e) {

                        Log.d(TAG, "showResult : ", e);
                    }

                }

                GetData task = new GetData();
                task.execute("https://app-db-hdxqr.run.goorm.io/html/recq_progress.php");


                Log.e("나나나난", "나 들어옴");
                Intent intentR = new Intent();
                intentR.putExtra("calories" , Integer.parseInt(calories.getText().toString())); //사용자에게 입력받은값 넣기
                setResult(RESULT_OK,intentR); //결과를 저장
                finish();



            }

        });



        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setType("image/*");                      // 이미지만
                intent.setAction(Intent.ACTION_GET_CONTENT);    // 카메라(ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, FROM_ALBUM);

            }
        });
        checkPermission();//권한 체크

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


    //데이터 pasing시작하기
    private class GetData extends AsyncTask<String, Void, String> {



        //위에까지 건드리는것
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ImageAndDataActivity.this,
                    "Please Wait", null, true, true);
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
//            String name = params[1];  //name
//            String NUTR_CONT2 = params[4];//탄수화물
//            String NUTR_CONT3 = params[5];//단백질
//            String NUTR_CONT4 = params[6];//지방
//            String NUTR_CONT1 = params[3];//열량
//            String selectData = "name=$name & NUTR_CONT2=$nutr2 & NUTR_CONT3=$nutr3 & NUTR_CONT4=$nutr4 & NUTR_CONT1=$nutr1";


            try {
                name1="name";

                //TAG_FOOD="미역국";

                //selectData = "Data=" + tv_output1.getText().toString();
                // 따옴표 안과 php의 post [ ] 안이 이름이 같아야 함

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.connect();

                //어플에서 데이터 전송
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(selectData.getBytes("UTF-8"));//원래 getBytes
//                outputStream.flush();
//                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }



        private void showResult(){
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);

                    String tan = item.getString(TAG_TAN);

                    //String food= item.getString(TAG_FOOD);
                    String name = item.getString(name1);
                    String address = item.getString(TAG_ADDRESS);
                    String dan = item.getString(TAG_DAN);
                    String ji = item.getString(TAG_JI);



                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(TAG_TAN, tan);
                    hashMap.put(name1, name);
                    hashMap.put(TAG_ADDRESS, address);
                    hashMap.put(TAG_DAN,dan);
                    hashMap.put(TAG_JI,ji);

                   // hashMap.put(TAG_FOOD,food);

                    //mArrayList.add(hashMap);

                    //get하기
                    foodname.setText(hashMap.get(name1));
                    carb.setText(hashMap.get(TAG_TAN));
                    protein.setText(hashMap.get(TAG_DAN));
                    fat.setText(hashMap.get(TAG_JI));
                    calories.setText(hashMap.get(TAG_ADDRESS));

                }

//                ListAdapter adapter = new SimpleAdapter(
//                        ImageAndDataActivity.this, mArrayList, R.layout.food_edit,
//                        new String[]{name1, TAG_TAN,TAG_DAN,TAG_JI,TAG_ADDRESS},
//                        new int[]{R.id.foodname, R.id.carb, R.id.protein,R.id.fat,R.id.calories}
//                );
//
//                mlistView.setAdapter(adapter);

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


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

        if (requestCode != FROM_ALBUM || resultCode != RESULT_OK)// 갤러리 사용
            return;

        try {  // 갤러리 사용

            //int batchNum = 0;
            InputStream buf = getContentResolver().openInputStream(intent.getData()); // intetn대신 data였음 tensor에는

            Bitmap bitmap = BitmapFactory.decodeStream(buf);

            buf.close();

            //이미지 뷰에 선택한 사진 띄우기
            ImageView iv = findViewById(R.id.imageView2);


            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //tv_output1.setText(String.format("ggg"));

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



}

