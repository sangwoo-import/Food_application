package com.example.mymanager;


import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Fragment3 extends Fragment {
    private static String TAG = "Mymanager_ImageAndDataActivity";
    public static final int REQUEST_CODE = 100;
    private static final String TAG_JSON = "필요영양정보";

    //php에 지정되어있는 칼로리 변수
    private static final String name1 = "name";
    private static final String TAG_ADDRESS = "열량(kcal)";

    private TextView progress;
    String mJsonString;

    View view;
    ProgressBar pb;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);
        progress = (TextView) view.findViewById(R.id.progress);

        initUI(view);

        GetData task = new GetData();
        task.execute("https://app-db-hdxqr.run.goorm.io/html/recq_progress.php");

        Button btn_foodadd = view.findViewById(R.id.btn_foodadd);
        btn_foodadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ImageAndDataActivity2.class);
                intent.putExtra("calories", "");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response  - " + result);

            if (result == null) {

            } else {

                mJsonString = result;
                showResult();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            try {


                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

//    -----------------------------------------------------------------


                // Input, Output Connection
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
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    String name = item.getString(name1);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(name1, name);


                    progress.setText(hashMap.get(name1));
                }

                setProgress();

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                return;
            }

            int calories = data.getExtras().getInt("calories");

            pb.setProgress(calories);
        }

    }

    private void initUI(View view) {
        final ArrayAdapter listViewAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice);
        listView = view.findViewById(R.id.list);
        listView.setAdapter(listViewAdapter);
    }

    private void setProgress() {
        String x;
        x = progress.getText().toString();
//        System.out.println(x);
        int y = Integer.parseInt(x);

        pb = view.findViewById(R.id.prgs);
//        progress max volume
        pb.setMax(2800);
        pb.setProgress(y);
    }

}

