package com.zhichan.openadsdk.holder;

import android.content.Context;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;

/**
 * 开屏广告加载帮助类
 */

public class MsmAdLoadHolder {

    private TTAdNative mTTAdNative; // 广告类

    private SplashAdListener splashAdListener;

    public void setSplashAdListener(SplashAdListener splashAdListener) {
        this.splashAdListener = splashAdListener;
    }

    private static MsmAdLoadHolder mSingleton = null;
    private MsmAdLoadHolder () {}
    public static MsmAdLoadHolder getInstance() {
        if (mSingleton == null) {
            synchronized (MsmAdLoadHolder.class) {
                if (mSingleton == null) {
                    mSingleton = new MsmAdLoadHolder();
                }
            }
        }
        return mSingleton;
    }

    /**
     * 开屏广告加载方法
     */
    public void splashAdLoad(Context context, String codeId, int w, int h, final int timeOut, final boolean isNotAllowSdkCountdown) {
        mTTAdNative = MsmManagerHolder.get().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(w, h)
                .build();
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                splashAdListener.onError(i, s);
            }

            @Override
            public void onTimeout() {
                splashAdListener.onTimeout();
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                View view = ttSplashAd.getSplashView();
                splashAdListener.onLoadSuccess(view);
                if (isNotAllowSdkCountdown) {
                    ttSplashAd.setNotAllowSdkCountdown();
                }

                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {
                        splashAdListener.onAdClicked(view, i);
                    }

                    @Override
                    public void onAdShow(View view, int i) {
                        splashAdListener.onAdShow(view, i);
                    }

                    @Override
                    public void onAdSkip() {
                        splashAdListener.onAdSkip();
                    }

                    @Override
                    public void onAdTimeOver() {
                        splashAdListener.onAdTimeOver();
                    }
                });
            }
        }, timeOut);
    }

    public interface SplashAdListener {
        void onError(int i, String s);
        void onTimeout();
        void onLoadSuccess(View view);
        void onAdClicked(View view, int i);
        void onAdShow(View view, int i);
        void onAdSkip();
        void onAdTimeOver();
    }

}
