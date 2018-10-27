package me.ningsk.photoselector.widget.cropper.cropview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import me.ningsk.photoselector.R;
import me.ningsk.photoselector.widget.cropper.cropview.utils.PxUtil;


public class DisplayHelper implements IDisplayHelper, ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnDoubleTapListener {


    private Context mContext;
    private View mParentView;
    private float[] mSuppMatrixValues = new float[9];
    private Bitmap mSrcBitmap;
    private RectF mCropArea;
    private Paint mBitmapPaint;
    private Matrix mBaseMatrix;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private int mSrcBitmapWidth;
    private int mSrcBitmapHeight;
    private float mMaxScale = 3.f;
    private float mNormalScale = 1.f;
    private float mMinScale;
    private Matrix mSuppMatrix;
    private Matrix mDrawMatrix;
    private final RectF mDisplayRect = new RectF();
    private float mScaleFocusX;
    private float mScaleFocusY;
    private Paint mBufferPaint;
    private MeshHelper mMeshHelper;
    private RectF mOrgBitmapRect;
    private MatrixTranslateHelper mMatrixTranslateHelper;
    private ObjectAnimator mTranslateAnimator;
    private boolean mIsFiling;
    private int mMaximumVelocity;
    private FilingRunnable mFilingRunnable;
    private boolean mIsFillMode = true;//is bitmap fill crop area --- bitmap >= crop area
    private int mWidth, mHeight;
    private final int mBufferColor = 0xaa0f0f0f;
    private int mBackgroundColor = Color.TRANSPARENT;
    private float mMaxOverScroll;
    private boolean mIsAdvancedMode = false;
    private RectTranslateHelper mRectTranslateHelper;
    private ObjectAnimator mCropChangeAnimator;
    private ICropView.OnFillableChangeListener mOnFillableChangeListener;


