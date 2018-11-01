package me.ningsk.mediascanlibrary.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;

import java.util.ArrayList;

import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.compress.LuBan;
import me.ningsk.mediascanlibrary.entity.LocalMedia;


public final class SelectionOptions implements Parcelable {
    @MimeType.MediaMimeType
    public int mimeType;  // 媒体库mime类型
    @StyleRes
    public int themeId;
    public int maxSelectNum;  // 最多选择数量
    public int minSelectNum; // 最小选择数量
    public int gridSize;  // RecyclerView显示网格大小
    public boolean supportDarkStatusBar;  // 是否支持暗色状态栏
    public boolean previewPhoto;  // 是否需要点击预览图片
    public boolean showGif;  // 是否显示gif图片
    public boolean showGifFlag;  // 是否显示gif标志
    @DrawableRes
    public int gifFlagResId = R.drawable.ic_gif_flag;  // gif标志resId
    public boolean canceledOnTouchOutside = true;  // 是否点击空白区域取消PhotoSelector
    public ArrayList<LocalMedia> selectedItems;
    public boolean isCompress;  // 是否压缩图片
    public int compressMode = PhotoSelectorConfig.SYSTEM_COMPRESS_MODE;  // 压缩模式
    public int compressMaxSize = PhotoSelectorConfig.MAX_COMPRESS_SIZE;  // 单位kb
    public int compressGrade = LuBan.CUSTOM_GEAR;
    public int compressHeight;
    public int compressWidth;
    public boolean isFirstCheck; // 是否默认选中第一项
    public int videoMinSecond = 0; //  显示多少秒以内的视频or音频也可适用
    public int videoMaxSecond = 0; //  显示多少秒以内的视频or音频也可适用
    public int overrideWidth; //int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
    public int overrideHeight; // int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
    public float sizeMultiplier; // glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
    public boolean zoomAnim; //  图片列表点击缩放效果
    public int selectionMode = PhotoSelectorConfig.MULTIPLE;

    private SelectionOptions() {
    }

    public static SelectionOptions getOptions() {
        return SelectionOptionsHolder.OPTIONS;
    }

    public static SelectionOptions getCleanOptions() {
        SelectionOptions options = SelectionOptionsHolder.OPTIONS;
        options.reset();
        return options;
    }

    private void reset() {
        mimeType = MimeType.PHOTO;
        selectionMode = PhotoSelectorConfig.MULTIPLE;
        maxSelectNum = 9;
        minSelectNum = 0;
        videoMaxSecond = 0;
        videoMinSecond = 0;
        overrideWidth = 0;
        overrideHeight = 0;
        gridSize = 3;
        sizeMultiplier = 0.5f;
        supportDarkStatusBar = false;
        previewPhoto = false;
        showGif = false;
        showGifFlag = false;
        gifFlagResId = R.drawable.ic_gif_flag;
        canceledOnTouchOutside = true;
        if (selectedItems != null) {
            selectedItems.clear();
            selectedItems = null;
        }
        isCompress = false;
        compressMode = PhotoSelectorConfig.SYSTEM_COMPRESS_MODE;
        compressMaxSize = PhotoSelectorConfig.MAX_COMPRESS_SIZE;
        compressGrade = LuBan.CUSTOM_GEAR;
        compressHeight = 0;
        compressWidth = 0;
        isFirstCheck = false;
        zoomAnim = true;
    }

    private static class SelectionOptionsHolder {
        private static final SelectionOptions OPTIONS = new SelectionOptions();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mimeType);
        dest.writeInt(this.themeId);
        dest.writeInt(this.maxSelectNum);
        dest.writeInt(this.minSelectNum);
        dest.writeInt(this.gridSize);
        dest.writeByte(this.supportDarkStatusBar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.previewPhoto ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showGif ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showGifFlag ? (byte) 1 : (byte) 0);
        dest.writeInt(this.gifFlagResId);
        dest.writeByte(this.canceledOnTouchOutside ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.selectedItems);
        dest.writeByte(this.isCompress ? (byte) 1 : (byte) 0);
        dest.writeInt(this.compressMode);
        dest.writeInt(this.compressMaxSize);
        dest.writeInt(this.compressGrade);
        dest.writeInt(this.compressHeight);
        dest.writeInt(this.compressWidth);
        dest.writeByte(this.isFirstCheck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.videoMinSecond);
        dest.writeInt(this.videoMaxSecond);
        dest.writeInt(this.overrideWidth);
        dest.writeInt(this.overrideHeight);
        dest.writeFloat(this.sizeMultiplier);
        dest.writeByte(this.zoomAnim ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selectionMode);
    }

    protected SelectionOptions(Parcel in) {
        this.mimeType = in.readInt();
        this.themeId = in.readInt();
        this.maxSelectNum = in.readInt();
        this.minSelectNum = in.readInt();
        this.gridSize = in.readInt();
        this.supportDarkStatusBar = in.readByte() != 0;
        this.previewPhoto = in.readByte() != 0;
        this.showGif = in.readByte() != 0;
        this.showGifFlag = in.readByte() != 0;
        this.gifFlagResId = in.readInt();
        this.canceledOnTouchOutside = in.readByte() != 0;
        this.selectedItems = in.createTypedArrayList(LocalMedia.CREATOR);
        this.isCompress = in.readByte() != 0;
        this.compressMode = in.readInt();
        this.compressMaxSize = in.readInt();
        this.compressGrade = in.readInt();
        this.compressHeight = in.readInt();
        this.compressWidth = in.readInt();
        this.isFirstCheck = in.readByte() != 0;
        this.videoMinSecond = in.readInt();
        this.videoMaxSecond = in.readInt();
        this.overrideWidth = in.readInt();
        this.overrideHeight = in.readInt();
        this.sizeMultiplier = in.readFloat();
        this.zoomAnim = in.readByte() != 0;
        this.selectionMode = in.readInt();
    }

    public static final Parcelable.Creator<SelectionOptions> CREATOR = new Parcelable.Creator<SelectionOptions>() {
        @Override
        public SelectionOptions createFromParcel(Parcel source) {
            return new SelectionOptions(source);
        }

        @Override
        public SelectionOptions[] newArray(int size) {
            return new SelectionOptions[size];
        }
    };
}
