package com.example.mymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Fragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment3, container, false);


                    Button breakfast_btn = (Button) v.findViewById(R.id.breakfast_btn);
                    breakfast_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),FoodAddActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button lunch_btn = (Button) v.findViewById(R.id.lunch_btn);
                    lunch_btn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Intent intent = new Intent(getActivity(), EditActivity.class);
                            startActivity(intent);
                        }
                    });

                    Button dinner_btn = (Button) v.findViewById(R.id.dinner_btn);
                    dinner_btn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            Intent intent = new Intent(getActivity(), EditActivity.class);
                            startActivity(intent);
                        }
                    });

        DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() { // 만들어놓을때 이벤트를 넣으면 된다.
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };

        return v;
    }
}