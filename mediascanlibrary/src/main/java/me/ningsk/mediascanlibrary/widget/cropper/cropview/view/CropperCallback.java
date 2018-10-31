package me.ningsk.mediascanlibrary.widget.cropper.cropview.view;

import android.graphics.Bitmap;

public abstract class CropperCallback {

    public abstract void onCropped(Bitmap bitmap);

    public void onError(){
    }

    public void onOutOfMemoryError() {
    }

    public void onStarted() {
    }


}