package com.zhichan.msmds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.zhichan.openadsdk.view.fragment.MsmUniversalFragment;


public class DemoActivity extends AppCompatActivity {

    MsmUniversalFragment msmIntegralFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle  = new Bundle();
        msmIntegralFragment = MsmUniversalFragment.getInstance(mBundle);
        msmIntegralFragment.injectedUserData(""); // 注入用户信息
        ft.add(R.id.container_framelayout, msmIntegralFragment, MsmUniversalFragment.class.getName());
//        mBundle.putString(MsmIntegralFragment.URL_KEY, "https://wxapp.msmds.cn/h5/react_web/newSign");// 签到页
//        mBundle.putString(MsmIntegralFragment.URL_KEY, "http://192.168.0.222:8080/app/Sign");// 签到页http://192.168.0.222:8080/qsbk/indexPage
        mBundle.putString(MsmUniversalFragment.URL_KEY, "https://www.baidu.com/");
        mBundle.putBoolean(MsmUniversalFragment.SHOW_TOOLBAR, true); // 是否显示toolbar
        mBundle.putString(MsmUniversalFragment.APP_ID, "10000"); // appid
        mBundle.putString(MsmUniversalFragment.BANNER_CODE_ID, "945413865"); // banner广告位id
        mBundle.putString(MsmUniversalFragment.NATIVE_CODE_ID, "945198258"); // 信息流广告位id
        mBundle.putString(MsmUniversalFragment.REWARD_VIDEO_CODE_ID, "945198260"); // 激励视频广告位id
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (msmIntegralFragment != null && msmIntegralFragment.canBack()) {
            msmIntegralFragment.back();
        } else {
            super.onBackPressed();
        }
    }
}
