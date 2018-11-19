package me.ningsk.log.core;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class JrLoggerManager
{
    private static boolean sLoggerOpen = true;
    private static Map<String, JrLogger> mLoggers = new HashMap();

    public static JrLogger createLogger(Context context, String tag) {
        JrLogger logger = (JrLogger)mLoggers.get(tag);
        if (logger != null) {
            return logger;
        }
        logger = new JrLogger(new LogService(tag));
        logger.init(context);
        mLoggers.put(tag, logger);
        return logger;
    }

    public static void destroyLogger(String tag) {
        JrLogger logger = (JrLogger)mLoggers.remove(tag);
        if (logger != null)
            logger.destroy();
    }

    public static JrLogger getLogger(String tag)
    {
        return (JrLogger)mLoggers.get(tag);
    }

    public static void toggleLogger(boolean open) {
        sLoggerOpen = open;
    }
}
