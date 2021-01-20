package com.zhichan.openadsdk.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.just.agentweb.AgentWeb;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.zhichan.openadsdk.R;
import com.zhichan.openadsdk.holder.AdType;
import com.zhichan.openadsdk.holder.adnet.MsmAdError;
import com.zhichan.openadsdk.holder.adnet.MsmAdnetBannerAdLoadHolder;
import com.zhichan.openadsdk.holder.adnet.MsmAdnetNativeAdLoadHolder;
import com.zhichan.openadsdk.holder.adnet.MsmAdnetRewardVideoAdLoadHolder;
import com.zhichan.openadsdk.holder.union.MsmBannerAdLoadHolder;
import com.zhichan.openadsdk.holder.union.MsmNativeAdLoadHolder;
import com.zhichan.openadsdk.holder.union.MsmRewardVideoAdLoadHolder;

import org.json.JSONObject;

import java.util.Map;

public class MsmUniversalFragment extends AgentWebFragment implements
        MsmBannerAdLoadHolder.BannerAdListener,
        MsmRewardVideoAdLoadHolder.RewardVideoAdListener,
        MsmNativeAdLoadHolder.NativeAdListener,
        MsmAdnetBannerAdLoadHolder.BannerAdnetAdListener,
        MsmAdnetRewardVideoAdLoadHolder.RewardVideoAdnetListener,
        MsmAdnetNativeAdLoadHolder.NativeAdnetAdListener

