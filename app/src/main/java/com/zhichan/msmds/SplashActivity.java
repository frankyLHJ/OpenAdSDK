package com.zhichan.msmds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.zhichan.openadsdk.holder.union.MsmAdLoadHolder;

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

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void gotoMainPage() {
        this.finish();
    }
}
