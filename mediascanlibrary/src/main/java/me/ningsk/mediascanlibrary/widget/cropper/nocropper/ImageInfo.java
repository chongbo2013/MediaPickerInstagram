package me.ningsk.mediascanlibrary.widget.cropper.nocropper;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class ImageInfo {
    private Matrix baseMatrix;
    private Bitmap bitmap;
    private int degree;
    private Matrix drawMatrix;
    private float mScaleFocusX;
    private float mScaleFocusY;
    private Matrix mSuppMatrix;
    private String path;
    private Rect showRect;

    public ImageInfo(String path, Rect showRect, Matrix drawMatrix, Matrix suppMatrix, float scaleFocusX, float scaleFocusY, Matrix baseMatrix) {
        this.path = path;
        this.showRect = new Rect(showRect);
        this.drawMatrix = new Matrix(drawMatrix);
        this.mSuppMatrix = new Matrix(suppMatrix);
        this.mScaleFocusX = scaleFocusX;
        this.mScaleFocusY = scaleFocusY;
        this.baseMatrix = baseMatrix;

    }

    public Matrix getBaseMatrix() {
        return baseMatrix;
    }

    public int getDegree() {
        return degree;
    }

    public Matrix getDrawMatrix() {
        return drawMatrix;
    }

    public String getPath() {
        return path;
    }

    public float getScaleFocusX() {
        return mScaleFocusX;
    }


    public float getScaleFocusY() {
        return mScaleFocusY;
    }

    public Rect getShowRect() {
        return showRect;
    }

    public Matrix getSuppMatrix() {
        return mSuppMatrix;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}

