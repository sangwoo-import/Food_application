package com.example.mymanager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class GohyulAapActivity extends AppCompatActivity {
    private WebView webView;
    private String url = "https://dpg.danawa.com/news/view?boardSeq=66&listSeq=2923056&past=Y"; // 원하는 주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gohyul);

        webView = findViewById(R.id.webView); // webview id 값

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //안드로이드에서 어떤 특정한  key값을 지정 해줘라
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //현재 페이지 보일수 있는 메소드
            view.loadUrl(url);
            return true;
        }
    }
}
