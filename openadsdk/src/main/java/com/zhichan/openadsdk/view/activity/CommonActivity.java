package com.zhichan.openadsdk.view.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zhichan.openadsdk.R;
import com.zhichan.openadsdk.view.fragment.AgentWebFragment;

public class CommonActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    public static final String TYPE_KEY = "type_key";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);

        mFrameLayout = this.findViewById(R.id.container_framelayout);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(0);
    }

    private AgentWebFragment mAgentWebFragment;

    private void openFragment(int key) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        switch (key) {

            /*Fragment 使用AgenWeb*/
            case 0: //项目中请使用常量代替0 ， 代码可读性更高
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "https://wxapp.msmds.cn/h5/rn_web/#/sign");
                break;
            default:
                break;

        }
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        if (mAgentWebFragment == null || mAgentWebFragment.canBack()) {
            super.onBackPressed();
        } else {
            mAgentWebFragment.back();
        }

    }
}
