# 买什么都省SDK接入指南
*注意：此版本sdk不适用于中国以外的安卓商店/渠道。*

**步骤 1：工程设置**
*导入 aar 及 SDK 依赖的 jar 包*  
将本 SDK 压缩包内的 open_ad_sdk.aar 复制到 Application Module/libs 文件夹(没有的话须手动创建), 并将以下代码添加到您 app 的build.gradle
```
  android{
    ...
    repositories {
      flatDir {
        dirs 'libs'
      }
    }
  }
  
  depedencies {
      ...
      implementation(name: 'open_ad_sdk', ext: 'aar')
      implementation 'com.zhichan:openadsdk:1.0.1'
  }
```
**步骤二：全局配置**  
*添加权限*  
```
<!--必要权限-->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!--可选权限-->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
<uses-permission android:name="android.permission.GET_TASKS"/>

<!--可选，“获取地理位置权限”和“不给予地理位置权限，开发者传入地理位置参数”两种方式上报用户位置，两种方式均可不选，添加位置权限或参数将帮助投放定位广告-->
<!--请注意：无论通过何种方式提供用户地理位置，均需向用户声明地理位置权限将应用于广告投放，不强制获取地理位置信息-->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

<!-- 如果有视频相关的广告且使用textureView播放，请务必添加，否则黑屏 -->
<uses-permission android:name="android.permission.WAKE_LOCK" />
```  
**注意：SDK不强制获取以上权限，即使没有获取可选权限SDK也能正常运行；获取以上权限将帮助优化投放广告精准度和用户的交互体验，提高eCPM。
为获取更好的广告推荐效果，以及提高激励视频广告、下载类广告等填充率，建议在广告请求前，合适的时机调用 SDK 提供的方法，如在用户第一次启动您的 app 后的主界面时调用如下方法：**  
```
// 动态权限请求，可选
MsmManagerHolder.requestPermissionIfNecessary(this);
```  
**运行环境配置**  
*本 SDK 可运行于 Android4.0 (API Level 14) 及以上版本。*  
*如果开发者声明targetSdkVersion到API 23以上，请确保调用本SDK的任何接口前，已经申请到了SDK要求的所有权限，否则SDK部分特性可能受限。*  

**代码混淆配置**
*如果您需要使用 proguard 混淆代码，需确保不要混淆 SDK 的代码。 请在 proguard.cfg 文件(或其他混淆文件)尾部添加如下配置:*  
```
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}
-keep class com.zhichan.openadsdk.** { *; }
-keep public interface com.zhichan.openadsdk.common.** {*;}
```
**SDK 初始化配置**  
*开发者需要在Application#onCreate()方法中调用以下代码来初始化sdk。*  
```
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MsmAdConfig msmAdConfig = new MsmAdConfig.Builder()
                .appId("5068842") // 测试appid
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName("test")
                .titleBarTheme(MsmConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(MsmConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合,不填则所有网络都不允许直接下载，进行弹窗提示
                .supportMultiProcess(false)//是否支持多进程
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发。
                .build();
        MsmManagerHolder.init(this, msmAdConfig);
    }

}
```

# 使用
**开屏广告**  
**demo如下，可以根据自己需要进行修改**  
**注意：开屏广告view：width >=70%屏幕宽；height >=50%屏幕高才能正常上报获取收益**  
**加载广告方法的最后一个参数填true sdk就不会显示倒计时按钮，可以自定义**  

![avatar](https://alicdn.msmds.cn/adSdk/demo_sdk_splash.jpg)
```
public class SplashActivity extends AppCompatActivity implements MsmAdLoadHolder.SplashAdListener {

    private FrameLayout adGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        adGroup = findViewById(R.id.ad_group);
        MsmAdLoadHolder.getInstance().setSplashAdListener(this);
        MsmAdLoadHolder.getInstance().splashAdLoad(
                this,
                "887365528", // 广告位id
                1080,
                1920,
                3000, // 广告加载超时时间
                false // 是否自定义跳过逻辑
        );
    }

    @Override
    public void onError(int i, String s) {
        Log.e("MsmAdLoadHolder", "onError: " + i + ",msg:" + s);
        this.gotoMainPage();
    }

    @Override
    public void onTimeout() {
        Log.i("MsmAdLoadHolder", "onTimeout");
        this.gotoMainPage();
    }

    @Override
    public void onLoadSuccess(View view) {
        Log.i("MsmAdLoadHolder", "onLoadSuccess");
        if (view != null && adGroup != null && !SplashActivity.this.isFinishing()) {
            adGroup.removeAllViews();
            //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
            adGroup.addView(view);
        }
    }

    @Override
    public void onAdClicked(View view, int i) {
        Log.i("MsmAdLoadHolder", "onAdClicked:" + i);
    }

    @Override
    public void onAdShow(View view, int i) {
        Log.i("MsmAdLoadHolder", "onAdShow:" + i);
    }

    @Override
    public void onAdSkip() {
        Log.i("MsmAdLoadHolder", "onAdSkip" );
        this.gotoMainPage();
    }

    @Override
    public void onAdTimeOver() {
        Log.i("MsmAdLoadHolder", "onAdTimeOver" );
        this.gotoMainPage();
    }

    private void gotoMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
```
**打开签到页**  
**签到页是使用fragment可以嵌入自己的Activity中**
```
public class DemoActivity extends AppCompatActivity {

    MsmIntegralFragment msmIntegralFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle;
        msmIntegralFragment = MsmIntegralFragment.getInstance(mBundle = new Bundle());
        ft.add(R.id.container_framelayout, msmIntegralFragment, MsmIntegralFragment.class.getName());
        mBundle.putString(MsmIntegralFragment.URL_KEY, "https://wxapp.msmds.cn/h5/rn_web/#/sign");// 签到页
//        mBundle.putBoolean(MsmIntegralFragment.SHOW_TOOLBAR, false); // 是否显示toolbar(默认显示)
        mBundle.putString(MsmIntegralFragment.BANNER_CODE_ID, "945413865"); // banner广告位id
        mBundle.putString(MsmIntegralFragment.NATIVE_CODE_ID, "945198258"); // 信息流广告位id
        mBundle.putString(MsmIntegralFragment.REWARD_VIDEO_CODE_ID, "945198260"); // 激励视频广告位id
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (msmIntegralFragment == null || msmIntegralFragment.canBack()) {
            super.onBackPressed();
        } else {
            msmIntegralFragment.back();
        }
    }
}
```
