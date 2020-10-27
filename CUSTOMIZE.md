# 买什么都省H5展示穿山甲原生广告方法

## 步骤1：声明注入，注入名称必须是"msmdsInjected" ##
```
// 参考
getWebView().addJavascriptInterface(new MyJavascriptInterface(this.getActivity()), "msmdsInjected")
```

## 步骤2：编写注入类 ##
```

public class MyJavascriptInterface {
        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }

        /**
         * 在买什么都省h5页面上展示banner广告
         * 注意点：
         * 1.方法名称必须"showToutiaoBannerAd(String data)"
         * 2.参数data是由h5传递过来的参数，包含banner广告显示的位置和宽高等信息
         * 
         * @param data
         */
        @JavascriptInterface
        public void showToutiaoBannerAd(String data) {
            // 以下仅供参考，请按自己实际情况调用
            try {
                JSONObject result = new JSONObject(data);
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                // 获得需要显示的广告的宽高
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));

                // 获得广告显示位置，在渲染时使用
                top = styleObj.has("top") ? styleObj.getString("top") : "0";
                left = styleObj.has("left") ? styleObj.getString("left") : "0";

                // 加载banner广告，传入宽高和广告位ID
                bannerAdLoad(context, codeId, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面关闭banner广告
         * 注意点：
         * 1.方法名称必须"dismissToutiaoBannerAd()"
         * 2.在容器中移除广告view
         * 3.adView是渲染成功banner广告view
         */
        @JavascriptInterface
        public void dismissToutiaoBannerAd() {
            // 以下仅供参考，请按自己实际情况调用
            getWebView().removeView(adView);
        }

        /**
         * 买什么都省h5页面加载激励视频广告
         * 注意点：
         * 1.方法名称必须"loadToutiaoRewardVideoAd()"
         */
        @JavascriptInterface
        public void loadToutiaoRewardVideoAd() {
            try {
                // 传广告位ID
                rewardVideoAdLoad(context, codeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面播放激励视频广告
         * 注意点：
         * 1.方法名称必须"playToutiaoRewardVideoAd()"
         */
        @JavascriptInterface
        public void playToutiaoRewardVideoAd() {
            // 以下仅供参考，请按自己实际情况调用
            rewardVideoAdPlay(context);
        }

        /**
         * 买什么都省h5页面的信息流广告
         * 注意点：
         * 1.方法名称必须"showToutiaoNativeAd(String data)"
         * 2.参数data是由h5传递过来的参数，包含信息流广告显示的位置和宽高等信息
         * @param data
         */
        @JavascriptInterface
        public void showToutiaoNativeAd(String data) {
            // 以下仅供参考，请按自己实际情况调用
            try {
                JSONObject result = new JSONObject(data);
                String style = result.getString("style");
                JSONObject styleObj = new JSONObject(style);
                // 获得需要显示的广告的宽高
                int width = Integer.parseInt(styleObj.getString("width"));
                int height = Integer.parseInt(styleObj.getString("height"));

                // 获得广告显示位置，在渲染时使用
                nativeTop = styleObj.has("top") ? styleObj.getString("top") : "0";
                nativeLeft = styleObj.has("left") ? styleObj.getString("left") : "0";

                // 加载信息流广告，传入宽高和广告位ID
                nativeAdLoad(context, codeId, width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 买什么都省h5页面关闭信息流广告
         * 注意点：
         * 1.方法名称必须"dismissToutiaoNativeAd()"
         * 2.可以在信息流广告渲染成功后保存，然后在移除时取出
         * 3.adView是渲染成功信息流广告view
         */
        @JavascriptInterface
        public void dismissToutiaoNativeAd() {
            // 以下仅供参考，请按自己实际情况调用
            getWebView().removeView(adView);
        }
    }
```

## 步骤3：广告回调方法处理 ## 

**banner广告回调方法**    

*onError,banner广告加载失败的回调*
```
getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadError();");
```

*onRenderSuccess,banner广告渲染成功的回调*
```
/**
* 注意点：
* 1.广告渲染成功后设置view显示的位置
*/
@Override
public void onRenderSuccess(View view, float width, float height) {
    
     // 根据加载广告时获取的top和left参数调整广告的位置
     view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(top)));
     view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(left)));

     // 可以在添加广告view之前把老的移除
     getWebView().removeView(adV);
     getWebView().addView(view);

     // 告知h5广告已经渲染成功
     getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onLoadSuccess({height:"+height+"});");

}
```

*用户主动关闭广告回调，dislike*
```
// 在容器中移除广告
getWebView().removeView(adView);
// 告知h5用户关闭了广告
getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoBannerAd.onHideManually();");
```


**激励视频广告回调方法**  

*激励视频广告加载失败的回调*
```
@Override
public void onRewardError(int i, String s) {
   getWebView().loadUrl("javascript:window.msmdsInjected.playToutiaoRewardVideoAd.onRewardError(" + i + "," + s + ");");
}
```

*激励视频广告播放完成的回调，这里现在没有服务器检查* 
```
@Override
public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
    getWebView().loadUrl("javascript:window.msmdsInjected.playToutiaoRewardVideoAd.onRewardVerify();");
}
```

**信息流广告回调方法**  

*onNativeError,信息流广告加载失败的回调*    
```
@Override
public void onNativeError(int i, String s) {
     getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeError(" + i + "," + s + ");");
}
```

*onNativeError,信息流广告加载失败的回调*  
```
@Override
public void onNativeRenderSuccess(View view, float width, float height) {

   // 根据加载广告时获取的top和left参数调整广告的位置
   view.setTranslationY(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(top)));
   view.setTranslationX(DensityUtil.dp2px(this.getActivity(),Float.parseFloat(left)));

   // 可以在添加广告view之前把老的移除
   getWebView().removeView(adV);
   getWebView().addView(view);

    // 告知h5广告已经渲染成功
   getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeRenderSuccess();");
}
```

*用户主动关闭广告回调，dislike*
```
// 在容器中移除广告
getWebView().removeView(adView);
// 告知h5用户关闭了广告
getWebView().loadUrl("javascript:window.msmdsInjected.showToutiaoNativeAd.onNativeShield();");
```
