package me.ningsk.cameralibrary.engine.recorder;

import android.os.Parcel;
import android.os.Parcelable;

import me.ningsk.utilslibrary.utils.FileUtils;

/**
 * <p>描述：分段录制信息<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 20<br>
 * 版本：v1.0<br>
 */
public class RecordItem implements Parcelable {

    public static final Creator<RecordItem> CREATOR = new Creator<RecordItem>() {
        @Override
        public RecordItem createFromParcel(Parcel source) {
            return new RecordItem(source);
        }

        @Override
        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };

    // 视频路径
    public String mediaPath;
    // 视频长度
    public int duration;

    public RecordItem() {
    }

    public RecordItem(Parcel source) {
        mediaPath = source.readString();
        duration = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mediaPath);
        dest.writeInt(duration);
    }

    // 删除视频
    public void delete() {
        FileUtils.deleteFile(mediaPath);
        duration = 0;
        mediaPath = null;
    }

    /**
     * 获取时长
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 获取媒体路径
     * @return
     */
    public String getMediaPath() {
        return mediaPath;
    }
}

