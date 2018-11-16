package me.ningsk.common.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>描述：UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.<p>
 * 作者：ningsk<br>
 * 日期：2018/11/16 08 57<br>
 * 版本：v1.0<br>
 */
public class CrashHandler
        implements Thread.UncaughtExceptionHandler
{
    private static final String TAG = "CrashHandler";
    private static final String CRASH_PATH = "/mnt/sdcard/crash/";
    private Thread.UncaughtExceptionHandler mHandler; // 系统默认的UncaughtException 处理类
    private static CrashHandler mCrashHandler; // CrashHandler 实例
    private Context mContext; // 程序的Context对象
    private Map<String, String> mInfos = new HashMap(); // 用来存储设备信息和一场信息
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); // 用于格式化日期，作为日志文件名的一部分

    public static CrashHandler getInstance()
    {
        if (mCrashHandler == null)
            mCrashHandler = new CrashHandler();
        return mCrashHandler;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context)
    {
        this.mContext = context;

        this.mHandler = Thread.getDefaultUncaughtExceptionHandler(); // 获取系统默认的UncaughtException处理器

        Thread.setDefaultUncaughtExceptionHandler(this); // 设置该CrashHandler为程序的默认处理器
    }

    /**
     * dang UncaughtException发生时会载入该重写的方法来处理
     * @param thread
     * @param e
     */
    @Override
    public void uncaughtException(Thread thread, Throwable e)
    {
        if ((!handleException(e)) && (this.mHandler != null))
        {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            this.mHandler.uncaughtException(thread, e);
        } else {
            try {
                // 如果处理了，让程序继续运行3s再推出，保证文件保存并上传到服务器
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                Log.e(TAG, "error : ", ex);
            }
            // 退出程序
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     * @param ex 异常信息
     * @return true: 如果处理了该异常信息, 否则返回false;
     */
    private boolean handleException(Throwable ex)
    {
        if (ex == null) {
            return false;
        }
        // 手机设备参数信息
        collectDeviceInfo(mContext);

        new Thread()
        {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        // 保存日志文件
        saveCatchInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     * @Title: collectDeviceInfo
     * @param @param context
     * @return void
     * @throws
     */
    private void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfos.put("versionName", versionName);
                mInfos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfos.put(field.getName(), field.get("").toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveCatchInfo2File(Throwable ex)
    {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry entry : this.mInfos.entrySet()) {
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Object writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter((Writer)writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入Writer中
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        // 记得关闭
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            // 保存文件
            long timestamp = System.currentTimeMillis();
            String time = this.formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals("mounted")) {
                File dir = new File(CRASH_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(CRASH_PATH + fileName);
                fos.write(sb.toString().getBytes());

                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    private void sendCrashLog2PM(String fileName)
    {
        if (!new File(fileName).exists()) {
            Toast.makeText(this.mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
            while (true) {
                s = reader.readLine();
                if (s == null)
                    break;
                Log.i("info", s.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