{

    public static MsmUniversalFragment getInstance(Bundle bundle) {

        MsmUniversalFragment msmIntegralFragment = new MsmUniversalFragment();
        if (msmIntegralFragment != null) {
            // 穿山甲广告监听
            MsmRewardVideoAdLoadHolder.getInstance().setRewardVideoAdListener(msmIntegralFragment);
            MsmBannerAdLoadHolder.getInstance().setBannerAdListener(msmIntegralFragment);
            MsmNativeAdLoadHolder.getInstance().setNativeAdListener(msmIntegralFragment);

            // 广点通广告监听
            MsmAdnetRewardVideoAdLoadHolder.getInstance().setRewardVideoAdnetAdListener(msmIntegralFragment);
            MsmAdnetBannerAdLoadHolder.getInstance().setBannerAdnetAdListener(msmIntegralFragment);
            MsmAdnetNativeAdLoadHolder.getInstance().setNativeAdnetAdListener(msmIntegralFragment);

            msmIntegralFragment.setArguments(bundle);
        }

        return msmIntegralFragment;
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
                .setSecurityType(AgentWeb.SecurityType.DEFAULT_CHECK)
                .createAgentWeb()//
                .ready()//
                .go(getUrl());


        initView(view);

        mAgentWeb.getWebCreator().getWebView().addJavascriptInterface(new MyJavascriptInterface(this.getActivity()), "msmdsInjected");
    }

    // ======================== 穿山甲Banner广告监听 ==================//
    @Override
    public void onError(int i, String s) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadError();");
    }

    @Override
    public void onRenderFail(View view, String msg, int code) {

    }

    @Override
    public void onRenderSuccess(View view, float width, float height, String top, String left) {
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
    // ======================== 穿山甲Banner广告监听 ==================//

    // ======================== 广点通Banner广告监听 ==================//
    @Override
    public void onNoAD(MsmAdError error) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadError();");
    }

    @Override
    public void onADReceive(View view, int w, float scale, String top, String left) {
        view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(top)));
        view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(left)));
        View adV = adViews.get("bannerAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().addView(view);
        adViews.put("bannerAd", view);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadSuccess({height:"+(w/scale)+"});");
    }

    @Override
    public void onADExposure() {

    }

    @Override
    public void onADClosed() {
        Log.i(TAG, "onADClosed: " + mAgentWeb.getWebCreator().getWebView().getChildCount());
        View adV = adViews.get("bannerAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onHideManually();");
    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADLeftApplication() {

    }

    @Override
    public void onADOpenOverlay() {

    }

    @Override
    public void onADCloseOverlay() {

    }
    // ======================== 广点通Banner广告监听 ==================//

    // ======================== 穿山甲激励视频广告监听 ==================//
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
    // ======================== 穿山甲激励视频广告监听 ==================//

    // ======================== 广点通激励视频广告监听 ==================//
    @Override
    public void onAdLoaded() {

    }

    @Override
    public void onVideoCached() {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onExpose() {

    }

    @Override
    public void onReward(Map<String, Object> map) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.playToutiaoRewardVideoAd.onRewardVerify();");
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onVideoComplete() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onError(MsmAdError error) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.playToutiaoRewardVideoAd.onRewardError(" + error.getErrorCode() + "," + error.getErrorMsg() + ");");
    }
    // ======================== 广点通激励视频广告监听 ==================//

    // ======================== 穿山甲信息流广告监听 ==================//
    @Override
    public void onNativeError(int i, String s) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeError(" + i + "," + s + ");");
    }

    @Override
    public void onNativeRenderFail(View view, String msg, int code) {

    }

    @Override
    public void onNativeRenderSuccess(View view, float width, float height, String top, String left) {
        view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(top)));
        view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(left)));
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
    // ======================== 穿山甲信息流广告监听 ==================//

    // ======================== 广点通信息流广告监听 ==================//
    @Override
    public void onNativeNoAD(MsmAdError error) {
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeError(" + error.getErrorCode() + "," + error.getErrorMsg() + ");");
    }

    @Override
    public void onADLoaded(View view, String top, String left) {
        view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(top)));
        view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(left)));
        View adV = adViews.get("nativeAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().addView(view);
        adViews.put("nativeAd", view);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeRenderSuccess();");
    }

    @Override
    public void onADClicked(View nv) {

    }

    @Override
    public void onADExposure(View nv) {

    }

    @Override
    public void onRenderSuccess(View nv) {

    }

    @Override
    public void onRenderFail(View nv) {

    }

    @Override
    public void onADClosed(View nv) {
        View adV = adViews.get("nativeAd");
        mAgentWeb.getWebCreator().getWebView().removeView(adV);
        mAgentWeb.getWebCreator().getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeShield();");
    }

    @Override
    public void onADCloseOverlay(View nv) {

    }

    @Override
    public void onADLeftApplication(View nv) {

    }

    @Override
    public void onADOpenOverlay(View nv) {

    }
    // ======================== 广点通信息流广告监听 ==================//


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

                // js传递的广告宽度和位置
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));
                float scale = styleObj.has("scale") ? Float.parseFloat(styleObj.getString("scale")) : 1;
                String top = styleObj.has("top") ? styleObj.getString("top") : "0";
                String left = styleObj.has("left") ? styleObj.getString("left") : "0";

                // 广告位ID和广告类型（穿山甲或者广点通）
                String codeID = result.getString("androidCodeId");
                String adType = result.getString("adType");
                int refreshInterval = result.getInt("refreshInterval");
                AdType type = AdType.fromTypeName(adType);
                // 广告加载
                switch (type) {
                    case ADNET:
                        MsmAdnetBannerAdLoadHolder.getInstance().bannerAdLoad(
                                (Activity) context,
                                codeID,
                                refreshInterval,
                                width,
                                scale,
                                top,
                                left
                        );
                        break;
                    case UNION:
                        MsmBannerAdLoadHolder.getInstance().bannerAdLoad(
                                context,
                                codeID,
                                (int)(width*scale),
                                (int)(height*scale),
                                top,
                                left);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面关闭banner广告
         */
        @JavascriptInterface
        public void dismissToutiaoBannerAd() {
            MsmUniversalFragment.this.getActivity().runOnUiThread(new Runnable() {
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
        public void loadToutiaoRewardVideoAd(String data) {
            try {
                Log.i(TAG, "loadToutiaoRewardVideoAd: --------------");
                //String转JSONObject
                JSONObject result = new JSONObject(data);
                // 广告位ID和广告类型（穿山甲或者广点通）
                String codeID = result.getString("androidCodeId");
                String adType = result.getString("adType");
                AdType type = AdType.fromTypeName(adType);
                switch (type) {
                    case ADNET:
                        MsmAdnetRewardVideoAdLoadHolder.getInstance().rewardAdnetAdLoad((Activity) context, codeID);
                        break;
                    case UNION:
                        MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdLoad(context, codeID);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面播放激励视频广告
         */
        @JavascriptInterface
        public void playToutiaoRewardVideoAd(String data) {
            try {
                Log.e(TAG, "playToutiaoRewardVideoAd: ---------");
                //String转JSONObject
                JSONObject result = new JSONObject(data);
                // 广告位ID和广告类型（穿山甲或者广点通）
                String adType = result.getString("adType");
                AdType type = AdType.fromTypeName(adType);
                switch (type) {
                    case UNION:
                        MsmUniversalFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdPlay(context);
                            }
                        });
                        break;
                    case ADNET:
                        MsmUniversalFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MsmAdnetRewardVideoAdLoadHolder.getInstance().rewardVideoAdPlay((Activity) context);
                            }
                        });
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

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
                // 广告宽高和位置
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));
                String nativeTop = styleObj.has("top") ? styleObj.getString("top") : "0";
                String  nativeLeft = styleObj.has("left") ? styleObj.getString("left") : "0";

                // 广告位ID和广告类型（穿山甲或者广点通）
                String codeID = result.getString("androidCodeId");
                String adType = result.getString("adType");
                AdType type = AdType.fromTypeName(adType);
                switch (type) {
                    case ADNET:
                        MsmAdnetNativeAdLoadHolder.getInstance().nativeAdLoad(
                                (Activity) context,
                                codeID,
                                width,
                                nativeTop,
                                nativeLeft
                        );
                        break;
                    case UNION:
                        MsmNativeAdLoadHolder.getInstance().nativeAdLoad(
                                context,
                                codeID,
                                width,
                                nativeTop,
                                nativeLeft
                        );
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面关闭信息流广告
         */
        @JavascriptInterface
        public void dismissToutiaoNativeAd() {
            MsmUniversalFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View adV = adViews.get("nativeAd");
                    mAgentWeb.getWebCreator().getWebView().removeView(adV);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAgentWeb.getWebCreator().getWebView() != null) {
            MsmBannerAdLoadHolder.getInstance().onDestroy();
            MsmNativeAdLoadHolder.getInstance().onDestroy();
            MsmAdnetBannerAdLoadHolder.getInstance().onDestroy();
            MsmAdnetNativeAdLoadHolder.getInstance().onDestroy();
        }
    }
}
