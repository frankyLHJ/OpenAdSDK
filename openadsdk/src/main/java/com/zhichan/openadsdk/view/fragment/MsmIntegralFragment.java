package com.zhichan.openadsdk.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.just.agentweb.AgentWeb;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.zhichan.openadsdk.R;
import com.zhichan.openadsdk.holder.MsmBannerAdLoadHolder;
import com.zhichan.openadsdk.holder.MsmNativeAdLoadHolder;
import com.zhichan.openadsdk.holder.MsmRewardVideoAdLoadHolder;

import org.json.JSONObject;

public class MsmIntegralFragment extends AgentWebFragment implements
        MsmBannerAdLoadHolder.BannerAdListener,
        MsmRewardVideoAdLoadHolder.RewardVideoAdListener,
        MsmNativeAdLoadHolder.NativeAdListener
{
    public static final String APP_ID = "APP_ID";
    public static final String BANNER_CODE_ID = "banner_code_id";
    public static final String NATIVE_CODE_ID = "native_code_id";
    public static final String REWARD_VIDEO_CODE_ID = "reward_video_code_id";

    private String top = "0";
    private String nativeTop = "0";

    private String left = "0";
    private String nativeLeft = "0";

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

//    @Override
//    public String getUrl() {
//        return "http://192.168.0.222:8080/#/test";
//    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((ViewGroup) view.findViewById(R.id.web_layout), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(-1, 2)
                .setAgentWebWebSettings(getSettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setSecurityType(AgentWeb.SecurityType.DEFAULT_CHECK)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());


        initView(view);

        mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new MyJavascriptInterface(this.getActivity()), "msmdsInjected");

        try {
            //获取包管理器
            PackageManager pm = this.getContext().getPackageManager();
            //获取包信息
            PackageInfo packageInfo = pm.getPackageInfo(this.getContext().getPackageName(), 0);
            //返回版本号
            String version = packageInfo.versionName;
            String platform = "android";
            assert MsmIntegralFragment.this.getArguments() != null;
            String appId = MsmIntegralFragment.this.getArguments().getString(APP_ID);
            boolean showToolbar = MsmIntegralFragment.this.getArguments().getBoolean(SHOW_TOOLBAR);
            mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.sdkInfo={" +
                    "version:" + version + "," +
                    "platform" + platform + "," +
                    "appId" + appId + "," +
                    "showToolbar" + showToolbar +
                    "};");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(left)));
        View adV = adViews.get("bannerAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().addView(view);
        adViews.put("bannerAd", view);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadSuccess({height:"+height+"});");

    }

    @Override
    public void onShield(String filter) {
        Log.i(TAG, "onShield: " + mAgentWeb.getWebCreator().getWebView().getChildCount());
        View adV = adViews.get("bannerAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onHideManually();");
    }
    // ======================== Banner广告监听 ==================//

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAgentWeb.getWebCreator().getWebView() != null) {
            MsmBannerAdLoadHolder.getInstance().onDestroy();
            MsmNativeAdLoadHolder.getInstance().onDestroy();
        }
    }

    // ======================== 激励视频广告监听 ==================//
    @Override
    public void onRewardError(int i, String s) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.playToutiaoRewardVideoAd.onRewardError(" + i + "," + s + ");");
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
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.playToutiaoRewardVideoAd.onRewardVerify();");
    }

    @Override
    public void onSkippedVideo() {

    }
    // ======================== 激励视频广告监听 ==================//

    // ======================== 信息流广告监听 ==================//
    @Override
    public void onNativeError(int i, String s) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeError(" + i + "," + s + ");");
    }

    @Override
    public void onNativeRenderFail(View view, String msg, int code) {

    }

    @Override
    public void onNativeRenderSuccess(View view, float width, float height) {
        view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(nativeTop)));
        view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(nativeLeft)));
        View adV = adViews.get("nativeAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().addView(view);
        adViews.put("nativeAd", view);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeRenderSuccess();");
    }

    @Override
    public void onNativeShield(String filter) {
        View adV = adViews.get("nativeAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeShield();");
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
                top = styleObj.has("top") ? styleObj.getString("top") : "0";
                left = styleObj.has("left") ? styleObj.getString("left") : "0";
                assert MsmIntegralFragment.this.getArguments() != null;
                String codeId = MsmIntegralFragment.this.getArguments().getString(BANNER_CODE_ID);
                MsmBannerAdLoadHolder.getInstance().bannerAdLoad(context, codeId, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面关闭banner广告
         */
        @JavascriptInterface
        public void dismissToutiaoBannerAd() {
            MsmIntegralFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View adV = adViews.get("bannerAd");
                    mAgentWeb.getWebCreator().getWebView().removeView(adV);
                }
            });
        }

        /**
         * 买什么都省h5页面加载激励视频广告
         */
        @JavascriptInterface
        public void loadToutiaoRewardVideoAd() {
            try {
                Log.i(TAG, "loadToutiaoRewardVideoAd: --------------");
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
            Log.e(TAG, "playToutiaoRewardVideoAd: ---------");
            MsmIntegralFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdPlay(context);
                }
            });
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
                nativeTop = styleObj.has("top") ? styleObj.getString("top") : "0";
                nativeLeft = styleObj.has("left") ? styleObj.getString("left") : "0";
                assert MsmIntegralFragment.this.getArguments() != null;
                String codeId = MsmIntegralFragment.this.getArguments().getString(NATIVE_CODE_ID);
                MsmNativeAdLoadHolder.getInstance().nativeAdLoad(context, codeId, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面关闭信息流广告
         */
        @JavascriptInterface
        public void dismissToutiaoNativeAd() {
            MsmIntegralFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View adV = adViews.get("nativeAd");
                    mAgentWeb.getWebCreator().getWebView().removeView(adV);
                }
            });
        }
    }


}
