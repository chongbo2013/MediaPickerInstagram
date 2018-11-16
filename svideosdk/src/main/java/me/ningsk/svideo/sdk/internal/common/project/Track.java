package me.ningsk.svideo.sdk.internal.common.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 44<br>
 * 版本：v1.0<br>
 */
public final class Track
{
    public String id;
    private final ArrayList<Clip> _ClipList = new ArrayList();
    private long _DurationNano;
    private float _Volume = 1.0F;

    public void setClipArray(Clip[] clip_list)
    {
        this._ClipList.clear();
        this._ClipList.addAll(Arrays.asList(clip_list));

        updateDuration();
    }
    public List<Clip> getClipList() {
        return this._ClipList;
    }
    Clip[] getClipArray() {
        return (Clip[])this._ClipList.toArray(new Clip[0]);
    }

    public void addClip(Clip clip) {
        this._ClipList.add(clip);
        this._DurationNano += clip.getDurationMilli();
    }

    public long getDurationMilli()
    {
        return TimeUnit.NANOSECONDS.toMillis(this._DurationNano);
    }
    public long getDuration() { return this._DurationNano; }

    private void updateDuration()
    {
        long duration = 0L;
        for (Clip clip : this._ClipList) {
            duration += clip.getDurationMilli();
        }

        this._DurationNano = duration;
    }
    public boolean isEmpty() {
        return this._ClipList.isEmpty();
    }
    public boolean validate() {
        for (Clip clip : this._ClipList) {
            if (!clip.validate()) {
                return false;
            }
        }
        return true;
    }
    public Iterable<Clip> getClipIterable() {
        return this._ClipList;
    }
    public Clip getClip(int i) { return (Clip)this._ClipList.get(i); }

    public Clip removeLastClip() {
        if (this._ClipList.size() == 0) {
            return null;
        }
        Clip clip = (Clip)this._ClipList.remove(this._ClipList.size() - 1);
        updateDuration();
        return clip;
    }

    public Clip removeClip(int index) {
        Clip clip = (Clip)this._ClipList.remove(index - 1);
        updateDuration();
        return clip;
    }

    public void removeAllClip() {
        this._ClipList.clear();
        updateDuration();
    }
    public int getClipCount() {
        return this._ClipList.size();
    }
    public Clip getLastClip() { return (Clip)this._ClipList.get(this._ClipList.size() - 1); }

    public float getVolume()
    {
        return this._Volume;
    }
    public void setVolume(float value) { this._Volume = value; }

}