    public DisplayHelper(Context context, View parentView) {
        mContext = context;
        mParentView = parentView;
        mMaxOverScroll = PxUtil.dip2px(mParentView.getResources(), 400);
        mMaximumVelocity = 2100;
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, new MoveAndClickGestureDetector());
        mGestureDetector.setOnDoubleTapListener(this);
        initPaint();
        initMatrix();
        mMeshHelper = new MeshHelper();
        mMatrixTranslateHelper = new MatrixTranslateHelper();

    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBufferPaint = new Paint();
        mBufferPaint.setColor(mBufferColor);
        mBufferPaint.setAntiAlias(true);
        mBufferPaint.setStyle(Paint.Style.FILL);
    }

    private void initMatrix() {
        mBaseMatrix = new Matrix();
        mSuppMatrix = new Matrix();
        mDrawMatrix = new Matrix();
    }

    private final PorterDuffXfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        if (mSrcBitmap == null)
            return;

        int save = canvas.save();
        canvas.concat(mDrawMatrix);
        canvas.drawBitmap(mSrcBitmap, 0, 0, mBitmapPaint);
        canvas.restoreToCount(save);

        //draw mesh
        mMeshHelper.drawMesh(canvas);

        // draw buffer
        final int saveLayerCount = canvas.saveLayer(0, 0, mWidth, mHeight, mBufferPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(0, 0, mWidth, mHeight, mBufferPaint);
        mBufferPaint.setXfermode(mXfermode);
        canvas.drawRect(mCropArea, mBufferPaint);
        mBufferPaint.setXfermode(null);
        canvas.restoreToCount(saveLayerCount);
    }

    @Override
    public void getCroppedBitmap(CropperCallback callback) {
        if (mCropChangeAnimator != null && mCropChangeAnimator.isRunning()) {
            mCropChangeAnimator.end();
        }
        if (mTranslateAnimator != null && mTranslateAnimator.isRunning()) {
            mTranslateAnimator.end();
        }
        if (callback == null) {
            callback.onError();
            return;
        }

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        final Rect showRect = getShowRect();
        if (showRect.width() <= 0 || showRect.height() <= 0) {
            callback.onError();
            return;
        }
        Bitmap output = Bitmap.createBitmap(Math.round(mCropArea.width()), Math.round(mCropArea.height()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawColor(mBackgroundColor);
        canvas.translate(-mCropArea.left, -mCropArea.top);
        canvas.concat(mDrawMatrix);
        canvas.drawBitmap(mSrcBitmap, 0, 0, paint);
        callback.onCropped(output);

    }


    public static Bitmap  getCroppedBitmap(Rect cropArea, int backgroundColor, Matrix drawMatrix, Bitmap bitmap) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Bitmap output = Bitmap.createBitmap(Math.round(cropArea.width()), Math.round(cropArea.height()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawColor(backgroundColor);
        canvas.translate(-cropArea.left, -cropArea.top);
        canvas.concat(drawMatrix);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return output;
    }


    @Override
    public boolean processTouchEvent(MotionEvent event) {
        if (mSrcBitmap == null) {
            try {
                return mParentView.onTouchEvent(event);
            } catch (Exception e) {
                return false;
            }
        }


        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsFiling = false;
                cancelAnimation();
                showMesh();
                break;
        }
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        if (action == MotionEvent.ACTION_UP && !mIsFiling) {
            checkDisplayArea();
        }
        return true;
    }

    @Override
    public void setBitmap(Bitmap srcBitmap) {
        this.mSrcBitmap = srcBitmap;
        mIsFillMode = true;
        cancelAnimation();
        resetMatrix();
        dismissMesh();
    }

    private void cancelAnimation() {
        if (mFilingRunnable != null) {
            mFilingRunnable.cancelFling();
        }
        if (mTranslateAnimator != null && mTranslateAnimator.isRunning()) {
            mTranslateAnimator.cancel();
        }
    }

    @Override
    public void setCropArea(RectF cropArea) {
        if (mCropArea != null && mCropArea.equals(cropArea))
            return;

        mIsFillMode = true;
        mWidth = mParentView.getMeasuredWidth();
        mHeight = mParentView.getMeasuredHeight();
        if (mCropArea == null) {
            mCropArea = cropArea;
            resetMatrix();
        } else {
            if (mRectTranslateHelper == null) {
                mRectTranslateHelper = new RectTranslateHelper();
            }
            mRectTranslateHelper.setDstRestF(cropArea);
            if (mCropChangeAnimator == null) {
                mCropChangeAnimator = ObjectAnimator.ofFloat(mRectTranslateHelper, "translateFactor", 1.0f);
                mCropChangeAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mMeshHelper.isMeshShow()) {
                            dismissMesh();
                        }
                    }
                });
            }
            mCropChangeAnimator.start();
        }


    }

    public void resetMatrixFit(Matrix baseMatrix, Matrix drawMatrix, Matrix suppMatrix) {
        if (mSrcBitmap == null || mCropArea == null) {
            ViewCompat.postInvalidateOnAnimation(mParentView);
            return;
        }
        float cropWidth = mCropArea.width();
        float cropHeight = mCropArea.height();
        mSrcBitmapWidth = mSrcBitmap.getWidth();
        mSrcBitmapHeight = mSrcBitmap.getHeight();
        RectF srcRect = new RectF(0, 0, mSrcBitmapWidth, mSrcBitmapHeight);
        float newDrawableHeight;
        float newDrawableWidth;
        final boolean isVertical = mSrcBitmapHeight / cropHeight > mSrcBitmapWidth / cropWidth;
        if (isVertical) {
            newDrawableWidth = cropWidth;
            newDrawableHeight = mSrcBitmapHeight * (cropWidth / mSrcBitmapWidth);

        } else {
            newDrawableWidth = mSrcBitmapWidth * (cropHeight / mSrcBitmapHeight);
            newDrawableHeight = cropHeight;
        }

        float dx = mCropArea.left - (newDrawableWidth - cropWidth) * 0.5f;
        float dy = mCropArea.top - (newDrawableHeight - cropHeight) * 0.5f;
        //original rect of bitmap
        mOrgBitmapRect = new RectF(dx, dy, dx + newDrawableWidth, dy + newDrawableHeight); //normal scale rect
        mBaseMatrix.setRectToRect(srcRect, mOrgBitmapRect, Matrix.ScaleToFit.CENTER);
        mSuppMatrix.reset();

        if (isVertical) {
            mMinScale = cropHeight / newDrawableHeight;
        } else {
            mMinScale = cropWidth / newDrawableWidth;
        }

        if (mOnFillableChangeListener != null) {
            boolean isFillable = false;
            if (mIsAdvancedMode) {
                isFillable = (mCropArea.width() / cropHeight) != (mOrgBitmapRect.width() / mOrgBitmapRect.height());
            }
            mOnFillableChangeListener.onFillableChange(isFillable);
            if (suppMatrix == null) {
                return;
            }
            Matrix resetMatrix = new Matrix(mSuppMatrix);
            float scaleX = getMatrixValueS(suppMatrix, 0);
            float scaleY = getMatrixValueS(suppMatrix, 4);
            float transX = getMatrixValueS(suppMatrix, 2);
            float transY = getMatrixValueS(suppMatrix, 5);
            resetMatrix.postScale(scaleX, scaleY);
            resetMatrix.postTranslate(transX, transY);
            startTranslateAnimator(resetMatrix);
            Log.e("scale", "" + mSuppMatrix);
            Log.e("scale", "" + resetMatrix);

        }
    }

    public void setBitmapFit(Bitmap srcBitmap, Matrix baseMatrix, Matrix drawMatrix, Matrix suppMatrix, float scaleFocusX, float scaleFocusY) {
        mSrcBitmap = srcBitmap;
        mIsFillMode = true;
        mScaleFocusX = scaleFocusX;
        mScaleFocusY = scaleFocusY;
        cancelAnimation();
        resetMatrixFit(baseMatrix, drawMatrix, suppMatrix);
        dismissMesh();
    }

    @SuppressWarnings("unused")
    private class RectTranslateHelper {
        private RectF mDstRect;

        public float getTranslateFactor() {
            return 0.f;
        }

        public void setDstRestF(RectF dst) {
            mDstRect = dst;
        }

        public void setTranslateFactor(@FloatRange(from = 0.0, to = 1.0) float translateFactor) {
            mCropArea.left = mCropArea.left + (mDstRect.left - mCropArea.left) * translateFactor;
            mCropArea.top = mCropArea.top + (mDstRect.top - mCropArea.top) * translateFactor;
            mCropArea.right = mCropArea.right + (mDstRect.right - mCropArea.right) * translateFactor;
            mCropArea.bottom = mCropArea.bottom + (mDstRect.bottom - mCropArea.bottom) * translateFactor;
            resetMatrix();
        }

    }



    public Rect getShowRect() {
        if (mCropChangeAnimator != null && mCropChangeAnimator.isRunning())
            mCropChangeAnimator.end();
        if (mTranslateAnimator != null && mTranslateAnimator.isRunning())
            this.mTranslateAnimator.end();
        Rect showRect = new Rect();
        RectF drawRect = new RectF();
        drawRect.set(0.0F, 0.0F, mSrcBitmapWidth, mSrcBitmapHeight);
        mDrawMatrix.mapRect(drawRect);
        int left = Math.round(Math.max(drawRect.left, this.mCropArea.left));
        int top = Math.round(Math.max(drawRect.top, this.mCropArea.top));
        int right = Math.round(Math.max(drawRect.right, this.mCropArea.right));
        int bottom = Math.round(Math.max(drawRect.bottom, this.mCropArea.bottom));
        showRect.set(left, top, right, bottom);
        return showRect;
    }

    public Matrix getBaseMatrix() {
        return mBaseMatrix;
    }

    public Matrix getDrawMatrix() {
        return mDrawMatrix;
    }

    public float getScaleFocusX() {
        return mScaleFocusX;
    }

    public float getScaleFocusY() {
        return mScaleFocusY;
    }

    public Matrix getSuppMatrix() {
        return mSuppMatrix;
    }

    @Override
    public void setOnFillableChangeListener(ICropView.OnFillableChangeListener l) {
        mOnFillableChangeListener = l;
    }

    @Override
    public void setIsAdvancedMode(boolean isAdvancedMode) {
        if (mIsAdvancedMode != isAdvancedMode) {
            mIsAdvancedMode = isAdvancedMode;
            mIsFillMode = true;
            cancelAnimation();
            resetMatrix();
            dismissMesh();
        }
    }

    @Override
    public boolean isFillMode() {
        return mIsFillMode;
    }

    @Override
    public void setIsFillMode(boolean isFillMode) {
        if (!mIsAdvancedMode)
            return;


        if (mIsFillMode == isFillMode)
            return;
        mIsFillMode = isFillMode;
        if (mSrcBitmap == null)
            return;
        cancelAnimation();
        final Matrix finalSuppMatrix;
        //如果mIsFillMode == true  则没必要做运算
        if (mIsFillMode) {
            finalSuppMatrix = new Matrix();
        } else {
            finalSuppMatrix = getMinimumFinalSuppMatrix();
        }
        startTranslateAnimator(finalSuppMatrix);
    }

    private Matrix getMinimumFinalSuppMatrix() {
        Matrix finalSuppMatrix = new Matrix();
        finalSuppMatrix.setScale(mMinScale, mMinScale, mCropArea.centerX(), mCropArea.centerY());
        return finalSuppMatrix;
    }

    protected void resetMatrix() {
        if (mSrcBitmap == null || mCropArea == null) {
            ViewCompat.postInvalidateOnAnimation(mParentView);
            return;
        }
        final float cropWidth = mCropArea.width();
        final float cropHeight = mCropArea.height();
        float newDrawabelHeight;
        float newDrawabelWidth;
        mSrcBitmapWidth = mSrcBitmap.getWidth();
        mSrcBitmapHeight = mSrcBitmap.getHeight();
        RectF srcRect = new RectF(0, 0, mSrcBitmapWidth, mSrcBitmapHeight);
        final boolean isVertical = mSrcBitmapHeight / cropHeight > mSrcBitmapWidth / cropWidth;
        if (isVertical) {
            newDrawabelWidth = cropWidth;
            newDrawabelHeight = mSrcBitmapHeight * (cropWidth / mSrcBitmapWidth);

        } else {
            newDrawabelWidth = mSrcBitmapWidth * (cropHeight / mSrcBitmapHeight);
            newDrawabelHeight = cropHeight;
        }

        float dx = mCropArea.left - (newDrawabelWidth - cropWidth) * 0.5f;
        float dy = mCropArea.top - (newDrawabelHeight - cropHeight) * 0.5f;
        //original rect of bitmap
        mOrgBitmapRect = new RectF(dx, dy, dx + newDrawabelWidth, dy + newDrawabelHeight); //normal scale rect
        mBaseMatrix.setRectToRect(srcRect, mOrgBitmapRect, Matrix.ScaleToFit.CENTER);
        mSuppMatrix.reset();

        if (isVertical) {
            mMinScale = cropHeight / newDrawabelHeight;
        } else {
            mMinScale = cropWidth / newDrawabelWidth;
        }

        if (mOnFillableChangeListener != null) {
            boolean isFillable = false;
            if (mIsAdvancedMode) {
                isFillable = (mCropArea.width() / cropHeight) != (mOrgBitmapRect.width() / mOrgBitmapRect.height());
            }
            mOnFillableChangeListener.onFillableChange(isFillable);
        }

        updateDrawMatrix();
    }


    private void updateDrawMatrix() {
        mDrawMatrix.set(mBaseMatrix);
        mDrawMatrix.postConcat(mSuppMatrix);
        mMeshHelper.updateMeshRect(mDrawMatrix);
        showMesh();
        ViewCompat.postInvalidateOnAnimation(mParentView);
    }

    private void checkDisplayArea() {
        final RectF displayRect = getDisplayRect();
        float displayWidth = displayRect.width();
        float displayHeight = displayRect.height();
        Matrix finalSuppMatrix = null;
        final float cropWidth = Math.round(mCropArea.width());
        final float cropHeight = Math.round(mCropArea.height());
        final float currentScale = getScale();
        if (!mIsAdvancedMode) {
            if (currentScale < mNormalScale) {
                float scale = mOrgBitmapRect.width() / displayRect.width();
                finalSuppMatrix = new Matrix(mSuppMatrix);
                finalSuppMatrix.postScale(scale, scale, mScaleFocusX, mScaleFocusY);
                //check out of area
                final RectF tempRect = new RectF(mOrgBitmapRect);
                finalSuppMatrix.mapRect(tempRect);
                tempRect.offset(getHorizontalOffset(tempRect), getVerticalOffset(tempRect));
                //reset finalSuppMatrix
                finalSuppMatrix.setRectToRect(mOrgBitmapRect, tempRect, Matrix.ScaleToFit.CENTER);
            } else if (!displayRect.contains(mCropArea)) {
                finalSuppMatrix = getOffsetRecoverMatrix(displayRect);
            }
        } else {
            if (currentScale < mMinScale) {
                mIsFillMode = false;
                finalSuppMatrix = getMinimumFinalSuppMatrix();
            }
            //  minRect(mMinScale) < displayRect < mCropArea
            //  ps:此方法只会在mIsFillMode = false 时才会生效, 因为此时才可能mMinimumAreaRect != mCropArea
            else if (currentScale < mNormalScale) {
                mIsFillMode = false;
                float dx, dy;
                final RectF dstRect = new RectF();
                dstRect.set(displayRect);
                if (displayWidth < cropWidth) {
                    dy = getVerticalOffset(displayRect);
                    float dstLeft = mCropArea.left + (cropWidth - displayWidth) * 0.5f;
                    dstRect.set(dstLeft, dstRect.top + dy, dstLeft + displayWidth, dstRect.bottom + dy);
                } else if (displayHeight < cropHeight) {
                    dx = getHorizontalOffset(displayRect);
                    float dstTop = mCropArea.top + (cropHeight - displayHeight) * 0.5f;
                    dstRect.set(dstRect.left + dx, dstTop, dstRect.right + dx, dstTop + displayHeight);
                }
                finalSuppMatrix = new Matrix();
                finalSuppMatrix.setRectToRect(mOrgBitmapRect, dstRect, Matrix.ScaleToFit.CENTER);
            }
            //mCropArea < displayRect,判断边界是否超出mCropArea
            else if (!displayRect.contains(mCropArea)) {
                finalSuppMatrix = getOffsetRecoverMatrix(displayRect);
            }
        }
        if (finalSuppMatrix != null) {
            startTranslateAnimator(finalSuppMatrix);
        } else {
            dismissMesh();
        }
    }

    @NonNull
    private Matrix getOffsetRecoverMatrix(RectF displayRect) {
        Matrix finalSuppMatrix;
        float dx = getHorizontalOffset(displayRect);
        float dy = getVerticalOffset(displayRect);
        finalSuppMatrix = new Matrix(mSuppMatrix);
        finalSuppMatrix.postTranslate(dx, dy);
        return finalSuppMatrix;
    }

    private void startTranslateAnimator(Matrix finalSuppMatrix) {
        mMatrixTranslateHelper.setFinalSuppMatrix(finalSuppMatrix);
        if (mTranslateAnimator == null) {
            mTranslateAnimator = ObjectAnimator.ofFloat(mMatrixTranslateHelper, "translateFactor", 1.0f);
            mTranslateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mMeshHelper.isMeshShow()) {
                        dismissMesh();
                    }
                }
            });
        }
        mTranslateAnimator.start();
    }

    private float getVerticalOffset(RectF displayRect) {
        float dy = 0;
        if (displayRect.top > mCropArea.top) {
            dy = mCropArea.top - displayRect.top;
        } else if (displayRect.bottom < mCropArea.bottom) {
            dy = mCropArea.bottom - displayRect.bottom;
        }
        return dy;
    }

    private float getHorizontalOffset(RectF displayRect) {
        float dx = 0;
        if (displayRect.left > mCropArea.left) {
            dx = mCropArea.left - displayRect.left;
        } else if (displayRect.right < mCropArea.right) {
            dx = mCropArea.right - displayRect.right;
        }
        return dx;
    }

    private RectF getDisplayRect() {
        mDisplayRect.set(0, 0, mSrcBitmapWidth, mSrcBitmapHeight);
        mDrawMatrix.mapRect(mDisplayRect);
        return mDisplayRect;
    }

    public static float getMatrixValueS(Matrix matrix, int value) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[value];
    }


    private void onDrag(float dx, float dy) {
        mSuppMatrix.postTranslate(dx, dy);
        updateDrawMatrix();
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        if (getScale() < mMaxScale || scaleFactor < 1.f) {
            mScaleFocusX = detector.getFocusX();
            mScaleFocusY = detector.getFocusY();
            mSuppMatrix.postScale(scaleFactor, scaleFactor, mScaleFocusX, mScaleFocusY);
            updateDrawMatrix();
        }

        return true;

    }

    private float getScale() {
        return (float) Math.sqrt((float) Math.pow(getValue(mSuppMatrix, Matrix.MSCALE_X), 2)
                + (float) Math.pow(getValue(mSuppMatrix, Matrix.MSKEW_Y), 2));
    }

    public void limitCropAreaToCurrent() {
        mCropArea = new RectF(getShowRect());
        resetMatrix();
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {//NO-OP
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


    private float[] getValues(Matrix matrix, float[] floats) {
        matrix.getValues(floats);
        return floats;
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mSuppMatrixValues);
        return mSuppMatrixValues[whichValue];
    }


    @SuppressWarnings("unused")
    private class MatrixTranslateHelper {
        private float srcTranX, srcTranY, srcScaleX, srcScaleY;
        private float[] dstMatrixValues = new float[9];

        public float getTranslateFactor() {
            return 0.f;
        }

        public void setFinalSuppMatrix(Matrix dstMatrix) {
            mSuppMatrixValues = getValues(mSuppMatrix, mSuppMatrixValues);
            srcTranX = mSuppMatrixValues[Matrix.MTRANS_X];
            srcTranY = mSuppMatrixValues[Matrix.MTRANS_Y];
            srcScaleX = mSuppMatrixValues[Matrix.MSCALE_X];
            srcScaleY = mSuppMatrixValues[Matrix.MSCALE_Y];
            dstMatrixValues = getValues(dstMatrix, dstMatrixValues);
        }

        public void setTranslateFactor(@FloatRange(from = 0.0, to = 1.0) float translateFactor) {
            mSuppMatrixValues[Matrix.MTRANS_X] = srcTranX + (dstMatrixValues[Matrix.MTRANS_X] - srcTranX) * translateFactor;
            mSuppMatrixValues[Matrix.MTRANS_Y] = srcTranY + (dstMatrixValues[Matrix.MTRANS_Y] - srcTranY) * translateFactor;
            mSuppMatrixValues[Matrix.MSCALE_X] = srcScaleX + (dstMatrixValues[Matrix.MSCALE_X] - srcScaleX) * translateFactor;
            mSuppMatrixValues[Matrix.MSCALE_Y] = srcScaleY + (dstMatrixValues[Matrix.MSCALE_Y] - srcScaleY) * translateFactor;
            mSuppMatrix.setValues(mSuppMatrixValues);
            updateDrawMatrix();
        }

    }


    private class MoveAndClickGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            onDrag(-distanceX, -distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent e2, float velocityX, float velocityY) {
            mIsFiling = true;
            mFilingRunnable = new FilingRunnable(mContext);
            velocityX = Math.max(-mMaximumVelocity, Math.min(velocityX, mMaximumVelocity));
            velocityY = Math.max(-mMaximumVelocity, Math.min(velocityY, mMaximumVelocity));
            mFilingRunnable.fling((int) velocityX, (int) velocityY);
            ViewCompat.postOnAnimation(mParentView, mFilingRunnable);
            return true;
        }
    }


    private class FilingRunnable implements Runnable {

        private int mLastFlingX, mLastFlingY;
        private OverScroller mScroller;

        public FilingRunnable(Context context) {
            mScroller = new OverScroller(context);
//            mScroller.setFriction(0.03f);
        }

        public void fling(int velocityX, int velocityY) {
            mLastFlingX = mLastFlingY = 0;
            final RectF displayRect = getDisplayRect();
            int minX = (int) Math.min(0, -(displayRect.right - mCropArea.right));
            int minY = (int) Math.min(0, -(displayRect.bottom - mCropArea.bottom));
            int maxX = (int) Math.max(0, -(displayRect.left - mCropArea.left));
            int maxY = (int) Math.max(0, -(displayRect.top - mCropArea.top));

            //TODO overScroll
            mScroller.fling(0, 0, velocityX, velocityY,
                    minX, maxX, minY, maxY);
//            mScroller.fling(0, 0, velocityX, velocityY,
//                    minX, maxX, minY, maxY, (int)mMaxOverScroll,(int)mMaxOverScroll);
        }

        public void cancelFling() {
            mScroller.forceFinished(true);
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();

                onDrag(newX - mLastFlingX, newY - mLastFlingY);
                mLastFlingX = newX;
                mLastFlingY = newY;

                if (mScroller.isFinished()) {
                    mIsFiling = false;
                    checkDisplayArea();
                } else {
                    ViewCompat.postOnAnimation(mParentView, this);
                }
            }
        }
    }

    private void showMesh() {
        mMeshHelper.cancelAnimation();
        mMeshHelper.setAlpha(1.f);
    }

    private void dismissMesh() {
        mMeshHelper.dismissMesh();
    }

    @SuppressWarnings("unused")
    private class MeshHelper {
        private ObjectAnimator mDissmissMeshAnimator;
        private float mAlpha = 1.f;
        private Rect mMeshRect;
        private RectF mTempRect;
        private Drawable mMeshDrawable;

        public MeshHelper() {
            mMeshRect = new Rect();
            mTempRect = new RectF();
            mMeshDrawable = mParentView.getResources().getDrawable(R.drawable.bg_mesh);
            mDissmissMeshAnimator = ObjectAnimator.ofFloat(this, "Alpha", .0f);
            mDissmissMeshAnimator.setStartDelay(300);
        }

        public float getAlpha() {
            return mAlpha;
        }

        public void updateMeshRect(Matrix src) {
            mTempRect.set(0, 0, mSrcBitmapWidth, mSrcBitmapHeight);
            src.mapRect(mTempRect);
            int left = Math.round(Math.max(mTempRect.left, mCropArea.left));
            int right = Math.round(Math.min(mTempRect.right, mCropArea.right));
            int top = Math.round(Math.max(mTempRect.top, mCropArea.top));
            int bottom = Math.round(Math.min(mTempRect.bottom, mCropArea.bottom));
            mMeshRect.set(left, top, right, bottom);
        }

        public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
            this.mAlpha = alpha;
            ViewCompat.postInvalidateOnAnimation(mParentView);
        }


        public void dismissMesh() {
            mDissmissMeshAnimator.start();
        }

        public void cancelAnimation() {
            if (mDissmissMeshAnimator.isRunning())
                mDissmissMeshAnimator.cancel();
        }

        protected void drawMesh(Canvas canvas) {
            if (mMeshRect.width() <= 0 || mMeshRect.height() <= 0)
                return;
            mMeshDrawable.setAlpha(Math.round(255 * mAlpha));
            mMeshDrawable.setBounds(mMeshRect);
            mMeshDrawable.draw(canvas);
        }

        public boolean isMeshShow() {
            return mAlpha != 0;
        }
    }

}

