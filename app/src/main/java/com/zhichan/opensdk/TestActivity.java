package com.zhichan.opensdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.zhichan.openadsdk.view.fragment.MsmIntegralFragment;


public class TestActivity extends AppCompatActivity {

    MsmIntegralFragment agentWebFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle;
        agentWebFragment = MsmIntegralFragment.getInstance(mBundle = new Bundle());
        ft.add(R.id.container_framelayout, agentWebFragment, MsmIntegralFragment.class.getName());
        mBundle.putString(MsmIntegralFragment.URL_KEY, "https://wxapp.msmds.cn/h5/rn_web/#/sign");
//        mBundle.putBoolean(MsmIntegralFragment.SHOW_TOOLBAR, false);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (agentWebFragment == null || agentWebFragment.canBack()) {
            super.onBackPressed();
        } else {
            agentWebFragment.back();
        }
    }
}
