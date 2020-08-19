package com.zhichan.openadsdk.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.just.agentweb.AgentWeb;
import com.zhichan.openadsdk.R;
import com.zhichan.openadsdk.holder.MsmBannerAdLoadHolder;

import org.json.JSONException;
import org.json.JSONObject;

public class MsmIntegralFragment extends AgentWebFragment implements MsmBannerAdLoadHolder.BannerAdListener {

    public static MsmIntegralFragment getInstance(Bundle bundle) {

        MsmIntegralFragment msmIntegralFragment = new MsmIntegralFragment();
        if (msmIntegralFragment != null) {
            MsmBannerAdLoadHolder.getInstance().setBannerAdListener(msmIntegralFragment);
            msmIntegralFragment.setArguments(bundle);
        }

        return msmIntegralFragment;
    }

    @Override
    public String getUrl() {
        return "http://192.168.0.222:8080/#/test";
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((ViewGroup) view.findViewById(R.id.web_layout), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(-1, 2)
                .setAgentWebWebSettings(getSettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());


        initView(view);

        mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new MyJavascriptInterface(this.getActivity()), "msmdsInjected");

    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onAdClicked(View view, int type) {

    }

    @Override
    public void onAdShow(View view, int type) {

    }

    @Override
    public void onRenderFail(View view, String msg, int code) {

    }

    @Override
    public void onRenderSuccess(View view, float width, float height) {
        Log.i("MsmIntegralFragment", "onRenderSuccess: " + width);
        Log.i("MsmIntegralFragment", "onRenderSuccess: " + height);
        webLayout.addView(view);
    }

    @Override
    public void onShield(String filter) {
        if (webLayout.getChildCount() >= 1) {
            for (int i = 1; i < webLayout.getChildCount(); i++) {
                View view = webLayout.getChildAt(i);
                webLayout.removeView(view);
            }
        }
    }

    public class MyJavascriptInterface {
        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showToutiaoBannerAd(String data) {
            Log.e("showToutiaoBannerAd", data);
            try {
                //String转JSONObject
                JSONObject result = new JSONObject(data);
                //取数据
                String codeId = result.getString("androidCodeId");
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));
                MsmBannerAdLoadHolder.getInstance().bannerAdLoad(context, codeId, width, height);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
