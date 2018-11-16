package me.ningsk.svideo.sdk.external.struct;

import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.ningsk.svideo.sdk.internal.common.project.CanvasAction;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 36<br>
 * 版本：v1.0<br>
 */
public class CanvasInfo
        implements Serializable
{
    List<SerInfo> mSerPaths = new ArrayList();
    private SerInfo mCurrentInfo;
    private static String mSavePath = "/sdcard/pathinfo";

    public void lineStart(float x, float y)
    {
        this.mCurrentInfo = new SerInfo();
        this.mCurrentInfo.mPoints.add(new SerPoint(x, y));
    }
    public void lineMove(float x, float y) {
        this.mCurrentInfo.mPoints.add(new SerPoint(x, y));
    }
    public void lineEnd(float x, float y, Paint paint) {
        this.mCurrentInfo.mPoints.add(new SerPoint(x, y));
        this.mCurrentInfo.mColor = paint.getColor();
        this.mCurrentInfo.mWidth = paint.getStrokeWidth();
        this.mSerPaths.add(this.mCurrentInfo);
    }

    private Paint transferPaint(SerInfo si) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(si.mWidth);
        paint.setColor(si.mColor);
        return paint;
    }
    private Path transferPath(SerInfo sp) {
        Path path = new Path();

        int size = sp.mPoints.size();

        if (size < 3) {
            return path;
        }

        SerPoint p = (SerPoint)sp.mPoints.get(0);
        path.moveTo(p.x, p.y);

        float ox = p.x;
        float oy = p.y;

        for (int i = 1; i < size - 1; i++) {
            p = (SerPoint)sp.mPoints.get(i);
            path.quadTo(ox, oy, (ox + p.x) / 2.0F, (oy + p.y) / 2.0F);
            ox = p.x;
            oy = p.y;
        }

        p = (SerPoint)sp.mPoints.get(size - 1);
        path.lineTo(p.x, p.y);

        return path;
    }

    public List<CanvasAction> transfer() {
        List canvasActions = new ArrayList();
        for (SerInfo sp : this.mSerPaths) {
            Paint paint = transferPaint(sp);
            Path path = transferPath(sp);
            canvasActions.add(new CanvasAction(paint, path));
        }
        return canvasActions;
    }

    public void resore(CanvasInfo info) {
        if ((info.mSerPaths == null) || (info.mSerPaths.size() == 0)) {
            return;
        }
        this.mSerPaths.addAll(0, info.mSerPaths);
    }

    public static CanvasInfo load()
    {
        CanvasInfo ci = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(mSavePath));
            ci = (CanvasInfo)ois.readObject();
            Log.i("CanvasInfo", "load ok, size = " + ci.mSerPaths.size());
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ois = null;
            }
            if (ci == null) {
                ci = new CanvasInfo();
            }
        }
        return ci;
    }

    public void save() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(mSavePath));
            oos.writeObject(this);
            Log.i("CanvasInfo", "save ok, size = " + this.mSerPaths.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                oos = null;
            }
        }
    }

    public void clean()
    {
        this.mSerPaths.clear();
    }

    public void remove() {
        if ((this.mSerPaths != null) && (this.mSerPaths.size() > 0))
            this.mSerPaths.remove(this.mSerPaths.size() - 1);
    }

    class SerInfo
            implements Serializable
    {
        private int mColor;
        private float mWidth;
        private List<CanvasInfo.SerPoint> mPoints = new ArrayList();

        SerInfo()
        {
        }
    }

    class SerPoint
            implements Serializable
    {
        private float x;
        private float y;

        public SerPoint(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    }
}