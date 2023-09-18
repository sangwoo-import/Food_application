package com.example.mymanager;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2, container, false);


        // 다이어트 웹 사이트로 이동
        ImageButton Diet_btn = v.findViewById(R.id.Diet_btn);
        Diet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DietActivity.class); // 프레그먼트에서는 getActivity를사용해야됨
                startActivity(intent);
            }
        });

        //벌크업 웹 사이트로 이동
        ImageButton bulk_btn = v.findViewById(R.id.bulk_btn);
        bulk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BulkActivity.class);
                startActivity(intent);
            }
        });

        //당뇨 웹 사이트로 이동
        ImageButton dn_btn = v.findViewById(R.id.dn_btn);
        dn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DangNewoActivity.class);
                startActivity(intent);
            }
        });

        //고혈압 웹 사이트로 이동
        ImageButton go_btn = v.findViewById(R.id.go_btn);
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GohyulAapActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
