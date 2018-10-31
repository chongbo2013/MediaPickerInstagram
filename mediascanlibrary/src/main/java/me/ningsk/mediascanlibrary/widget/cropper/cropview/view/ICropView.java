package me.ningsk.mediascanlibrary.widget.cropper.cropview.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;

public interface ICropView {


    boolean isFillMode();

    void setFillMode(boolean isFillMode);

    void getCroppedBitmap(CropperCallback callback);


    void setCropType(CopyType type);

    void setIsAdvancedMode(boolean isAdvancedMode);


    void setImageBitmap(Bitmap bitmap);

    void setImageDrawable(Drawable drawable);

    void setImageResource(@DrawableRes int resId);

    void setImageUri(Uri uri);

    void setImageFile(String path);

    void setOnFillableChangeListener(OnFillableChangeListener l);

    interface OnFillableChangeListener {
        void onFillableChange(boolean isFillable);
    }
}

