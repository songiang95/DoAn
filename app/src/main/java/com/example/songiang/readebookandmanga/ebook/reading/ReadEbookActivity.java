package com.example.songiang.readebookandmanga.ebook.reading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.utils.Constant;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadEbookActivity extends AppCompatActivity {

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_ebook);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String onlineLink = intent.getStringExtra(Constant.EXTRA_READ_ONLINE);
        webView.setWebViewClient(new MyWebview());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(onlineLink);
    }

    class MyWebview extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbLoading.setVisibility(View.GONE);
        }
    }

}
