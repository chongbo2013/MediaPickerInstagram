package me.ningsk.svideo.sdk.internal.common.project;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 07<br>
 * 版本：v1.0<br>
 */
public class CanvasAction {
    public static final float TOUCH_TOLERANGE = 4.0F;
    public Path path;
    public Paint paint;
    float tempX;
    float tempY;

    public CanvasAction(float x, float y, Paint p) {
        this.path = new Path();
        this.paint = p;
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setStyle(Style.STROKE);
        this.path.moveTo(x, y);
        this.path.lineTo(x, y);
        this.tempX = x;
        this.tempY = y;
    }


    public CanvasAction(Paint paint, Path path) {
        this.paint = paint;
        this.path = path;
    }

    public void point(float px, float py, Canvas canvas) {
        canvas.drawPoint(px, py, this.paint);
    }

    public void move(float mx, float my, Canvas mCanvas) {
        float dx = Math.abs(mx - this.tempX);
        float dy = Math.abs(this.tempY - my);
        if ((dx >= 4.0F) || (dy >= 4.0F)) {
            this.path.quadTo(this.tempX, this.tempY, (mx + this.tempX) / 2.0F, (my + this.tempY) / 2.0F);
            this.tempX = mx;
            this.tempY = my;
        }
        mCanvas.drawPath(this.path, this.paint);
    }

    public Path getPath() {
        return this.path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }


}
