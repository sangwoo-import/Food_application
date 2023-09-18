package com.example.mymanager;

import static java.lang.System.out;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ImageAndDataActivity2 extends AppCompatActivity {
    //영양분 데이터 parsing
    private static String TAG = "Mymanager_ImageAndDataActivity";
    private static final String TAG_JSON = "영양정보";
    private static final String TAG_TAN = "탄수화물";
    private static final String TAG_ADDRESS = "열량(kcal)";
    private static final String TAG_DAN = "단백질";
    private static final String TAG_JI = "지방";
    //result apply request  php
    private static final String TAG_JSON1 = "영양정보1";
    private Map<String, String> map;
    private static String message1;
    private static String message2;
    private static String message3;
    private static String message4;
    private static String message5;

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;
    File tempSelectFile;


    private static String name1;


    private EditText fat, carb, calories, protein;

    public static final int REQUEST_TAKE_PHOTO = 10;
    public static final int REQUEST_PERMISSION = 11;
    private static final int FROM_ALBUM = 1;

    Button btnCamera, btn_gallery, btnSave, btn_add;

    private ImageView imageView2;
    private String mCurrentPhotoPath;
    private TextView tv_output1, tv_output2, tv_output3;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_edit);
        //EditText

        fat = (EditText) findViewById(R.id.fat);
        protein = (EditText) findViewById(R.id.protein);
        carb = (EditText) findViewById(R.id.carb);
        calories = (EditText) findViewById(R.id.calories);
//        ---------------------------------------------


        //pasring
        //mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        //mlistView.setVisibility(View.INVISIBLE); //리스트뷰 숨키기

        mArrayList = new ArrayList<>();

