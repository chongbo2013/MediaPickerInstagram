package me.ningsk.common.logger;


public final class Logger {
    public static final String DEFAULT_TAG = "JRLogger";
    private static boolean debug = false;
    private static LoggerPrinter loggerPrinter;

    public Logger() {
    }

    public static void setDebug(boolean isDebug) {
        debug = isDebug;
    }

    public static LoggerPrinter getDefaultLogger() {
        if (loggerPrinter == null) {
            loggerPrinter = LoggerFactory.getFactory(DEFAULT_TAG, debug);
        }

        return loggerPrinter;
    }
}
