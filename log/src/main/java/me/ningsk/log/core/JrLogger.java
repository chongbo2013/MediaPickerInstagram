package me.ningsk.log.core;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import me.ningsk.log.util.UUIDGenerator;
import java.util.Map;

public class JrLogger {
    private static final String KEY_SHARED_PREFERENCE = "jr_svideo_global_info";
    private String mRequestID = null;
    private LogService mLogService;
    private LogService mHttpService;

    protected JrLogger(LogService logService)
    {
        this.mLogService = logService;
        this.mHttpService = new LogService(String.valueOf(System.currentTimeMillis()));
    }

    public void init(Context context) {
        initGlobalInfo(context);
        updateRequestID();
    }

    private void initGlobalInfo(Context context) {
        if (JrLogCommon.APPLICATION_ID == null) {
            JrLogCommon.APPLICATION_ID = context.getPackageName();
            JrLogCommon.APPLICATION_NAME = getApplicationName(context);
        }
        if (JrLogCommon.UUID == null) {
            SharedPreferences sp = context.getSharedPreferences("jerei_svideo_global_info", 0);
            if (sp.contains("uuid")) {
                JrLogCommon.UUID = sp.getString("uuid", null);
            }
            if (JrLogCommon.UUID == null) {
                JrLogCommon.UUID = UUIDGenerator.generateUUID();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("uuid", JrLogCommon.UUID);
                editor.commit();
            }
        }
    }

    public void updateRequestID() {
        this.mRequestID = UUIDGenerator.generateUUID();
    }

    public void pushLog(Map<String, String> args, String logLevel, String module, String subModule, int eventId)
    {
        pushLog(args, logLevel, module, subModule, eventId, this.mRequestID);
    }

    public void pushLog(Map<String, String> args, String logLevel, String module, String subModule, int eventId, String requestID)
    {
        final String url = JrLogCommon.LOG_PUSH_URL + JrLogParam.generatePushParams(args, logLevel, module, subModule, eventId, requestID);
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId())
            this.mHttpService.execute(new Runnable()
            {
                public void run() {
                    try {
                        HttpRequest.get(url, new BaseHttpRequestCallback()
                        {
                            protected void onSuccess(Headers headers, Object o) {
                                super.onSuccess(headers, o);
                                Log.d("AliYunLog", "Push log success");
                            }

                            public void onFailure(int errorCode, String msg)
                            {
                                super.onFailure(errorCode, msg);
                                Log.d("AliYunLog", "Push log failure, error Code " + errorCode + ", msg:" + msg);
                            } } );
                    }
                    catch (Exception e) {
                    }
                }
            });
        else try {
            HttpRequest.get(url, new BaseHttpRequestCallback()
            {
                protected void onSuccess(Headers headers, Object o)
                {
                    super.onSuccess(headers, o);
                    Log.d("AliYunLog", "Push log success");
                }

                public void onFailure(int errorCode, String msg)
                {
                    super.onFailure(errorCode, msg);
                    Log.d("AliYunLog", "Push log failure, error Code " + errorCode + ", msg:" + msg);
                }
            });
        }
        catch (Exception e) {
        }
    }

    public LogService getLogService() { return this.mLogService; }

    public void destroy()
    {
        if (this.mLogService != null) {
            this.mLogService.quit();
            this.mLogService = null;
        }
        if (this.mHttpService != null) {
            this.mHttpService.quit();
            this.mHttpService = null;
        }
    }

    public void setRequestID(String requestID) {
        this.mRequestID = requestID;
    }

    public String getRequestID() {
        return this.mRequestID;
    }

    public String getApplicationName(Context context) { PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            applicationInfo = null;
        }

        String applicationName = (String)packageManager
                .getApplicationLabel(applicationInfo);

        return applicationName;
    }
}
