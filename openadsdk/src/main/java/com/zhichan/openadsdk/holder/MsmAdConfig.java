package com.zhichan.openadsdk.holder;

import com.bytedance.sdk.adnet.face.IHttpStack;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.TTDownloadEventLogger;
import com.bytedance.sdk.openadsdk.TTGlobalAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSecAbs;

public class MsmAdConfig {

    private String appId;
    private String appName;
    private boolean isPaid;
    private String keywords;
    private String data;
    private int titleBarTheme;
    private boolean isAllowShowNotify;
    private boolean isDebug;
    private boolean isAllowShowPageWhenScreenLock;
    private int[] directDownloadNetworkType;
    private boolean isUseTextureView;
    private boolean isSupportMultiProcess;
    private IHttpStack httpStack;
    private TTDownloadEventLogger tTDownloadEventLogger;
    private TTSecAbs tTSecAbs;
    private String[] needClearTaskReset;
    private boolean isAsyncInit;
    private TTCustomController customController;

    private MsmAdConfig() {
        this.isPaid = false;
        this.titleBarTheme = 0;
        this.isAllowShowNotify = true;
        this.isDebug = false;
        this.isAllowShowPageWhenScreenLock = false;
        this.isUseTextureView = false;
        this.isSupportMultiProcess = false;
        this.isAsyncInit = false;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTitleBarTheme() {
        return titleBarTheme;
    }

    public void setTitleBarTheme(int titleBarTheme) {
        this.titleBarTheme = titleBarTheme;
    }

    public boolean isAllowShowNotify() {
        return isAllowShowNotify;
    }

    public void setAllowShowNotify(boolean allowShowNotify) {
        isAllowShowNotify = allowShowNotify;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public boolean isAllowShowPageWhenScreenLock() {
        return isAllowShowPageWhenScreenLock;
    }

    public void setAllowShowPageWhenScreenLock(boolean allowShowPageWhenScreenLock) {
        isAllowShowPageWhenScreenLock = allowShowPageWhenScreenLock;
    }

    public int[] getDirectDownloadNetworkType() {
        return directDownloadNetworkType;
    }

    public void setDirectDownloadNetworkType(int[] directDownloadNetworkType) {
        this.directDownloadNetworkType = directDownloadNetworkType;
    }

    public boolean isUseTextureView() {
        return isUseTextureView;
    }

    public void setUseTextureView(boolean useTextureView) {
        isUseTextureView = useTextureView;
    }

    public boolean isSupportMultiProcess() {
        return isSupportMultiProcess;
    }

    public void setSupportMultiProcess(boolean supportMultiProcess) {
        isSupportMultiProcess = supportMultiProcess;
    }

    public IHttpStack getHttpStack() {
        return httpStack;
    }

    public void setHttpStack(IHttpStack httpStack) {
        this.httpStack = httpStack;
    }

    public TTDownloadEventLogger gettTDownloadEventLogger() {
        return tTDownloadEventLogger;
    }

    public void settTDownloadEventLogger(TTDownloadEventLogger tTDownloadEventLogger) {
        this.tTDownloadEventLogger = tTDownloadEventLogger;
    }

    public TTSecAbs gettTSecAbs() {
        return tTSecAbs;
    }

    public void settTSecAbs(TTSecAbs tTSecAbs) {
        this.tTSecAbs = tTSecAbs;
    }

    public String[] getNeedClearTaskReset() {
        return needClearTaskReset;
    }

    public void setNeedClearTaskReset(String[] needClearTaskReset) {
        this.needClearTaskReset = needClearTaskReset;
    }

    public boolean isAsyncInit() {
        return isAsyncInit;
    }

    public void setAsyncInit(boolean asyncInit) {
        isAsyncInit = asyncInit;
    }

    public TTCustomController getCustomController() {
        return customController;
    }

    public void setCustomController(TTCustomController customController) {
        this.customController = customController;
    }

    public static class Builder {
        private String appId;
        private String appName;
        private boolean isPaid = false;
        private String keywords;
        private String data;
        private int titleBarTheme = 0;
        private boolean isAllowShowNotify = true;
        private boolean isDebug = false;
        private boolean isAllowShowPageWhenScreenLock = false;
        private int[] directDownloadNetworkType;
        private boolean isUseTextureView = false;
        private boolean isSupportMultiProcess = false;
        private IHttpStack httpStack;
        private TTDownloadEventLogger tTDownloadEventLogger;
        private TTSecAbs tTSecAbs;
        private String[] needClearTaskReset;
        private boolean isAsyncInit = false;
        private TTCustomController customController;

        public Builder() {
        }

        public MsmAdConfig.Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public MsmAdConfig.Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public MsmAdConfig.Builder paid(boolean isPaid) {
            this.isPaid = isPaid;
            return this;
        }

        public MsmAdConfig.Builder keywords(String keywords) {
            this.keywords = keywords;
            return this;
        }

        public MsmAdConfig.Builder data(String data) {
            this.data = data;
            return this;
        }

        public MsmAdConfig.Builder titleBarTheme(int titleBarTheme) {
            this.titleBarTheme = titleBarTheme;
            return this;
        }

        public MsmAdConfig.Builder allowShowNotify(boolean isAllowShowNotify) {
            this.isAllowShowNotify = isAllowShowNotify;
            return this;
        }

        public MsmAdConfig.Builder debug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public MsmAdConfig.Builder allowShowPageWhenScreenLock(boolean isAllowShowPageWhenScreenLock) {
            this.isAllowShowPageWhenScreenLock = isAllowShowPageWhenScreenLock;
            return this;
        }

        public MsmAdConfig.Builder directDownloadNetworkType(int... directDownloadNetworkType) {
            this.directDownloadNetworkType = directDownloadNetworkType;
            return this;
        }

        public MsmAdConfig.Builder useTextureView(boolean isUseTextureView) {
            this.isUseTextureView = isUseTextureView;
            return this;
        }

        public MsmAdConfig.Builder supportMultiProcess(boolean isSupportMultiProcess) {
            this.isSupportMultiProcess = isSupportMultiProcess;
            return this;
        }

        public MsmAdConfig.Builder httpStack(IHttpStack httpStack) {
            this.httpStack = httpStack;
            return this;
        }

        public MsmAdConfig.Builder ttDownloadEventLogger(TTDownloadEventLogger tTDownloadEventLogger) {
            this.tTDownloadEventLogger = tTDownloadEventLogger;
            return this;
        }

        public MsmAdConfig.Builder ttSecAbs(TTSecAbs tTSecAbs) {
            this.tTSecAbs = tTSecAbs;
            return this;
        }

        public MsmAdConfig.Builder needClearTaskReset(String... needClearTaskReset) {
            this.needClearTaskReset = needClearTaskReset;
            return this;
        }

        public MsmAdConfig.Builder asyncInit(boolean isAsyncInit) {
            this.isAsyncInit = isAsyncInit;
            return this;
        }

        public MsmAdConfig.Builder customController(TTCustomController customController) {
            this.customController = customController;
            return this;
        }

        public MsmAdConfig build() {
            MsmAdConfig config = new MsmAdConfig();
            config.setAppId(this.appId);
            config.setAppName(this.appName);
            config.setPaid(this.isPaid);
            config.setKeywords(this.keywords);
            config.setData(this.data);
            config.setTitleBarTheme(this.titleBarTheme);
            config.setAllowShowNotify(this.isAllowShowNotify);
            config.setDebug(this.isDebug);
            config.setAllowShowPageWhenScreenLock(this.isAllowShowPageWhenScreenLock);
            config.setDirectDownloadNetworkType(this.directDownloadNetworkType);
            config.setUseTextureView(this.isUseTextureView);
            config.setSupportMultiProcess(this.isSupportMultiProcess);
            config.setHttpStack(this.httpStack);
            config.settTDownloadEventLogger(this.tTDownloadEventLogger);
            config.settTSecAbs(this.tTSecAbs);
            config.setNeedClearTaskReset(this.needClearTaskReset);
            config.setAsyncInit(this.isAsyncInit);
            config.setCustomController(this.customController);
            return config;
        }
    }
}
