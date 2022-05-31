package com.example.mymanager;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultListActivity extends AppCompatActivity {
    private static String TAG = "Mymanager_ResultListActivity";

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); // 원래 acticty_list임
        Log.e("aa","a");

        //mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mlistView = (ListView) findViewById(R.id.listView_main_list);

        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("https://app-db-hdxqr.run.goorm.io/html/Nurt_return.php");


    }

    private class GetData extends AsyncTask<String, Void, String> {




        //위에까지 건드리는것
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ResultListActivity.this,
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


            try {
                name1="name";
//                Intent intent =getIntent();
//                name1="name"+intent.getExtras().getString("name");;//요청 변수 만들기

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                //어플에서 데이터 전송
//               OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(name1.getBytes("UTF-8"));
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

                    mArrayList.add(hashMap);
                }

                ListAdapter adapter = new SimpleAdapter(
                        ResultListActivity.this, mArrayList, R.layout.food_edit,
                        new String[]{name1, TAG_TAN,TAG_DAN,TAG_JI,TAG_ADDRESS},
                        new int[]{R.id.foodname, R.id.carb, R.id.protein,R.id.fat,R.id.calories}
                );

                mlistView.setAdapter(adapter);

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

        }
    }




}



