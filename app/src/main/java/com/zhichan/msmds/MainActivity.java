package com.zhichan.msmds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhichan.openadsdk.holder.MsmManagerHolder;
import com.zhichan.openadsdk.holder.adnet.MsmAdError;
import com.zhichan.openadsdk.holder.adnet.MsmAdnetRewardVideoAdLoadHolder;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        MsmAdnetRewardVideoAdLoadHolder.RewardVideoAdnetListener

{

    private MsmAdnetRewardVideoAdLoadHolder msmAdnetRewardVideoAdLoadHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msmAdnetRewardVideoAdLoadHolder = new MsmAdnetRewardVideoAdLoadHolder();

        // 动态权限请求，可选
        MsmManagerHolder.requestPermissionIfNecessary(this);

        // 打开穿山甲开屏页
        Button openSplash = findViewById(R.id.openSplash);
        openSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        // 打开优量汇开屏页
        Button openAdnetSplash = findViewById(R.id.openAdnetSplash);
        openAdnetSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SplashAdnetActivity.class);
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

        msmAdnetRewardVideoAdLoadHolder.setRewardVideoAdnetAdListener(this);

        Button loadReward = findViewById(R.id.load_reward);
        loadReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msmAdnetRewardVideoAdLoadHolder.rewardAdnetAdLoad(MainActivity.this, "2051256491220658");
            }
        });

        Button playReward = findViewById(R.id.play_reward);
        playReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msmAdnetRewardVideoAdLoadHolder.rewardVideoAdPlay(MainActivity.this);
            }
        });
    }

    @Override
    public void onAdLoaded() {
        Log.i("AD_NET_REWARD", "onAdLoaded");
    }

    @Override
    public void onVideoCached() {
        Log.i("AD_NET_REWARD", "onVideoCached");
    }

    @Override
    public void onShow() {
        Log.i("AD_NET_REWARD", "onShow");
    }

    @Override
    public void onExpose() {
        Log.i("AD_NET_REWARD", "onExpose");
    }

    @Override
    public void onReward(Map<String, Object> map) {
        Log.i("AD_NET_REWARD", "onReward");
    }

    @Override
    public void onClick() {
        Log.i("AD_NET_REWARD", "onClick");
    }

    @Override
    public void onVideoComplete() {
        Log.i("AD_NET_REWARD", "onVideoComplete");
    }

    @Override
    public void onClose() {
        Log.i("AD_NET_REWARD", "onClose");
    }

    @Override
    public void onError(MsmAdError error) {
        Log.i("AD_NET_REWARD", "onError, code:"+error.getErrorCode()+",msg:"+error.getErrorMsg());
    }
}
