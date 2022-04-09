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

    //@Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment2, container, false);



        //return inflater.inflate(R.layout.fragment3,container, false);

        // 다이어트 페이지 간다.
        ImageButton Diet_btn = (ImageButton) v.findViewById(R.id.Diet_btn);
        Diet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DietActivity.class); // 프레그먼트에서는 getActivity를사용해야됨
                startActivity(intent);
            }
        });

        //벌크업 페이지 간다.
        ImageButton bulk_btn = (ImageButton) v. findViewById(R.id.bulk_btn);
        bulk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BulkActivity.class);
                startActivity(intent);
            }
        });

        //당뇨 페이지 간다.
        ImageButton dn_btn = (ImageButton) v. findViewById(R.id.dn_btn);
        dn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DangNewoActivity.class);
                startActivity(intent);
            }
        });
        //고혈압 페이지 간다.
        ImageButton go_btn = (ImageButton) v. findViewById(R.id.go_btn);
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GohyulAapActivity.class);
                startActivity(intent);
            }
        });


        // 닫기 버튼
//        Button btn_close = (Button)findViewById(R.id.btn_close);
//        btn_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//            }
//        });
//
//        drawerLayout.setDrawerListener(listener);
//        drawerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//        });
//    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) { //안드로이드에서 어떤 특정한  key값을 지정 해줘라
//        if((keyCode == KeyEvent.KEYCODE_BACK)&& webView.canGoBack() ){
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    private class WebViewClientClass extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) { //현재 페이지 보일수 있는 메소드
//            view.loadUrl(url);
//            return true;
//        }
//    }


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
