package com.zhichan.openadsdk.holder.adnet;

import android.app.Activity;
import android.view.View;

import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * author : frankylee
 * date : 2021/1/15 9:47 AM
 * description : Native广告，暂时只支持图文且不支持双图双文模版
 */
public class MsmAdnetNativeAdLoadHolder {

    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;

    private NativeAdnetAdListener nativeAdnetAdListener;

    private String top;
    private String left;

    public void setNativeAdnetAdListener(NativeAdnetAdListener nativeAdnetAdListener) {
        this.nativeAdnetAdListener = nativeAdnetAdListener;
    }

    private static MsmAdnetNativeAdLoadHolder mSingleton = null;
    private MsmAdnetNativeAdLoadHolder () {}
    public static MsmAdnetNativeAdLoadHolder getInstance() {
        if (mSingleton == null) {
            synchronized (MsmAdnetNativeAdLoadHolder.class) {
                if (mSingleton == null) {
                    mSingleton = new MsmAdnetNativeAdLoadHolder();
                }
            }
        }
        return mSingleton;
    }

    /**
     * native广告加载，使用外部容器接受广告view
     * @param activity
     * @param codeId
     * @param w
     */
    public void nativeAdLoad(Activity activity, String codeId, int w, String top, String left) {
        try {
            this.top = top;
            this.left = left;
            // 广告的size，宽度由外部传递，高度自适应
            ADSize adSize = new ADSize(w, ADSize.AUTO_HEIGHT);
            // 广告加载对象
            nativeExpressAD = new NativeExpressAD(activity, adSize, codeId, expressADListener);
            nativeExpressAD.loadAD(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 自定义广告监听
    private NativeExpressAD.NativeExpressADListener expressADListener = new NativeExpressAD.NativeExpressADListener() {
        @Override
        public void onADLoaded(List<NativeExpressADView> list) {
            // 释放前一个展示的NativeExpressADView的资源
            if (nativeExpressADView != null) {
                nativeExpressADView.destroy();
            }
            if (list.size() > 0) {
                nativeExpressADView = list.get(0);
                nativeExpressADView.render();
                if (nativeAdnetAdListener != null) {
                    nativeAdnetAdListener.onADLoaded(nativeExpressADView, top, left);
                }
            }
        }

        @Override
        public void onRenderFail(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onRenderFail(nativeExpressADView);
            }
        }

        @Override
        public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onRenderSuccess(nativeExpressADView);
            }
        }

        @Override
        public void onADExposure(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onADExposure(nativeExpressADView);
            }
        }

        @Override
        public void onADClicked(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onADClicked(nativeExpressADView);
            }
        }

        @Override
        public void onADClosed(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onADClosed(nativeExpressADView);
            }
        }

        @Override
        public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onADLeftApplication(nativeExpressADView);
            }
        }

        @Override
        public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onADOpenOverlay(nativeExpressADView);
            }
        }

        @Override
        public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
            if (nativeAdnetAdListener != null) {
                nativeAdnetAdListener.onADCloseOverlay(nativeExpressADView);
            }
        }

        @Override
        public void onNoAD(AdError adError) {
            if (nativeAdnetAdListener != null) {
                MsmAdError msmAdError = new MsmAdError(adError.getErrorCode(), adError.getErrorMsg());
                nativeAdnetAdListener.onNativeNoAD(msmAdError);
            }
        }
    };

    public void onDestroy() {
        // 使用完了每一个NativeExpressADView之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
    }

    public interface NativeAdnetAdListener {
        void onADLoaded(View nv, String top, String left);
        void onNativeNoAD(MsmAdError error);
        void onRenderFail(View nv);
        void onRenderSuccess(View nv);
        void onADExposure(View nv);
        void onADClicked(View nv);
        void onADClosed(View nv);
        void onADLeftApplication(View nv);
        void onADOpenOverlay(View nv);
        void onADCloseOverlay(View nv);
    }
}
