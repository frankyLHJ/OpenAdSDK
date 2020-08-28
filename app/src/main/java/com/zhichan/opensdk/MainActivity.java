package com.zhichan.opensdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhichan.openadsdk.holder.MsmManagerHolder;
import com.zhichan.openadsdk.holder.MsmRewardVideoAdLoadHolder;

public class MainActivity extends AppCompatActivity implements MsmRewardVideoAdLoadHolder.RewardVideoAdListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 动态权限请求，可选
        MsmManagerHolder.requestPermissionIfNecessary(this);

        // 打开开屏页
        Button openSplash = findViewById(R.id.openSplash);
        openSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

//      打开积分活动页(fragment)
        Button integral1 = findViewById(R.id.integralBtn1);
        integral1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DemoActivity.class);
                startActivity(intent);
            }
        });

//        MsmRewardVideoAdLoadHolder.getInstance().setRewardVideoAdListener(this);
//        MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdLoad(MainActivity.this, "945198260");
//        Button rewardVideo = findViewById(R.id.rewardVideo);
//        rewardVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MsmRewardVideoAdLoadHolder.getInstance().rewardVideoAdPlay(MainActivity.this);
//            }
//        });
    }


    @Override
    public void onRewardError(int i, String s) {

    }

    @Override
    public void onRewardAdClose() {

    }

    @Override
    public void onRewardVideoComplete() {

    }

    @Override
    public void onRewardVideoError() {

    }

    @Override
    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {

    }

    @Override
    public void onSkippedVideo() {

    }
}
