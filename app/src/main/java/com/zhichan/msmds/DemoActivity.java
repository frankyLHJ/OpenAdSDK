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
        ft.add(R.id.container_framelayout, msmIntegralFragment, MsmUniversalFragment.class.getName());
//        mBundle.putString(MsmUniversalFragment.URL_KEY, "https://test.msmds.cn/h5/react_web/channel/newSign");// 签到页
//        mBundle.putString(MsmUniversalFragment.URL_KEY, "file:///android_asset/test.html");// 签到页
        mBundle.putString(MsmUniversalFragment.URL_KEY, "http://192.168.31.222:8080/channel/newSign");// 签到页
        mBundle.putBoolean(MsmUniversalFragment.SHOW_TOOLBAR, true); // 是否显示toolbar
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
