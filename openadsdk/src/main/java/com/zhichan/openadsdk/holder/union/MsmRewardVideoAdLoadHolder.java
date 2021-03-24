package com.zhichan.openadsdk.holder.union;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.zhichan.openadsdk.holder.MsmManagerHolder;

/**
 * author : frankylee
 * date : 2020/8/20 2:38 PM
 * description : 激励视频加载帮助类
 */
public class MsmRewardVideoAdLoadHolder {

    private final String TAG = this.getClass().getSimpleName();

    private TTAdNative mTTAdNative; // 广告类

    private TTRewardVideoAd mttRewardVideoAd;

    private RewardVideoAdListener rewardVideoAdListener;

    public void setRewardVideoAdListener(RewardVideoAdListener rewardVideoAdListener) {
        this.rewardVideoAdListener = rewardVideoAdListener;
    }

    /**
     * 激励视频广告加载方法
     */
    public void rewardVideoAdLoad(final Context context, String codeId) {
        mTTAdNative = MsmManagerHolder.get().createAdNative(context);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setAdCount(2)
                //个性化模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可。仅模板广告需要设置此参数
                .setExpressViewAcceptedSize(500,500)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)   //奖励的数量
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                //可设置为空字符串
                .setUserID("13374")
                .setOrientation(TTAdConstant.VERTICAL)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                .setMediaExtra("media_extra") //用户透传的信息，可不传
                .build();
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: " + i + ",msg:" +s);
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onRewardError(i, s);
                }
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                mttRewardVideoAd = ttRewardVideoAd;
                bindAdListener(mttRewardVideoAd);
            }

            @Override
            public void onRewardVideoCached() {
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onRewardVideoCached();
                }
            }
        });

    }

    private void bindAdListener(TTRewardVideoAd ttRewardVideoAd) {
        ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
            @Override
            public void onAdShow() {
                Log.i(TAG, "rewardVideoAd show");
            }

            @Override
            public void onAdVideoBarClick() {
                Log.i(TAG, "rewardVideoAd bar click");
            }

            @Override
            public void onAdClose() {
                Log.i(TAG, "rewardVideoAd close");
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onRewardAdClose();
                }
            }

            @Override
            public void onVideoComplete() {
                Log.i(TAG, "rewardVideoAd complete");
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onRewardVideoComplete();
                }
            }

            @Override
            public void onVideoError() {
                Log.i(TAG, "rewardVideoAd error");
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onRewardVideoError();
                }
            }

            @Override
            public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {
                Log.i(TAG, "verify:"+b+" amount:"+i+
                        " name:"+s);
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onRewardVerify(b, i, s);
                }
            }

            @Override
            public void onSkippedVideo() {
                Log.i(TAG, "onSkippedVideo");
                if (rewardVideoAdListener != null) {
                    rewardVideoAdListener.onSkippedVideo();
                }
            }
        });
    }

    public void onDestroy() {
        if (mttRewardVideoAd != null) {
            //调用destroy()方法释放
            mttRewardVideoAd = null;
        }
    }

    /**
     * 播放激励视频
     */
    public void rewardVideoAdPlay(Context context) {
        if (mttRewardVideoAd != null) {
            mttRewardVideoAd.showRewardVideoAd((Activity) context);
        }
    }

    public interface RewardVideoAdListener {
        void onRewardError(int i, String s);
        void onRewardAdClose();
        void onRewardVideoComplete();
        void onRewardVideoError();
        void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName);
        void onSkippedVideo();
        void onRewardVideoCached();
    }

}
