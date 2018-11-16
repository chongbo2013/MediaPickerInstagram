package me.ningsk.common.gl;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static me.ningsk.common.global.JRTag.TAG;


/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/16 09 32<br>
 * 版本：v1.0<br>
 */
public class GLCore {
    private static Object sGLContext = new Object();
    private static int sGLCount = 0;
    private static long sToken = 0L;
    private static LinkedHashMap<Callback, WeakReference<SurfaceView>> sPendingGLUsers = new LinkedHashMap();
    private static long mExecutingThreadCount = 0L;
    public static Object sReleaseLocker = new Object();

    public GLCore() {
    }

    public static boolean isGLContextAvailable(String tag, long token) {
        Object var3 = sGLContext;
        synchronized(sGLContext) {
            return isTokenValid(token) && mExecutingThreadCount == 0L;
        }
    }

    public static void useGLContext(String tag, SurfaceView surfaceView, final GLCore.Callback callback, long token) {
        Object var5 = sGLContext;
        synchronized(sGLContext) {
            if (isTokenValid(token)) {
                if (mExecutingThreadCount == 0L) {
                    Log.d(TAG, "Tag useGLContext::" + tag + ",token is simple, directly executing, token " + token);
                    callback.onGLContextAvailable();
                } else {
                    ++mExecutingThreadCount;
                    if (surfaceView instanceof GLSurfaceView) {
                        Log.d(TAG, "useGLContext::do gl callback, token " + token + ", sToken " + sToken);
                        ((GLSurfaceView)surfaceView).queueEvent(() -> {
                            try {
                                callback.onGLContextAvailable();
                                GLCore.mExecutingThreadCount--;
                            } catch (Exception e) {
                                ;
                            }

                        });
                    } else {
                        Log.d(TAG, "useGLContext::do main callback, token " + token + ", sToken " + sToken);
                        surfaceView.post(() -> {
                            try {
                                callback.onGLContextAvailable();
                                GLCore.mExecutingThreadCount--;
                            } catch (Exception e) {
                                ;
                            }

                        });
                    }
                }

            } else {
                if (sGLCount > 0) {
                    Log.d(TAG, "Tag:" + tag + ",useGLContext sGLContext , but need waiting, token " + token + ", sToken " + sToken);
                    surfaceView.setTag(token);
                    sPendingGLUsers.put(callback, new WeakReference(surfaceView));
                } else {
                    Log.d(TAG, "Tag:" + tag + ",useGLContext sGLContext, no thread use GL Context, token " + token);
                    callback.onGLContextAvailable();
                    ++sGLCount;
                    sToken = token;
                }

            }
        }
    }

    public static void releaseGLContext(String tag, long token) {
        Object var3 = sGLContext;
        synchronized(sGLContext) {
            if (sToken != token) {
                Log.e(TAG, "Tag:" + tag + ",invalid token " + token + ", cannot release gl context");
            } else {
                Log.d(TAG, "Tag:" + tag + ",releaseGLContext , sGLCount = " + sGLCount + ", sToken = " + sToken);
                if (sPendingGLUsers.size() == 0) {
                    --sGLCount;
                } else {
                    Log.d(TAG, "Tag:" + tag + ",Has next GLContext user waiting for it, mExecutingThreadCount = " + mExecutingThreadCount);
                    Set mapSet = sPendingGLUsers.entrySet();
                    ArrayList var6 = new ArrayList();
                    GLCore.Callback nextFirstCallback = null;
                    Iterator var8 = mapSet.iterator();

                    SurfaceView nextView;
                    Map.Entry var9;
                    GLCore.Callback var10;
                    long var11;
                    while(var8.hasNext()) {
                        var9 = (Map.Entry)var8.next();
                        var10 = (GLCore.Callback)var9.getKey();
                        if (nextFirstCallback == null) {
                            nextFirstCallback = var10;
                        }

                        nextView = (SurfaceView)((WeakReference)var9.getValue()).get();
                        if (nextView != null) {
                            var11 = (Long)nextView.getTag();
                            Log.d(TAG, "collect removeItem NextToken : " + var11);
                            if (var11 == sToken) {
                                var6.add(var10);
                            }
                        } else {
                            Log.d(TAG, "collect removeItem:SurfaceView is destroyed, so skip to execute callback");
                        }
                    }

                    mExecutingThreadCount = (long)var6.size();
                    Log.d(TAG, "removeItem Size:" + mExecutingThreadCount + ", sPendingGLUsers Size:" + sPendingGLUsers.size());
                    if (mExecutingThreadCount == 0L) {
                        Log.d(TAG, "remove item size is 0, so need other token to continue executing");
                        sToken = 0L;
                        var8 = mapSet.iterator();

                        while(var8.hasNext()) {
                            var9 = (Map.Entry)var8.next();
                            var10 = (GLCore.Callback)var9.getKey();
                            nextView = (SurfaceView)((WeakReference)var9.getValue()).get();
                            if (nextView != null) {
                                var11 = (Long)nextView.getTag();
                                Log.d(TAG, "other token NextToken:" + var11);
                                if (sToken == 0L) {
                                    sToken = var11;
                                }

                                if (var11 == sToken) {
                                    var6.add(var10);
                                }
                            } else {
                                Log.d(TAG, "other token:SurfaceView is destroyed, so skip to execute callback");
                            }
                        }

                        mExecutingThreadCount = (long)var6.size();
                    }

                    var8 = var6.iterator();

                    while(var8.hasNext()) {
                        final GLCore.Callback callback = (GLCore.Callback)var8.next();
                        nextView = (SurfaceView)((WeakReference)sPendingGLUsers.get(callback)).get();
                        if (nextView != null) {
                            if (nextView instanceof GLSurfaceView) {
                                Log.d(TAG, "do gl callback, token " + nextView.getTag() + ", sToken " + sToken);
                                ((GLSurfaceView)nextView).queueEvent(new Runnable() {
                                    public void run() {
                                        try {
                                            callback.onGLContextAvailable();
                                            GLCore.mExecutingThreadCount--;
                                        } catch (Exception e) {
                                            ;
                                        }

                                    }
                                });
                            } else {
                                Log.d(TAG, "do main callback, token " + nextView.getTag() + ", sToken " + sToken);
                                nextView.post(() -> {
                                    try {
                                        callback.onGLContextAvailable();
                                        GLCore.mExecutingThreadCount--;
                                    } catch (Exception e) {
                                        ;
                                    }

                                });
                            }

                            sPendingGLUsers.remove(callback);
                        } else {
                            Log.d(TAG, "iterator removeItem:SurfaceView is destroyed, so skip to execute callback");
                        }
                    }
                }

            }
        }
    }

    private static boolean isTokenValid(long token) {
        return sToken == token && token > 0L;
    }

    public interface Callback {
        void onGLContextAvailable();
    }
}

