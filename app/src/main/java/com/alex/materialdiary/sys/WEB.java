package com.alex.materialdiary.sys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.alex.materialdiary.ui.login.LoginActivity;

public class WEB extends WebViewClient {
    Fragment diaryInterface;
    public WEB(Fragment diaryInterface){
        this.diaryInterface = diaryInterface;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return handleUri(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return handleUri(view, request.getUrl().toString());
    }
    Boolean handleUri(WebView view, String url) {
        String gcookies = CookieManager.getInstance().getCookie(url);
        Log.d("web", gcookies);
        if (url.equals("https://one.pskovedu.ru/edv/index/error/access_denied")) {
            Intent i = new Intent(diaryInterface.getContext(), LoginActivity.class);
            diaryInterface.startActivity(i);
            view.loadUrl(url);
        }
        return true;
    }
}
