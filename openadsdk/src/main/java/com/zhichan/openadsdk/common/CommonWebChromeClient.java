package com.zhichan.openadsdk.common;

import android.util.Log;
import android.webkit.WebView;

import com.just.agentweb.WebChromeClient;

public class CommonWebChromeClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.i("CommonWebChromeClient", "onProgressChanged:" + newProgress + "  view:" + view);
    }
}
