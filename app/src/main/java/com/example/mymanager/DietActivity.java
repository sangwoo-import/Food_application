package com.example.mymanager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class DietActivity extends AppCompatActivity {

    private WebView webView;
    private String url = "https://www.youtube.com/results?search_query=%EB%8B%A4%EC%9D%B4%EC%96%B4%ED%8A%B8+%EC%8B%9D%EB%8B%A8"; // 원하는 주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        webView = findViewById(R.id.webView); // webview id 값

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
