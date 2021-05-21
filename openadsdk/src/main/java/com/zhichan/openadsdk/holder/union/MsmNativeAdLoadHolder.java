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

public class MsmNativeAdLoadHolder {

    private final String TAG = this.getClass().getSimpleName();

    private TTAdNative mTTAdNative; // 广告类

    private TTNativeExpressAd mTTAd;

    private NativeAdListener nativeAdListener;

    public void setNativeAdListener(NativeAdListener nativeAdListener) {
        this.nativeAdListener = nativeAdListener;
    }

    private static MsmNativeAdLoadHolder mSingleton = null;
    private MsmNativeAdLoadHolder () {}
    public static MsmNativeAdLoadHolder getInstance() {
        if (mSingleton == null) {
            synchronized (MsmNativeAdLoadHolder.class) {
                if (mSingleton == null) {
                    mSingleton = new MsmNativeAdLoadHolder();
                }
            }
        }
        return mSingleton;
    }

    /**
     * 信息流广告加载方法
     */
    public void nativeAdLoad(final Context context, String codeId, int w, final String top, final String left) {
        mTTAdNative = MsmManagerHolder.get().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(w,0) //必填：期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640,320) //这个参数设置即可，不影响个性化模板广告的size
                .build();
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                nativeAdListener.onNativeError(i, s);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd, context, top, left);
                mTTAd.render();//调用render开始渲染广告
                nativeAdListener.onNativeExpressAdLoad();
            }
        });

    }

    //绑定广告行为
    private void bindAdListener(TTNativeExpressAd ad, Context context, final String top, final String left) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i(TAG, "onAdClicked: " + type);
                nativeAdListener.onNativeAdClicked(view, type);
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i(TAG, "onAdClicked: 广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e(TAG,"render fail: " + code);
                nativeAdListener.onNativeRenderFail(view, msg, code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.e(TAG,"onRenderSuccess: " + width);
                //在渲染成功回调时展示广告，提升体验
//                mExpressContainer.removeAllViews();
//                mExpressContainer.addView(view);
                nativeAdListener.onNativeRenderSuccess(view, width, height, top, left);
            }
        });
        //dislike设置
        bindDislike(context, ad);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
            return;
        }
        //可选，下载监听设置
//        ad.setDownloadListener(new TTAppDownloadListener() {
//            @Override
//            public void onIdle() {
//                TToast.show(NativeExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!mHasShowDownloadActive) {
//                    mHasShowDownloadActive = true;
//                    TToast.show(NativeExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
//                }
//            }
//
//            @Override
//            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onInstalled(String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(NativeExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
//            }
//        });
    }

    /**
     * 设置广告的不喜欢，开发者可自定义样式
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(Context context, TTNativeExpressAd ad) {
        //使用默认个性化模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) context, new TTAdDislike.DislikeInteractionCallback() {

            @Override
            public void onShow() {

            }

            @Override
            public void onSelected(int i, String s, boolean b) {
                Log.e(TAG,"onItemClick: " + s);
                //用户选择不喜欢原因后，移除广告展示
//                mExpressContainer.removeAllViews();
                nativeAdListener.onNativeShield(s);
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"onCancel: 点击取消");
            }

        });
    }

    public void onDestroy() {
        if (mTTAd != null) {
            //调用destroy()方法释放
            mTTAd.destroy();
        }
    }

    public interface NativeAdListener {
        void onNativeExpressAdLoad();
        void onNativeAdClicked(View view, int type);
        void onNativeError(int i, String s);
        void onNativeRenderFail(View view, String msg, int code);
        void onNativeRenderSuccess(View view, float width, float height, String top, String left);
        void onNativeShield(String filter);
    }
}
