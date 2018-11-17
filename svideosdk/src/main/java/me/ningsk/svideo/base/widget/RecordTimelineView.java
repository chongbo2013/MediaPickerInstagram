package me.ningsk.svideo.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.DeadObjectException;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>描述：分段录制条<p>
 * 作者：ningsk<br>
 * 日期：2018/11/17 09 56<br>
 * 版本：v1.0<br>
 */
public class RecordTimelineView extends View{
    private int maxDuration;
    private int minDuration;
    private CopyOnWriteArrayList<DrawInfo> clipDurationList = new CopyOnWriteArrayList<>();
    private DrawInfo currentClipDuration = new DrawInfo();
    private Paint paint = new Paint();
    private int durationColor;
    private int selectColor;
    private int offsetColor;
    private int backgroundColor;
    private boolean isSelected = false;
    public RecordTimelineView(Context context) {
        super(context);
        init();
    }

    public RecordTimelineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordTimelineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (backgroundColor != 0) {
            canvas.drawColor(getResources().getColor(backgroundColor));
        }
        int lastTotalDuration = 0;
        for (int i = 0; i < clipDurationList.size(); i++) {
            DrawInfo drawInfo = clipDurationList.get(i);
            switch (drawInfo.drawType) {
                case OFFSET:
                    paint.setColor(getResources().getColor(offsetColor));
                    break;
                case DURATION:
                    paint.setColor(getResources().getColor(durationColor));
                    break;
                case SELECT:
                    paint.setColor(getResources().getColor(selectColor));
                    break;
            }
            if (drawInfo.drawType == DrawType.OFFSET) {
                canvas.drawRect((lastTotalDuration - drawInfo.length) / (float) maxDuration * getWidth(), 0f, lastTotalDuration / (float) maxDuration * getWidth(), getHeight(), paint) ;
            } else {
                canvas.drawRect(lastTotalDuration / (float) maxDuration * getWidth(), 0f, (lastTotalDuration + drawInfo.length) / (float) maxDuration * getWidth(), getHeight(), paint);
                lastTotalDuration += drawInfo.length;
            }
        }
        if (currentClipDuration != null && currentClipDuration.length != 0) {
            paint.setColor(getResources().getColor(durationColor));
            canvas.drawRect(lastTotalDuration / (float) maxDuration * getWidth(), 0f, (lastTotalDuration + currentClipDuration.length) / (float) maxDuration * getWidth(), getHeight(), paint);
        }
        Log.e("onDraw", "lastTotalDuration" + lastTotalDuration + "\n" + " maxDuration" + maxDuration);
    }

    public void clipComplete() {
        clipDurationList.add(currentClipDuration);
        DrawInfo info = new DrawInfo();
        info.length = maxDuration / 400;
        info.drawType = DrawType.OFFSET;
        clipDurationList.add(info);
        currentClipDuration = new DrawInfo();
        invalidate();
    }

    public void deleteList() {
        if (clipDurationList.size() >= 2) {
            clipDurationList.remove(clipDurationList.size() - 1);
            clipDurationList.remove(clipDurationList.size() - 1);
        }
        invalidate();
    }

    public void clear() {
        if (clipDurationList.size() >= 2) {
            clipDurationList.clear();
        }
        invalidate();
    }

    public void selectLast() {
        if (clipDurationList.size() >= 2) {
            DrawInfo info = clipDurationList.get(clipDurationList.size() - 2);
            info.drawType = DrawType.SELECT;
            invalidate();
            isSelected = true;
        }
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }

    public void setDuration(int duration) {
        if (isSelected) {
            for(DrawInfo info : clipDurationList) {
                if (info.drawType == DrawType.SELECT) {
                    info.drawType = DrawType.DURATION;
                    isSelected = false;
                    break;
                }
            }
            this.currentClipDuration.drawType = DrawType.DURATION;
            this.currentClipDuration.length = duration;
            invalidate();
        }
    }

    public void setColor(int duration, int select, int offset, int backgroundColor) {
        this.durationColor = duration;
        this.selectColor = select;
        this.offsetColor = offset;
        this.backgroundColor = backgroundColor;
    }

    class DrawInfo{
        int length;
        DrawType drawType = DrawType.DURATION;
    }
    enum  DrawType{
        OFFSET,
        DURATION,
        SELECT
    }
}
