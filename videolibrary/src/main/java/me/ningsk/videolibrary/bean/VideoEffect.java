package me.ningsk.videolibrary.bean;

import me.ningsk.filterlibrary.glfilter.base.GLImageFilter;

/**
 * <p>描述：视频特效绘制<p>
 * 作者：ningsk<br>
 * 日期：2018/10/31 09 23<br>
 * 版本：v1.0<br>
 */
public class VideoEffect {
    private int mStartPosition;         // 滤镜开始位置
    private int mFinishPosition;        // 滤镜结束位置
    private GLImageFilter mFilter;      // 滤镜对象

    public VideoEffect() {
        mStartPosition = 0;
        mFinishPosition = 0;
        mFilter = null;
    }

    /**
     * 获取特效起始位置
     * @return
     */
    public int getStartPosition() {
        return mStartPosition;
    }

    /**
     * 设置特效起始位置
     * @param startPosition
     */
    public void setStartPosition(int startPosition) {
        mStartPosition = startPosition;
    }

    /**
     * 获取特效结束位置
     * @return
     */
    public int getFinishPosition() {
        return mFinishPosition;
    }

    /**
     * 设置特效结束位置
     * @param finishPosition
     */
    public void setFinishPosition(int finishPosition) {
        mFinishPosition = finishPosition;
    }

    /**
     * 获取滤镜
     * @return
     */
    public GLImageFilter getFilter() {
        return mFilter;
    }

    /**
     * 设置滤镜
     * @param filter
     */
    public void setFilter(GLImageFilter filter) {
        mFilter = filter;
    }

}

