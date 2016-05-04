package com.pktworld.physicalweb.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pktworld.physicalweb.R;
import com.pktworld.physicalweb.util.ApplicationConstant;

public class MainActivity extends AppCompatActivity {

    private String data = null;
    private WebView webView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView)findViewById(R.id.webView1);
        try {
            data = getIntent().getStringExtra(ApplicationConstant.DATA);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyWebViewClient());
            webView.loadUrl(data);
            mProgressDialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading...", true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