//        GetData task = new GetData();
//        task.execute("https://app-db-hdxqr.run.goorm.io/html/Nurt_return.php");


        //이미지 갤러리
        imageView2 = findViewById(R.id.imageView2);
        btnCamera = findViewById(R.id.btnCapture); //Button 선언
        tv_output1 = (TextView) findViewById(R.id.tv_output1);
        tv_output2 = (TextView) findViewById(R.id.tv_output2);
        tv_output3 = (TextView) findViewById(R.id.tv_output3);
        btn_add = (Button) findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            private Object Fragment3;

            @Override
            public void onClick(View view) {
                InsertData task = new InsertData();
                task.execute("https://app-db-hdxqr.run.goorm.io/html/userprogress.php");

                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ResultData task = new ResultData();
                                task.execute("https://app-db-hdxqr.run.goorm.io/html/result_apply.php");
                            }
                        }, 50);
                        Looper.loop();
                    }
                }).start();

                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ResultParsing task = new ResultParsing();
                                task.execute("https://app-db-hdxqr.run.goorm.io/html/result_request.php");
                            }
                        }, 50);
                        Looper.loop();
                    }
                }).start();


                Intent intent = new Intent(ImageAndDataActivity2.this, MainActivity.class);
                startActivity(intent);


            }
        });


        btn_gallery = findViewById(R.id.btn_gallery);

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setType("image/*");                      // 이미지만
                intent.setAction(Intent.ACTION_GET_CONTENT);    // 카메라(ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, FROM_ALBUM);

            }
        });
        imageView2.findViewById(R.id.imageView2);


        checkPermission();//권한 체크

        //촬영
        btnCamera.setOnClickListener(v -> captureCamera());
        //저장

    }


    //데이터 pasing시작하기
    private class GetData extends AsyncTask<String, Void, String> {

        //위에까지 건드리는것
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ImageAndDataActivity2.this,
                    "Please Wait", null, true, true);


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();


            Log.d(TAG, "response  - " + result);

            if (result == null) {

                mTextViewResult.setText(errorString);
            } else {

                mJsonString = result;
                showResult();

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String selectData;


            try {
                name1 = "name";

                String selectData1;
                String selectData2;
                String selectData3;


                String x;
                String y;
                String z;
                x = tv_output1.getText().toString(); //음식이름
                y = tv_output2.getText().toString();     //탄수화물
                z = tv_output3.getText().toString();
                selectData1 = "name1=" + x;
                selectData2 = "name1=" + x + "&name2=" + y;
                selectData3 = "name1=" + x + "&name2=" + y + "&name3=" + z;


                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

//    -----------------------------------------------------------------


                //3줄 갈아끼움
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
//-------------------------------------------------------------

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.connect();


                //어플에서 데이터 전송
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(selectData3.getBytes("UTF-8"));//원래 getBytes
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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


        private void showResult() {
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String tan = item.getString(TAG_TAN);
                    String address = item.getString(TAG_ADDRESS);
                    String dan = item.getString(TAG_DAN);
                    String ji = item.getString(TAG_JI);

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put(TAG_TAN, tan);

                    hashMap.put(TAG_ADDRESS, address);
                    hashMap.put(TAG_DAN, dan);
                    hashMap.put(TAG_JI, ji);


                    // get해서 받아오기
                    //foodname.setText(hashMap.get(name1));
                    carb.setText(hashMap.get(TAG_TAN));
                    protein.setText(hashMap.get(TAG_DAN));
                    fat.setText(hashMap.get(TAG_JI));
                    calories.setText(hashMap.get(TAG_ADDRESS));

                }


            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

        }
    }// 데이터 보내고 파싱하기 여기까지


    private class InsertData extends AsyncTask<String, Void, String> { //result apply에 데이터 삽입
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ImageAndDataActivity2.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            String goText;
            String goText1;
            String goText2;
            String goText3;
            String goText4;
            String goText5;
            String goText6;


            try {
                goText1 = carb.getText().toString();     //탄수화물
                goText2 = protein.getText().toString();  //단백질
                goText3 = fat.getText().toString();      //지방
                goText4 = calories.getText().toString();  //칼로리


                //userprogress에 보낼때
                goText5 = "200=" + goText4 + "&300=" + goText1 + "&400=" + goText2 + "&500="
                        + goText3;

                URL url = new URL(serverURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

//    -----------------------------------------------------------------


                //3줄 갈아끼움
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
//-------------------------------------------------------------

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.connect();


                //result apply
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(goText5.getBytes("UTF-8"));

                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }

        }
    }//progressbar db저장


    //result apply db 저장
    private class ResultData extends AsyncTask<String, Void, String> { //result apply에 데이터 삽입
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ImageAndDataActivity2.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            String goText;
            String goText1;
            String goText2;
            String goText3;
            String goText4;

            String goText6;


            try {
                //goText=foodname.getText().toString(); //음식이름
                goText1 = carb.getText().toString();     //탄수화물
                goText2 = protein.getText().toString();  //단백질
                goText3 = fat.getText().toString();      //지방
                goText4 = calories.getText().toString();  //칼로리

                //result apply에 보낼때
                goText6 = "NUTR_CONT2=" + goText1 + "&NUTR_CONT3=" + goText2
                        + "&NUTR_CONT4=" + goText3 + "&NUTR_CONT1=" + goText4;


                URL url = new URL(serverURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

//    -----------------------------------------------------------------


                //3줄 갈아끼움
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
//-------------------------------------------------------------

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.connect();


                //result apply
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(goText6.getBytes("UTF-8"));

                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return null;
            }

        }
    }

    //result parsing

    private class ResultParsing extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ImageAndDataActivity2.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {

                mTextViewResult.setText(errorString);
            } else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {
                name1 = "name";

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

//    -----------------------------------------------------------------
                //3줄 갈아끼움
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
//-------------------------------------------------------------

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.connect();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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


        private void showResult() {
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON1);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String tan = item.getString(TAG_TAN);
                    String name = item.getString(name1);
                    String address = item.getString(TAG_ADDRESS);
                    String dan = item.getString(TAG_DAN);
                    String ji = item.getString(TAG_JI);

//                    ------------2개일때---------


                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put(TAG_TAN, tan);
                    hashMap.put(name1, name);

                    hashMap.put(TAG_ADDRESS, address);
                    hashMap.put(TAG_DAN, dan);
                    hashMap.put(TAG_JI, ji);


                }


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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


        if (requestCode != FROM_ALBUM || resultCode != RESULT_OK) {
            return;// 갤러리 사용
        }

        Uri dataUri = data.getData();
        imageView2.setImageURI(dataUri);

        try {
            // ImageView 에 이미지 출력
            InputStream in = getContentResolver().openInputStream(dataUri);
            Bitmap image = BitmapFactory.decodeStream(in);
            imageView2.setImageBitmap(image);
            in.close();

            // 선택한 이미지 임시 저장
            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
            tempSelectFile = new File(Environment.getExternalStorageDirectory() + "/Pictures", "temp_" + date + ".jpg");
            OutputStream out = new FileOutputStream(tempSelectFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);


            send2Server(tempSelectFile);


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void send2Server(File file) {

        OkHttpClient okHttpCclient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.MINUTES)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .build();
        okhttp3.Request request = new Request.Builder()
                .url("https://ryo-cnnapi.run.goorm.io/imgfile") // Server URL 은 본인 IP를 입력
                .post(requestBody)
                .build();

        okHttpCclient.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    try {
                        message1 = jsonObject.getString("1");
                        if (message1 != null) {
                            tv_output1.setText(String.format(message1));
                        }
                    } catch (Exception e) {
                        System.out.println("dd");
                    }
                    try {

                        message2 = jsonObject.getString("1") + jsonObject.getString("2");
                        message4 = jsonObject.getString("2");
                        if (message2 != null) {
                            tv_output1.setText(String.format(message1));
                            tv_output2.setText(String.format(message4));
                            //tv_output2.setText(String.format(message2));
                        }
                    } catch (Exception e) {
                        System.out.println("dd");
                    }

                    try {
                        message3 = jsonObject.getString("1") + jsonObject.getString("2") + jsonObject
                                .getString("3");
                        message5 = jsonObject.getString("3");
                        if (message3 != null) {
                            //tv_output1.setText(String.format(message3));
                            tv_output1.setText(String.format(message1));
                            tv_output2.setText(String.format(message4));
                            tv_output3.setText(String.format(message5));
                        }
                    } catch (Exception e) {
                        System.out.println("dd");
                    }

                    body.close();

//---------------------------------쓰레드 2개 이상쓸때--------------------
                    new Thread(new Runnable() {
                        public void run() {
                            Looper.prepare();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    GetData task = new GetData();
                                    task.execute("https://app-db-hdxqr.run.goorm.io/html/Nurt_return.php");

                                }
                            }, 50);
                            Looper.loop();
                        }
                    }).start();

//----------------------------------------쓰레드 2개-------------------------------

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


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

