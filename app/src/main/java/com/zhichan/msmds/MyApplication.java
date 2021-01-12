package com.zhichan.msmds;

import android.app.Application;

import com.zhichan.openadsdk.constant.MsmConstant;
import com.zhichan.openadsdk.holder.union.MsmAdConfig;
import com.zhichan.openadsdk.holder.MsmManagerHolder;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MsmAdConfig msmAdConfig = new MsmAdConfig.Builder()
                .appId("5068842")
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName("test")
                .titleBarTheme(MsmConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(MsmConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合,不填则所有网络都不允许直接下载，进行弹窗提示
                .supportMultiProcess(false)//是否支持多进程
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();
        MsmManagerHolder.init(this, msmAdConfig);

        MsmManagerHolder.initGDT(this, "1110002906");
    }

}


