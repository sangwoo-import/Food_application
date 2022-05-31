package com.example.mymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class GoalEditActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private String userGoal;
    private Button btn_modify;
    private RadioButton radio_diet, radio_bulk, radio_dangn, radio_gohyul;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goaledit);
//        //아이디 값 찾아주기
//        radio_diet = findViewById(R.id.radio_diet);
//        radio_bulk = findViewById(R.id.radio_bulk);
//        radio_dangn = findViewById(R.id.radio_dangn);
//        radio_gohyul = findViewById(R.id.radio_gohyul);


//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == R.id.radio_diet) {
//                    Toast.makeText(GoalEditActivity.this, "다이어트", Toast.LENGTH_SHORT).show();
//                    userGoal = radio_diet.getText().toString();
//                } else if (i == R.id.radio_bulk) {
//                    Toast.makeText(GoalEditActivity.this, "벌크업", Toast.LENGTH_SHORT).show();
//                    userGoal = radio_bulk.getText().toString();
//                } else if (i == R.id.radio_dangn) {
//                    Toast.makeText(GoalEditActivity.this, "당뇨", Toast.LENGTH_SHORT).show();
//                    userGoal = radio_dangn.getText().toString();
//                } else if(i == R.id.radio_gohyul) {
//                    Toast.makeText(GoalEditActivity.this, "고혈압", Toast.LENGTH_SHORT).show();
//                    userGoal = radio_gohyul.getText().toString();
//                }
//            }
//        });

//        btn_modify = findViewById(R.id.btn_modify);
//        btn_modify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //라디오버튼 값 get(가져온다)해야될듯
//
//
//
//                Response.Listener<String> responseListener=new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jasonObject=new JSONObject(response);//Register2 php에 response
//                            boolean success=jasonObject.getBoolean("success");//Register2 php에 sucess
////                            if(userGoal) {
////                                if (success) { //목표수정 성공한 경우
////                                    Toast.makeText(getApplicationContext(), "목표 수정 성공", Toast.LENGTH_SHORT).show();
////                                    Intent intent = new Intent(GoalEditActivity.this, RegisterActivity.class);
////                                    startActivity(intent);
////                                }
////                            }
//                                //TODO : 주석 풀어
////                            else{//목표수정 실패한 경우
////                                Toast.makeText(getApplicationContext(),"목표 수정 실패",Toast.LENGTH_SHORT).show();
////                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        }
//                };
//
//            }
//        });
    }
}
