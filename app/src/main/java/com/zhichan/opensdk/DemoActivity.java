package com.zhichan.opensdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.zhichan.openadsdk.view.fragment.MsmIntegralFragment;


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
//        mBundle.putBoolean(MsmIntegralFragment.SHOW_TOOLBAR, false); // 是否显示toolbar
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
