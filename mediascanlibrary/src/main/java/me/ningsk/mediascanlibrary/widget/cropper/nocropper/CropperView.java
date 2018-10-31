package me.ningsk.mediascanlibrary.widget.cropper.nocropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.utils.StringUtils;
import me.ningsk.mediascanlibrary.widget.cropper.cropview.view.CropView;
import me.ningsk.mediascanlibrary.widget.cropper.cropview.view.CropperCallback;
import me.ningsk.mediascanlibrary.widget.cropper.cropview.view.DisplayHelper;


public class CropperView extends CropView {

    private static final String TAG = "CropperView";
    private LinkedHashMap<String, ImageInfo> cacheMap = new LinkedHashMap<>();
    private int currentRotateDegree = 0;
    private boolean enableTouchEvent;
    private boolean hasLimitArea = false;
    private volatile boolean isLoadingNewImage = false;
    private String lastPath = "";
    private Bitmap mBitmap;
    private Disposable addMultiImageDisposable;
    private Disposable loadNewImageDisposable;

    public CropperView(Context context) {
        super(context);
        init();
    }

    public CropperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    private Bitmap decodeFile(File file) {
        long originalTime = System.currentTimeMillis();
        int inSampleSize = 1;
        Bitmap bitmap;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            if (options.outWidth/ inSampleSize / 2 >= 1080 && options.outHeight / inSampleSize / 2 >= 1080) {
                inSampleSize *= 2;
            }
            options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        } catch (FileNotFoundException ex) {
            return null;
        } finally {
            Log.e(TAG, Thread.currentThread() + "cost time= " + (System.currentTimeMillis() - originalTime));
        }
        return bitmap;
    }

    private Bitmap rotateBitmap(int rotateDegree) {
        Bitmap bitmap;
        if (mBitmap == null) {
            return  null;
        }
        this.currentRotateDegree += rotateDegree;
        int width = this.mBitmap.getWidth();
        int height = this.mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree);
        bitmap = Bitmap.createBitmap(this.mBitmap, 0 , 0, width, height, matrix, false);
        if (!bitmap.equals(this.mBitmap)) {
            return bitmap;
        }

        return mBitmap;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int rotateDegree) {
        if (bitmap == null) {
            return bitmap;
        }
        Bitmap rotateBitmap;
        int width = this.mBitmap.getWidth();
        int height = this.mBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree);
        rotateBitmap = Bitmap.createBitmap(bitmap, 0 , 0, width, height, matrix, false);
        if (!bitmap.equals(rotateBitmap)) {
            return rotateBitmap;
        }

        return bitmap;
    }

    public void addMultiImage(final String path, final int rotateDegree) {
        if (isLoadingNewImage) {
            return;
        }
        isLoadingNewImage = true;
        Log.e(TAG, "add image..." + path);
        if (StringUtils.equals(lastPath, path)) {
            isLoadingNewImage = false;
        }
        if (!hasLimitArea) {
            mDisplayHelper.limitCropAreaToCurrent();
            hasLimitArea = true;
        }
        if (addMultiImageDisposable != null) {
            addMultiImageDisposable.dispose();
            addMultiImageDisposable = null;
        }

        saveLastInfo();

        addMultiImageDisposable = Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                return null;
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                animation.setFillAfter(true);
                animation.setFillBefore(true);
                startAnimation(animation);
            }
        }).subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        isLoadingNewImage = false;
                        clearAnimation();
                        mBitmap = bitmap;
                        setBitmapFit(mBitmap, null, null, null, 0, 0);
                        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                        startAnimation(animation);
                    }
                });




        saveLastInfo();

    }

    public void clearImage() {
        setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (this.enableTouchEvent) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    public Observable<Bitmap> getAllSelectedBitmap(ArrayList<LocalMedia> localMedia) {
        saveLastInfo();
        if (cacheMap.isEmpty()) {
            return Observable.empty();
        }
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ImageInfo> mediaMap = (Map.Entry<String, ImageInfo>) iterator.next();
                    ImageInfo imageInfo = mediaMap.getValue();
                    Bitmap bitmap = decodeFile(new File(imageInfo.getPath()));
                    emitter.onNext(DisplayHelper.getCroppedBitmap(imageInfo.getShowRect(), 0, imageInfo.getDrawMatrix(), rotateBitmap(bitmap, imageInfo.getDegree())));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public void getCropperBitmapAsync(CropperCallback callback) {
        callback.onStarted();
        try {
            getCroppedBitmap(callback);
        }catch (Exception ex) {
            ex.printStackTrace();
        } catch (OutOfMemoryError ex) {
            callback.onOutOfMemoryError();
        }

    }

    public boolean isLoadingNewImage() {
        return isLoadingNewImage;
    }


    public void loadNewImage(final String path, final  int rotateDegree) {
        try {
            clearImage();
            currentRotateDegree = 0;
            hasLimitArea = false;
            setIsAdvancedMode(true);
            if (loadNewImageDisposable != null) {
                loadNewImageDisposable.dispose();
                loadNewImageDisposable = null;
            }
            loadNewImageDisposable =  Observable.fromCallable(new Callable<Bitmap>() {
                @Override
                public Bitmap call() throws Exception {
                    Bitmap bitmap = decodeFile(new File(path));
                    if (rotateDegree != 0) {
                        rotateBitmap(bitmap, rotateDegree);
                    }
                    return bitmap;
                }
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                    animation.setFillAfter(true);
                    animation.setFillBefore(true);
                    startAnimation(animation);
                }
            }).subscribe(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    clearAnimation();
                    mBitmap = bitmap;
                    setImageBitmap(bitmap);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                    startAnimation(animation);
                    setFillMode(false);
                    resetCropArea();
                    lastPath = path;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCroppedBitmapAsync(CropperCallback callback){
        callback.onStarted();
        try {
            getCroppedBitmap(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (OutOfMemoryError ex) {
            callback.onOutOfMemoryError();
        }
    }

    public void resetShowImage(final String path) {
        final ImageInfo info = cacheMap.get(path);
        if (info == null) {
            return;
        }

    }

    public void removeImage(String path) {
        cacheMap.remove(path);
        if (path.equals(lastPath))
            lastPath = "";
    }

    public void resetSelectedImage() {
        cacheMap.clear();
        resetMatrix();
    }

    public void rotateImage(int rotateDegree) {
        rotateImageInner(rotateDegree, true);
    }

    public void rotateImageInner(int rotateDegree, boolean isFillMode) {
        if (mBitmap == null || mBitmap.isRecycled()) {
            return;
        }

        if (rotateDegree != 0)
            mBitmap = rotateBitmap(rotateDegree);
        setImageBitmap(mBitmap);
        setFillMode(isFillMode);
    }


    /**
     * 保存最新的图片信息
     */
    public void saveLastInfo() {
        if (!TextUtils.isEmpty(this.lastPath)) {
            ImageInfo info = new ImageInfo(this.lastPath, getShowRect(), getDrawableMatrix(), getSuppMatrix(), getScaleFocusX(), getScaleFocusY(), getBaseMatrix());
            info.setDegree(currentRotateDegree);
            cacheMap.put(this.lastPath, info);
            Log.e(TAG, lastPath + "save info " + getDrawableMatrix());
            currentRotateDegree = 0;
        }
    }




    public void setEnableTouchEvent(boolean enable) {
        enableTouchEvent = enable;
    }

    public void snapImage() {
        if (!isFillMode()) {
            return;
        }
        setFillMode(true);
    }

}

