package com.example.mymanager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;


public class Fragment3 extends Fragment
{
    private static String TAG = "Mymanager_ImageAndDataActivity";
    public static final int REQUEST_CODE = 100;


    private static final String TAG_ADDRESS ="열량(kcal)";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;

    View view;
    ProgressBar pb;
    ListView listView;
    ArrayList<String>items;
    EditText text;
    Button btn_foodadd, btn_goaledit;


    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment3, container, false);
        initUI(view);


        Button btn_foodadd = (Button) view.findViewById(R.id.btn_foodadd);
        btn_foodadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), ImageAndDataActivity.class);
                intent.putExtra("calories", "");
                startActivityForResult(intent,REQUEST_CODE);

            }
        });

        Button btn_goaledit = (Button) view.findViewById(R.id.btn_goaledit);
        btn_goaledit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(getActivity(), GoalEditActivity.class);
                startActivity(intent);

            }
         });


//        private void showResult(){
//        try {
//            JSONObject jsonObject = new JSONObject(mJsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//
//            for(int i=0;i<jsonArray.length();i++){
//
//                JSONObject item = jsonArray.getJSONObject(i);
//
//                String address = item.getString(TAG_ADDRESS);
//
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put(TAG_ADDRESS, address);
//
//                mArrayList.add(hashMap);
//            }
//
//            ListAdapter adapter = new SimpleAdapter(
//                    ResultListActivity.this, mArrayList, R.layout.food_edit,
//                    new String[]{TAG_ADDRESS},
//                    new int[]{R.id.foodname, R.id.carb, R.id.protein,R.id.fat,R.id.calories}
//            );
//
//            mlistView.setAdapter(adapter);
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//        }

//    }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

            int calories = data.getExtras().getInt("calories");

            pb.setProgress(calories);
        }
    }


    private void initUI(View view)
    {
        final ArrayAdapter listViewAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice);
        listView = view.findViewById(R.id.list);
        listView.setAdapter(listViewAdapter);

        setProgress();
    }

    private void setProgress()
    {
        pb = view.findViewById(R.id.prgs);
        pb.setMax(2000);
//        pb.setProgress(setProgressData());
        //pb.setProgress(Integer.parseInt(TAG_ADDRESS));
        pb.setProgress(0);
    }

    private int setProgressData()
    {
//    setProgressData();

        return 1;
    }

}

