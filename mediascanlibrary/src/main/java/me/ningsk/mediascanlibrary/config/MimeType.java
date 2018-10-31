package me.ningsk.mediascanlibrary.config;

import android.content.Context;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.ningsk.mediascanlibrary.R;


public class MimeType {

    public static final int ALL = 0;
    public static final int AUDIO = 6;
    public static final int PHOTO = 2;
    public static final int VIDEO = 4;
    public static final int OTHER = 5;
    public int mimeType = PHOTO;

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * 根据不同的类型，返回不同的错误提示
     *
     * @param mediaMimeType
     * @return
     */
    public static String s(Context context, int mediaMimeType) {
        Context ctx = context.getApplicationContext();
        switch (mediaMimeType) {
            case MimeType.PHOTO:
                return ctx.getString(R.string.photo_error);
            case MimeType.VIDEO:
                return ctx.getString(R.string.photo_video_error);

            default:
                return ctx.getString(R.string.photo_error);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public  @interface MediaMimeType{}
}

