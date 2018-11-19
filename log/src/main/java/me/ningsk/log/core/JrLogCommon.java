package me.ningsk.log.core;


import android.os.Build;

public class JrLogCommon {
    //TODO: log信息上传地址
    public static final String LOG_PUSH_URL = "";
    public static final String LOG_LEVEL = "1";
    public static final String REFERER = "aliyun";
    public static final String PRODUCT = "svideo";
    public static final String MODULE = "svideo_pro";
    public static final String TERMINAL_TYPE = "phone";
    public static final String DEVICE_MODEL = Build.MODEL;
    public static final String OPERATION_SYSTEM = "android";
    public static final String OS_VERSION = Build.VERSION.RELEASE;
    public static String APPLICATION_ID = null;
    public static String APPLICATION_NAME = null;
    public static String UUID = null;

    public static class SubModule
    {
        public static final String RECORD = "record";
        public static final String CUT = "cut";
        public static final String EDIT = "edit";
        public static final String IMPORT = "import";
    }

    public static class Module
    {
        public static final String BASE = "svideo_basic";
        public static final String STANDARD = "svideo_standard";
        public static final String PRO = "svideo_pro";
    }

    public static class LogLevel
    {
        public static final String DEBUG = "debug";
        public static final String INFO = "info";
        public static final String WARN = "warn";
        public static final String ERROR = "error";
    }
}
