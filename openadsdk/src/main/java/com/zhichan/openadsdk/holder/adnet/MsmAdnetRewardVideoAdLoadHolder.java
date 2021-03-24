package com.zhichan.openadsdk.holder.adnet;

import android.app.Activity;
import android.os.SystemClock;

import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAdListener;
import com.qq.e.comm.util.AdError;

import java.util.Map;

/**
 * author : frankylee
 * date : 2021/1/15 11:17 AM
 * description :
 */
public class MsmAdnetRewardVideoAdLoadHolder {

    private RewardVideoAD rewardVideoAD;

    private RewardVideoAdnetListener rewardVideoAdnetListener;

    private boolean adLoaded;//广告加载成功标志

    public void setRewardVideoAdnetAdListener(RewardVideoAdnetListener rewardVideoAdnetAdListener) {
        this.rewardVideoAdnetListener = rewardVideoAdnetAdListener;
    }

    /**
     * 激励视频广告加载
     * @param activity
     * @param codeId
     */
    public void rewardAdnetAdLoad(Activity activity, String codeId) {
        try {
            adLoaded = false;
            rewardVideoAD = new RewardVideoAD(activity, codeId, expressRewardVideoAdListener, false);
            rewardVideoAD.loadAD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放激励视频广告
     * 推荐加载完成后再播放，提升用户体验
     * @param activity
     */
    public void rewardVideoAdPlay(Activity activity) {
        //广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
        MsmAdError msmAdError;
        if (rewardVideoAD == null || !adLoaded) {
            if (rewardVideoAdnetListener != null) {
                msmAdError = new MsmAdError(900001, "视频未准备好");
                rewardVideoAdnetListener.onError(msmAdError);
            }
            return;
        }
        //广告展示检查2：当前广告数据还没有展示过
        if (rewardVideoAD.hasShown()) {
            if (rewardVideoAdnetListener != null) {
                msmAdError = new MsmAdError(900002, "视频已经播放过");
                rewardVideoAdnetListener.onError(msmAdError);
            }
            return;
        }

        //广告展示检查3：展示广告前判断广告数据未过期
        long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里采用1000ms的buffer
        if (SystemClock.elapsedRealtime() >= (rewardVideoAD.getExpireTimestamp() - delta)) {
            if (rewardVideoAdnetListener != null) {
                msmAdError = new MsmAdError(900003, "视频已经过期");
                rewardVideoAdnetListener.onError(msmAdError);
            }
            return;
        }

        // 展示广告
        rewardVideoAD.showAD(activity);
    }

    private RewardVideoADListener expressRewardVideoAdListener = new RewardVideoADListener() {

        /**
         * 广告加载成功，可在此回调后进行广告展示
         **/
        @Override
        public void onADLoad() {
            adLoaded = true;
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onAdLoaded();
            }
        }

        /**
         * 视频素材缓存成功，可在此回调后进行广告展示
         */
        @Override
        public void onVideoCached() {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onVideoCached();
            }
        }

        /**
         * 激励视频广告页面展示
         */
        @Override
        public void onADShow() {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onShow();
            }
        }

        /**
         * 激励视频广告曝光
         */
        @Override
        public void onADExpose() {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onExpose();
            }
        }

        /**
         * 激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
         */
        @Override
        public void onReward(Map<String, Object> map) {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onReward(map);
            }
        }

        /**
         * 激励视频广告被点击
         */
        @Override
        public void onADClick() {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onClick();
            }
        }

        /**
         * 激励视频播放完毕
         */
        @Override
        public void onVideoComplete() {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onVideoComplete();
            }
        }

        /**
         * 激励视频广告被关闭
         */
        @Override
        public void onADClose() {
            if (rewardVideoAdnetListener != null) {
                rewardVideoAdnetListener.onClose();
            }
        }

        /**
         * 广告流程出错
         */
        @Override
        public void onError(AdError adError) {
            if (rewardVideoAdnetListener != null) {
                MsmAdError msmAdError = new MsmAdError(adError.getErrorCode(), adError.getErrorMsg());
                rewardVideoAdnetListener.onError(msmAdError);
            }
        }
    };

    public void onDestroy() {
        if (rewardVideoAD != null) {
            //调用destroy()方法释放
            rewardVideoAD = null;
        }
    }

    public interface RewardVideoAdnetListener {
        void onAdLoaded();
        void onVideoCached();
        void onShow();
        void onExpose();
        void onReward(Map<String, Object> map);
        void onClick();
        void onVideoComplete();
        void onClose();
        void onError(MsmAdError error);
    }
}
