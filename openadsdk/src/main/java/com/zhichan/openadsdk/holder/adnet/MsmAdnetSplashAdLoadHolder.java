package com.zhichan.openadsdk.holder.adnet;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.constants.LoadAdParams;
import com.qq.e.comm.util.AdError;

/**
 * author : frankylee
 * date : 2021/1/12 2:52 PM
 * description : 开屏广告帮助类
 * 实现SplashADZoomOutListener是为了支持V+广告，普通只需要实现SplashADListener即可
 */
public class MsmAdnetSplashAdLoadHolder {

    private SplashAD splashAD;

    private boolean loadAdOnly = false;

    private SplashAdnetAdListener splashAdListener;

    public void setSplashAdnetAdListener(SplashAdnetAdListener splashAdListener) {
        this.splashAdListener = splashAdListener;
    }

    private static MsmAdnetSplashAdLoadHolder mSingleton = null;
    private MsmAdnetSplashAdLoadHolder () {}
    public static MsmAdnetSplashAdLoadHolder getInstance() {
        if (mSingleton == null) {
            synchronized (MsmAdnetSplashAdLoadHolder.class) {
                if (mSingleton == null) {
                    mSingleton = new MsmAdnetSplashAdLoadHolder();
                }
            }
        }
        return mSingleton;
    }

    /**
     * 拉取开屏广告并且显示
     *
     * @param activity        展示广告的activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的
     *                        样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param posId           广告位ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    public void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String posId, int fetchDelay) {
        splashAD = new SplashAD(activity, skipContainer, posId, splashADListener, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }

    /**
     * 只拉取开屏广告
     *
     * @param activity        展示广告的activity
     * @param adContainer     展示广告的大容器
     * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式
     *                        可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param posId           广告位ID
     * @param adListener      广告状态监听器
     * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     */
    private void loadAdOnly(Activity activity, View skipContainer,
                               String posId, int fetchDelay) {
        splashAD = new SplashAD(activity, skipContainer, posId, splashADListener, fetchDelay);
        splashAD.fetchAdOnly();
    }

    /**
     * 显示广告，跟loadAdOnly方法搭配，建议使用fetchSplashAD方法，因为loadAdOnly方法加载的广告有过期时间
     * 如果过期未展示会影响曝光和收益
     */
    private void showAd(ViewGroup adContainer) {
        if (splashAD != null) {
            splashAD.showAd(adContainer);
        }
    }

    /**
     * 预加载广告(当前只支持预加载合约广告)，调用后开屏广告会尝试进行广告数据和图片/视频素材的预加载（其中视频素材
     * 默认仅wifi下预加载，若视频广告的视频素材未能提前预加载，在得到播放机会时将退化为图片广告进行播放），预加载的
     * 广告在得到展示机会时可以跳过广告素材下载的步骤而直接展示。
     *
     * 目前优量汇的开屏广告还不支持开发者做预加载，现在都是竞价广告，
     * 预加载功能只能在合约广告中使用，现在没有合约类型
     */
    private void preLoad(Activity activity,String posId) {
        splashAD = new SplashAD(activity, posId, splashADListener);
        LoadAdParams params = new LoadAdParams();
        splashAD.setLoadAdParams(params);
        splashAD.preLoad();
    }

    private SplashADListener splashADListener = new SplashADListener() {
        @Override
        public void onADDismissed() {
            if (splashAdListener != null) {
                splashAdListener.onADDismissed();
            }
        }

        @Override
        public void onNoAD(AdError adError) {
            if (splashAdListener != null) {
                MsmAdError msmAdError = new MsmAdError(adError.getErrorCode(), adError.getErrorMsg());
                splashAdListener.onNoAD(msmAdError);
            }
        }

        @Override
        public void onADPresent() {
            if (splashAdListener != null) {
                splashAdListener.onADPresent();
            }
        }

        @Override
        public void onADClicked() {
            if (splashAdListener != null) {
                splashAdListener.onADClicked();
            }
        }

        @Override
        public void onADTick(long l) {
            if (splashAdListener != null) {
                splashAdListener.onADTick(l);
            }
        }

        @Override
        public void onADExposure() {
            if (splashAdListener != null) {
                splashAdListener.onADExposure();
            }
        }

        @Override
        public void onADLoaded(long l) {
            if (splashAdListener != null) {
                splashAdListener.onADLoaded(l);
            }
        }
    };

    public interface SplashAdnetAdListener {
        void onADPresent();
        void onADClicked();
        void onADTick(long millisUntilFinished);
        void onADExposure();
        void onADLoaded(long expireTimestamp);
        void onADDismissed();
        void onNoAD(MsmAdError error);
    }
}
