package com.zhichan.openadsdk.holder.union;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.PersonalizationPrompt;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.zhichan.openadsdk.holder.MsmManagerHolder;
import com.zhichan.openadsdk.view.dialog.DislikeDialog;

import java.util.List;

public class MsmBannerAdLoadHolder {

    private final String TAG = this.getClass().getSimpleName();

    private TTAdNative mTTAdNative; // 广告类

    private TTNativeExpressAd mTTAd;

    private BannerAdListener bannerAdListener;

    public void setBannerAdListener(BannerAdListener bannerAdListener) {
        this.bannerAdListener = bannerAdListener;
    }

    private static MsmBannerAdLoadHolder mSingleton = null;
    private MsmBannerAdLoadHolder () {}
    public static MsmBannerAdLoadHolder getInstance() {
        if (mSingleton == null) {
            synchronized (MsmBannerAdLoadHolder.class) {
                if (mSingleton == null) {
                    mSingleton = new MsmBannerAdLoadHolder();
                }
            }
        }
        return mSingleton;
    }

    /**
     * Banner广告加载方法
     */
    public void bannerAdLoad(final Context context, String codeId, int w, int h, final String top, final String left) {
        mTTAdNative = MsmManagerHolder.get().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(w,h) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640,320 )//这个参数设置即可，不影响个性化模板广告的size
                .build();
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                bannerAdListener.onError(i, s);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAd = ads.get(0);
//                mTTAd.setSlideIntervalTime(30*1000);//设置轮播间隔 ms,不调用则不进行轮播展示
                bindAdListener(mTTAd, context, top, left);
                mTTAd.render();//调用render开始渲染广告
                bannerAdListener.onBannerExpressAdLoad();
            }
        });

    }

    //绑定广告行为
    private void bindAdListener(TTNativeExpressAd ad, Context context, final String top, final String left) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i(TAG, "onAdClicked: " + type);
                bannerAdListener.onBannerAdClicked(view, type);
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i(TAG, "onAdClicked: 广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e(TAG,"render fail: " + code);
                bannerAdListener.onRenderFail(view, msg, code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.e(TAG,"onRenderSuccess: " + width);
                //在渲染成功回调时展示广告，提升体验
//                mExpressContainer.removeAllViews();
//                mExpressContainer.addView(view);
                bannerAdListener.onRenderSuccess(view, width, height, top, left);

            }
        });
        //dislike设置
        bindDislike(context, ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
            return;
        }
        //可选，下载监听设置
//        ad.setDownloadListener(new TTAppDownloadListener() {
//            @Override
//            public void onIdle() {
//                Log.e(TAG,"onIdle: 点击开始下载");
//            }
//
//            @Override
//            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                Log.e(TAG,"onDownloadActive: 下载中，点击暂停");
//            }
//
//            @Override
//            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                Log.e(TAG,"onDownloadPaused: 下载暂停，点击继续");
//            }
//
//            @Override
//            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                Log.e(TAG,"onDownloadFailed: 下载失败，点击重新下载");
//            }
//
//            @Override
//            public void onInstalled(String fileName, String appName) {
//                Log.e(TAG,"onInstalled: 安装完成，点击图片打开");
//            }
//
//            @Override
//            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                Log.e(TAG,"onDownloadFinished: 点击安装");
//            }
//        });
    }

    /**
     * 设置广告的不喜欢，开发者可自定义样式
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(Context context, TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            PersonalizationPrompt personalizationPrompt = ad.getPersonalizationPrompt();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(context, words, personalizationPrompt);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    Log.e(TAG,"onItemClick: " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
//                    mExpressContainer.removeAllViews();
                    bannerAdListener.onShield(filterWord.getName());
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认个性化模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) context, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.e(TAG,"onItemClick: " + value);
                //用户选择不喜欢原因后，移除广告展示
//                mExpressContainer.removeAllViews();
                bannerAdListener.onShield(value);
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"onCancel: 点击取消");
            }

            @Override
            public void onRefuse() {
                Log.e(TAG,"onRefuse: 回收");
            }
        });
    }

    public void onDestroy() {
        if (mTTAd != null) {
            //调用destroy()方法释放
            mTTAd.destroy();
        }
    }

    public interface BannerAdListener {
        void onBannerExpressAdLoad();
        void onBannerAdClicked(View view, int type);
        void onError(int i, String s);
        void onRenderFail(View view, String msg, int code);
        void onRenderSuccess(View view, float width, float height, String top, String left);
        void onShield(String filter);
    }
}
