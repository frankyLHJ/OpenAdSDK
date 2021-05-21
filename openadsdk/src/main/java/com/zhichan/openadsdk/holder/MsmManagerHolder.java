package com.zhichan.openadsdk.holder;

import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.TTLocation;
import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.managers.setting.GlobalSetting;
import com.zhichan.openadsdk.holder.union.MsmAdConfig;

public class MsmManagerHolder {

    private static boolean sInit;

    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTAdSdk.getAdManager();
    }

    public static void requestPermissionIfNecessary(Context context) {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        TTAdSdk.getAdManager().requestPermissionIfNecessary(context);
    }

    // 穿山甲初始化
    public static void init(Context context, MsmAdConfig config) {
        doInit(context, config);
    }

    //step1:接入网盟广告sdk的初始化操作
    private static void doInit(Context context, MsmAdConfig config) {
        if (!sInit) {
            TTAdConfig ttAdConfig = new TTAdConfig.Builder()
                    .appId(config.getAppId())
                    .useTextureView(config.isUseTextureView()) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                    .appName(config.getAppName())
                    .titleBarTheme(config.getTitleBarTheme())
                    .allowShowNotify(config.isAllowShowNotify()) //是否允许sdk展示通知栏提示
                    .allowShowPageWhenScreenLock(config.isAllowShowPageWhenScreenLock()) //是否在锁屏场景支持展示广告落地页
                    .debug(config.isDebug()) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                    .directDownloadNetworkType(config.getDirectDownloadNetworkType()) //允许直接下载的网络状态集合
                    .supportMultiProcess(config.isSupportMultiProcess())//是否支持多进程
                    .httpStack(config.getHttpStack())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                    .customController(new TTCustomController() {
                        @Override
                        public boolean isCanUseLocation() {
                            return super.isCanUseLocation();
                        }

                        @Override
                        public TTLocation getTTLocation() {
                            return super.getTTLocation();
                        }

                        @Override
                        public boolean alist() {
                            return super.alist();
                        }

                        @Override
                        public boolean isCanUsePhoneState() {
                            return false;
                        }

                        @Override
                        public String getDevImei() {
                            return null;
                        }

                        @Override
                        public boolean isCanUseWifiState() {
                            return super.isCanUseWifiState();
                        }

                        @Override
                        public boolean isCanUseWriteExternal() {
                            return super.isCanUseWriteExternal();
                        }

                        @Override
                        public String getDevOaid() {
                            return super.getDevOaid();
                        }
                    })
                    .build();
            TTAdSdk.init(context, ttAdConfig);
            sInit = true;
        }
    }

    //---------------------广点通------------------//

    // 广点通初始化
    public static void initGDT(Context context, String appId) {
        GlobalSetting.setAgreePrivacyStrategy(false);
        GDTADManager.getInstance().initWith(context, appId);
    }

}
