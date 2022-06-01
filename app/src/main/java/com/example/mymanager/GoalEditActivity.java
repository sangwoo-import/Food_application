package com.example.mymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public static final int REQUEST_CODE_MAIN=100;
    public static final int REQUEST_CODE_MODIFY=101;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goaledit);

        radio_diet = findViewById(R.id.radio_diet);
        radio_bulk = findViewById(R.id.radio_bulk);
        radio_dangn = findViewById(R.id.radio_dangn);
        radio_gohyul = findViewById(R.id.radio_gohyul);

        Button button = findViewById(R.id.btn_modify);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_diet) {
                    Toast.makeText(GoalEditActivity.this, "다이어트", Toast.LENGTH_SHORT).show();
                    userGoal = radio_diet.getText().toString();
                } else if (i == R.id.radio_bulk) {
                    Toast.makeText(GoalEditActivity.this, "벌크업", Toast.LENGTH_SHORT).show();
                    userGoal = radio_bulk.getText().toString();
                } else if (i == R.id.radio_dangn) {
                    Toast.makeText(GoalEditActivity.this, "당뇨", Toast.LENGTH_SHORT).show();
                    userGoal = radio_dangn.getText().toString();
                } else if(i == R.id.radio_gohyul) {
                    Toast.makeText(GoalEditActivity.this, "고혈압", Toast.LENGTH_SHORT).show();
                    userGoal = radio_gohyul.getText().toString();
                }
            }
        });


        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_MODIFY);
            }
        });
    }
    public void modify(View view) {
        Intent receiveIntent=getIntent();
        String id=receiveIntent.getStringExtra("userID");
    }
    }
