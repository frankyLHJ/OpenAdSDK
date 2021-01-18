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
        String js = "(function(){\n" +
                "            //注入用户信息\n" +
                "            window.appUserInfo = function (){\n" +
                "                return ${JSON.stringify(userData)};\n" +
                "            };\n" +
                "            \n" +
                "            // 版本号\n" +
                "            window.appInfo = function (){\n" +
                "                return ${JSON.stringify(appInfo)};\n" +
                "            };\n" +
                "\n" +
                "            //注入与原声交互的方法\n" +
                "            window.sendMessageToApp = function (json){\n" +
                "                window.ReactNativeWebView.postMessage(JSON.stringify(json));\n" +
                "            };\n" +
                "\n" +
                "            // 删除美团外卖页面-“网页下单”按钮\n" +
                "            setTimeout(function(){document.getElementsByClassName('button-container-custome')[0].children[2].remove();},0);\n" +
                "            \n" +
                "        })()";
        msmIntegralFragment.injectedUserData(js); // 注入用户信息
        msmIntegralFragment.injectedExtData(""); // 扩展内容，注入方法或者其他数据
        ft.add(R.id.container_framelayout, msmIntegralFragment, MsmUniversalFragment.class.getName());
//        mBundle.putString(MsmIntegralFragment.URL_KEY, "https://wxapp.msmds.cn/h5/react_web/newSign");// 签到页
        mBundle.putString(MsmUniversalFragment.URL_KEY, "http://192.168.31.222:8080/app/Sign");// 签到页http://192.168.0.222:8080/qsbk/indexPage
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
