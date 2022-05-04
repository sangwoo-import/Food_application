package com.example.mymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<String> items;
    EditText text;
    Button add, modify, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        text = findViewById(R.id.text);
        add = findViewById(R.id.add);
        modify = findViewById(R.id.modify);
        delete = findViewById(R.id.delete);

        items = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items);
        listview = findViewById(R.id.View);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = adapter.getCount();
                items.add(Integer.toString(count + 1) + ") " + text.getText());
                adapter.notifyDataSetChanged();
                text.setText("");
            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check;
                int count = adapter.getCount();
                if (count > 0) {
                    check = listview.getCheckedItemPosition(); //선택 항목 position 얻기
                    if (check > -1 && check < count)
                        items.set(check, Integer.toString(check + 1) + ") " + text.getText() + " (수정됨)");
                    adapter.notifyDataSetChanged();
                    text.setText("");
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check, count= adapter.getCount();
                if (count>0){
                    check = listview.getCheckedItemPosition();
                    if (check>-1 && check<count){
                        items.remove(check);
                        listview.clearChoices();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}



