package com.zhichan.openadsdk.holder.adnet;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;


/**
 * author : frankylee
 * date : 2021/1/13 11:36 AM
 * description : 广点通Banner广告帮助类
 */
public class MsmAdnetBannerAdLoadHolder {

    private UnifiedBannerView bv;

    private BannerAdnetAdListener bannerAdnetAdListener;

    private int adViewWidth;

    private float scale;

    public void setBannerAdnetAdListener(BannerAdnetAdListener bannerAdnetAdListener) {
        this.bannerAdnetAdListener = bannerAdnetAdListener;
    }

    private static MsmAdnetBannerAdLoadHolder mSingleton = null;
    private MsmAdnetBannerAdLoadHolder () {}
    public static MsmAdnetBannerAdLoadHolder getInstance() {
        if (mSingleton == null) {
            synchronized (MsmAdnetBannerAdLoadHolder.class) {
                if (mSingleton == null) {
                    mSingleton = new MsmAdnetBannerAdLoadHolder();
                }
            }
        }
        return mSingleton;
    }

    /**
     * banner广告加载，使用外部容器接受广告view
     * @param activity
     * @param container
     * @param codeId
     * @param refreshInterval
     * @param w
     * @param scale
     */
    public void bannerAdLoad(Activity activity, String codeId, int refreshInterval, int w, float scale) {

        if(this.bv != null){
            bv.destroy();
        }
        this.adViewWidth = w;
        this.scale = scale;
        this.bv = new UnifiedBannerView(activity, codeId, bannerADListener);
        this.bv.setRefresh(refreshInterval);
        this.bv.loadAD();
    }

    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams(int w, float scale) {
        return new FrameLayout.LayoutParams(w,  Math.round(w / scale));
    }

    public void onDestroy() {
        if (bv != null) {
            bv.destroy();
            bv = null;
        }
    }

    // 自定义广告监听
    private UnifiedBannerADListener bannerADListener = new UnifiedBannerADListener() {

        @Override
        public void onNoAD(AdError adError) {
            if (bannerAdnetAdListener != null) {
                MsmAdError msmAdError = new MsmAdError(adError.getErrorCode(), adError.getErrorMsg());
                bannerAdnetAdListener.onNoAD(msmAdError);
            }
        }

        @Override
        public void onADReceive() {
            Log.i("onADReceive", "onADReceive: banner 广告加载成功");
            if (bannerAdnetAdListener != null) {
                bannerAdnetAdListener.onADReceive(bv, adViewWidth, scale);
            }
        }

        @Override
        public void onADExposure() {
            Log.i("onADReceive", "onADReceive: banner 广告渲染成功");
            if (bannerAdnetAdListener != null) {
                bannerAdnetAdListener.onADExposure();
            }
        }

        @Override
        public void onADClosed() {
            if (bannerAdnetAdListener != null) {
                if (bv != null) {
                    bv.destroy();
                    bv = null;
                }
                bannerAdnetAdListener.onADClosed();
            }
        }

        @Override
        public void onADClicked() {
            if (bannerAdnetAdListener != null) {
                bannerAdnetAdListener.onADClicked();
            }
        }

        @Override
        public void onADLeftApplication() {
            if (bannerAdnetAdListener != null) {
                bannerAdnetAdListener.onADLeftApplication();
            }
        }

        @Override
        public void onADOpenOverlay() {
            if (bannerAdnetAdListener != null) {
                bannerAdnetAdListener.onADOpenOverlay();
            }
        }

        @Override
        public void onADCloseOverlay() {
            if (bannerAdnetAdListener != null) {
                bannerAdnetAdListener.onADCloseOverlay();
            }
        }
    };

    public interface BannerAdnetAdListener {
        void onNoAD(MsmAdError error);
        void onADReceive(View view, int w, float scale);
        void onADExposure();
        void onADClosed();
        void onADClicked();
        void onADLeftApplication();
        void onADOpenOverlay();
        void onADCloseOverlay();
    }

}
