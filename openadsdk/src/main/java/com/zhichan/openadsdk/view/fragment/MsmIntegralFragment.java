package com.zhichan.openadsdk.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.just.agentweb.AgentWeb;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.zhichan.openadsdk.R;
import com.zhichan.openadsdk.holder.MsmBannerAdLoadHolder;
import com.zhichan.openadsdk.holder.MsmNativeAdLoadHolder;
import com.zhichan.openadsdk.holder.MsmRewardVideoAdLoadHolder;

import org.json.JSONException;
import org.json.JSONObject;

public class MsmIntegralFragment extends AgentWebFragment implements
        MsmBannerAdLoadHolder.BannerAdListener,
        MsmRewardVideoAdLoadHolder.RewardVideoAdListener,
        MsmNativeAdLoadHolder.NativeAdListener
{

    public static final String BANNER_CODE_ID = "banner_code_id";
    public static final String NATIVE_CODE_ID = "native_code_id";
    public static final String REWARD_VIDEO_CODE_ID = "reward_video_code_id";

    private String top = "0";
    private String nativeTop = "0";

    public static MsmIntegralFragment getInstance(Bundle bundle) {

        MsmIntegralFragment msmIntegralFragment = new MsmIntegralFragment();
        if (msmIntegralFragment != null) {
            MsmRewardVideoAdLoadHolder.getInstance().setRewardVideoAdListener(msmIntegralFragment);
            MsmBannerAdLoadHolder.getInstance().setBannerAdListener(msmIntegralFragment);
            MsmNativeAdLoadHolder.getInstance().setNativeAdListener(msmIntegralFragment);
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

    // ======================== Banner广告监听 ==================//
    @Override
    public void onError(int i, String s) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadError();");
    }

    @Override
    public void onRenderFail(View view, String msg, int code) {

    }

    @Override
    public void onRenderSuccess(View view, float width, float height) {
        Log.i("MsmIntegralFragment", "onRenderSuccess: " + width);
        Log.i("MsmIntegralFragment", "onRenderSuccess: " + height);
        view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(top)));
        mAgentWeb.getWebCreator().getWebView().removeAllViews();
        mAgentWeb.getWebCreator().getWebView().addView(view);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadSuccess({height:"+height+"});");

    }

    @Override
    public void onShield(String filter) {
        Log.i(TAG, "onShield: " + mAgentWeb.getWebCreator().getWebView().getChildCount());
        mAgentWeb.getWebCreator().getWebView().removeAllViews();
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onHideManually();");
    }
    // ======================== Banner广告监听 ==================//

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsmBannerAdLoadHolder.getInstance().onDestroy();
    }

    // ======================== 激励视频广告监听 ==================//
    @Override
    public void onRewardError(int i, String s) {

    }

    @Override
    public void onRewardAdClose() {

    }

    @Override
    public void onRewardVideoComplete() {

    }

    @Override
    public void onRewardVideoError() {

    }

    @Override
    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onRewardVerify();");
    }

    @Override
    public void onSkippedVideo() {

    }
    // ======================== 激励视频广告监听 ==================//

    // ======================== 信息流广告监听 ==================//
    @Override
    public void onNativeError(int i, String s) {

    }

    @Override
    public void onNativeRenderFail(View view, String msg, int code) {

    }

    @Override
    public void onNativeRenderSuccess(View view, float width, float height) {

    }

    @Override
    public void onNativeShield(String filter) {

    }
    // ======================== 信息流广告监听 ==================//

    public class MyJavascriptInterface {
        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }

        /**
         * 买什么都省h5页面的banner广告
         * @param data
         */
        @JavascriptInterface
        public void showToutiaoBannerAd(String data) {
            Log.e("showToutiaoBannerAd", data);
            try {
                //String转JSONObject
                JSONObject result = new JSONObject(data);
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));
                top = styleObj.getString("top");
                assert MsmIntegralFragment.this.getArguments() != null;
                String codeId = MsmIntegralFragment.this.getArguments().getString(BANNER_CODE_ID);
                MsmBannerAdLoadHolder.getInstance().bannerAdLoad(context, codeId, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面加载激励视频广告
         * @param data
         */
        @JavascriptInterface
        public void loadToutiaoRewardVideoAd(String data) {
            Log.e("loadToutiaoRewardVideo", data);
            try {
                assert MsmIntegralFragment.this.getArguments() != null;
                String codeId = MsmIntegralFragment.this.getArguments().getString(REWARD_VIDEO_CODE_ID);
                MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdLoad(context, codeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面播放激励视频广告
         */
        @JavascriptInterface
        public void playToutiaoRewardVideoAd() {
            Log.e("playToutiaoRewardVideo", "");
            MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdPlay(context);
        }

        /**
         * 买什么都省h5页面的信息流广告
         * @param data
         */
        @JavascriptInterface
        public void showToutiaoNativeAd(String data) {
            Log.e("showToutiaoNativeAd", data);
            try {
                //String转JSONObject
                JSONObject result = new JSONObject(data);
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));
                nativeTop = styleObj.getString("top");
                assert MsmIntegralFragment.this.getArguments() != null;
                String codeId = MsmIntegralFragment.this.getArguments().getString(NATIVE_CODE_ID);
                MsmNativeAdLoadHolder.getInstance().nativeAdLoad(context, codeId, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
