package me.ningsk.common.utils;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class VideoScaleHelper
{
    private static final float SCALE_SQUARE = 1.0F;
    private static final float SCALE_BROAD = 1.777778F;
    private static final float SCALE_NARROW = 0.8F;
    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;
    private int displayWidth;
    private int displayHeight;
    private int layoutWidth;
    private int layoutHeight;

    public VideoScaleHelper setScreenWidthAndHeight(int width, int height)
    {
        this.screenWidth = width;
        this.screenHeight = height;
        return this;
    }

    public VideoScaleHelper setVideoWidthAndHeight(int width, int height) {
        this.videoWidth = width;
        this.videoHeight = height;
        return this;
    }

    public void generateDisplayWidthAndHeight() {
        if ((this.screenHeight == 0) || (this.screenWidth == 0) || (this.videoHeight == 0) || (this.videoWidth == 0))
        {
            return;
        }
        int width = 0;
        int height = 0;
        float scaleWH = this.videoWidth / this.videoHeight;
        if (scaleWH > SCALE_BROAD) {
            height = (int)(this.screenWidth / SCALE_BROAD);
            width = (int)(scaleWH * height);
        } else if ((scaleWH <= SCALE_BROAD) && (scaleWH >= SCALE_SQUARE)) {
            width = this.screenWidth;
            height = (int)(width / scaleWH);
        } else if ((scaleWH < SCALE_BROAD) && (scaleWH >= SCALE_NARROW)) {
            height = this.screenHeight;
            width = (int)(height * scaleWH);
        } else {
            width = (int)(this.screenHeight * SCALE_NARROW);
            height = (int)(width / scaleWH);
        }
        this.displayWidth = width;
        this.displayHeight = height;
    }

    public void generateLayoutWidthAndHeight() {
        if ((this.screenHeight == 0) || (this.screenWidth == 0) || (this.videoHeight == 0) || (this.videoWidth == 0))
        {
            return;
        }
        int width = 0;
        int height = 0;
        float scaleWH = this.videoWidth / this.videoHeight;
        if (scaleWH > SCALE_BROAD) {
            height = (int)(this.screenWidth / SCALE_BROAD);
            width = (int)(SCALE_BROAD * height);
        } else if ((scaleWH <= SCALE_BROAD) && (scaleWH >= SCALE_SQUARE)) {
            width = this.screenWidth;
            height = (int)(width / scaleWH);
        } else if ((scaleWH < SCALE_SQUARE) && (scaleWH >= SCALE_NARROW)) {
            height = this.screenHeight;
            width = (int)(height * scaleWH);
        } else {
            width = (int)(this.screenHeight * SCALE_NARROW);
            height = (int)(width / SCALE_NARROW);
        }
        this.layoutWidth = width;
        this.layoutHeight = height;
    }

    public void generateLayoutParamsOnderWidth(ViewGroup.LayoutParams layoutParams) {
        if ((this.screenHeight == 0) || (this.screenWidth == 0) || (this.videoHeight == 0) || (this.videoWidth == 0))
        {
            return;
        }
        int width = 0;
        int height = 0;
        float scale = this.screenWidth / this.videoWidth;
        width = this.screenWidth;
        height = (int)(this.videoHeight * scale);
        layoutParams.width = width;
        layoutParams.height = height;
    }

    public void generatePhotoLayoutParams(FrameLayout.LayoutParams layoutParams) {
        generateLayoutWidthAndHeight();
        if ((this.layoutWidth == 0) || (this.layoutHeight == 0)) {
            return;
        }
        layoutParams.width = this.layoutWidth;
        layoutParams.height = this.layoutHeight;
        layoutParams.gravity = 17;
    }

    public void generateDisplayLayoutParams(FrameLayout.LayoutParams layoutParams) {
        generateDisplayWidthAndHeight();
        if ((this.displayHeight == 0) || (this.displayWidth == 0)) {
            return;
        }
        layoutParams.width = this.displayWidth;
        layoutParams.height = this.displayHeight;
        layoutParams.gravity = Gravity.CENTER;
    }

    public void generateSquareVideoLayout(FrameLayout.LayoutParams layoutParams) {
        if ((this.screenHeight == 0) || (this.screenWidth == 0) || (this.videoHeight == 0) || (this.videoWidth == 0))
        {
            return;
        }

        float scale_x = this.screenWidth / this.videoWidth;
        float scale_y = this.screenHeight / this.videoHeight;

        if ((scale_x == 0.0F) || (scale_y == 0.0F)) {
            return;
        }

        float scale = Math.max(scale_x, scale_y);

        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ((int)(scale * this.videoWidth));
        layoutParams.height = ((int)(scale * this.videoHeight));
    }

    public int getLayoutHeight() {
        return this.layoutHeight;
    }

    public int getLayoutWidth() {
        return this.layoutWidth;
    }

    public int getDisplayHeight() {
        return this.displayHeight;
    }

    public int getDisplayWidth() {
        return this.displayWidth;
    }
}
