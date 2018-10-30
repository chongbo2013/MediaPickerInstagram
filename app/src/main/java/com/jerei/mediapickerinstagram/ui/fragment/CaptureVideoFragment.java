package com.jerei.mediapickerinstagram.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jerei.mediapickerinstagram.R;

import java.io.File;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.ningsk.videorecord.camera.Camera;
import me.ningsk.videorecord.camera.Size;
import me.ningsk.videorecord.multirecorder.MultiPartRecorder;
import me.ningsk.videorecord.multirecorder.widget.MultiPartRecorderView;
import me.ningsk.videorecord.recorder.CameraController;
import me.ningsk.videorecord.recorder.DefaultCameraPreview;
import me.ningsk.videorecord.recorder.ICameraPreview;
import me.ningsk.videorecord.recorder.VideoRecorder;
import me.ningsk.videorecord.recorder.VideoRecorderHandler;
import me.ningsk.videorecord.util.LogUtil;
import me.ningsk.videorecord.util.RecordGestureDetector;

public class CaptureVideoFragment  extends Fragment {

    @BindView(R.id.mCameraPhotoView)
    TextureView mTextureView;

    @BindView(R.id.mBtnTakeVideo)
    View mBtnRecord;
    @BindView(R.id.recorderIndicator)
    MultiPartRecorderView mRecorderIndicator;
    @BindView(R.id.mShutter)
    View mShutter;
    @BindView(R.id.mFlashPhoto)
    ImageView mFlashPhoto;
    private static final String[] FLASH_OPTIONS = {
            android.hardware.Camera.Parameters.FLASH_MODE_AUTO,
            android.hardware.Camera.Parameters.FLASH_MODE_OFF,
            android.hardware.Camera.Parameters.FLASH_MODE_ON
    };
    private CameraController mCameraController;
    private static final int MAX_DURATION = 30;
    private static final int MIN_DURATION = 3;
    private MultiPartRecorder mRecorder;
    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private int mCurrentFlash;

    private boolean faceBack = true;

    @OnClick(R.id.mSwitchCamera)
    void onSwitchCamera() {
        if (mTextureView != null) {
            faceBack = !faceBack;
            if (faceBack) {
                mCameraController.setFacing(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                mCameraController.setFacing(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
        }
    }


    @OnClick(R.id.mFlashPhoto)
    void onChangeFlashState() {
        if (mTextureView != null) {
            mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
            mFlashPhoto.setImageResource(FLASH_ICONS[mCurrentFlash]);
            mCameraController.setFlashMode(FLASH_OPTIONS[mCurrentFlash]);
        }
    }










    public static CaptureVideoFragment newInstance() {
        return new CaptureVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.capture_video_view, container, false);
        ButterKnife.bind(this, v);

        return v;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecorderIndicator.setMinDuration(MIN_DURATION);
        mRecorderIndicator.setMaxDuration(MAX_DURATION);
        mRecorderIndicator.setRecordListener(new MultiPartRecorderView.RecordListener() {

            /**
             * 超过最小录制时间
             */
            @Override
            public void onOvertakeMinTime() {

            }

            /**
             * 超过最大录制时间
             * @param parts 视频块
             */
            @Override
            public void onOvertakeMaxTime(ArrayList<MultiPartRecorderView.Part> parts) {

            }

            /**
             * 视频时长改变
             * @param duration 时长
             */
            @Override
            public void onDurationChange(long duration) {

            }
        });

        mBtnRecord.setOnTouchListener(new View.OnTouchListener() {
            private final RecordGestureDetector mGestureDetector = new RecordGestureDetector(new RecordGestureDetector.SimpleOnGestureListener(){
                private static final String TAG = "TouchGestureDetector";

                @Override
                public void onLongPressDown(View view, MotionEvent e) {
                    LogUtil.logd(TAG, "onLongPressDown");

                }

                @Override
                public void onLongPressUp(View view, MotionEvent e) {
                    stopRecord();
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(v, event);
            }
        });

        mTextureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCameraController.setFocusAreaOnTouch(event);
                mCameraController.setMeteringAreaOnTouch(event);
                mCameraController.setZoomOnTouch(event);
                return true;
            }
        });
    }

    private void stopRecord() {
        if (mRecorder != null && mRecorder.isRecordEnable()) {
            mRecorder.setRecordEnabled(false);
            mRecorderIndicator.stopRecord();
        }
    }

    private void startRecord() {
        if (mRecorder != null && !mRecorder.isRecordEnable()) {
            mRecorder.setRecordEnabled(true);
            mRecorderIndicator.startRecord(mRecorder.getCurrentPartFile());
        }
    }

    private void initRecorder() {
        ICameraPreview cameraPreview = new DefaultCameraPreview(mTextureView);
        Camera.CameraBuilder cameraBuilder = new Camera.CameraBuilder(getActivity())
                .useDefaultConfig()
                .setFacing(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setPictureSize(new Size(2048, 1536))
                .setRecordingHint(true)
                .setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        VideoRecorder.Builder builder = new VideoRecorder.Builder(cameraPreview)
                .setCallbackHandler(new CallbackHandler())
                .setCameraBuilder(cameraBuilder)
                .setOutPutPath(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), File.separator + "VideoRecorder").getAbsolutePath())
                .setFrameRate(30)
                .setChannelCount(1);
        // 分段录制，回删支持
        MultiPartRecorder.Builder multiBuilder = new MultiPartRecorder.Builder(builder);
        mRecorder = multiBuilder
                .addPartListener(new MultiPartRecorder.VideoPartListener() {
                    @Override
                    public void onRecordVideoPartStarted(MultiPartRecorder.Part part) {
                        LogUtil.logd("onRecordVideoPartStarted\t" + part.toString());
                    }

                    @Override
                    public void onRecordVideoPartSuccess(MultiPartRecorder.Part part) {
                        LogUtil.logd("onRecordVideoPartSuccess\t" + part.toString());
                    }

                    @Override
                    public void onRecordVideoPartFailure(MultiPartRecorder.Part part) {
                        LogUtil.loge("onRecordVideoPartFailure\t" + part.file);
                        mRecorderIndicator.removePart(part.file.getAbsolutePath());
                        mRecorder.removePart(part.file.getAbsolutePath());
                    }
                })
                .setMergeListener(new MultiPartRecorder.VideoMergeListener() {
                    @Override
                    public void onStart() {
                        LogUtil.logd("merge onStart");
                    }

                    @Override
                    public void onSuccess(File outFile) {
                        LogUtil.logd("merge Success \t" + outFile);
                        //TODO: OTHER
                    }

                    @Override
                    public void onError(Exception e) {
                        LogUtil.logd("merge Error \t" + e.toString());
                    }

                    /**
                     * 合并进度
                     * @param value 0 - 1
                     */
                    @Override
                    public void onProgress(float value) {
                        LogUtil.logd("merge onProgress \t" + value);
                    }
                })
                .setFileFilter(new MultiPartRecorder.FileFilter() {
                    @Override
                    public boolean filter(MultiPartRecorder.Part part) {
                        return part.duration > 1500;
                    }
                })
                .build();
        mCameraController = mRecorder.getCameraController();
    }





    // TODO: 2018/6/26 暂时让他泄露吧~
    @SuppressLint("HandlerLeak")
    private class CallbackHandler extends VideoRecorderHandler {

        private Toast mToast;

        @Override
        protected void handleUpdateFPS(float fps) {

        }

        @SuppressLint("ShowToast")
        @Override
        protected void handleVideoMuxerStopped(String output) {
            if (mToast != null) {
                mToast.setText(output);
            } else {
                mToast = Toast.makeText(getContext(), output, Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

    private void stopPreview() {
        mRecorder.stopPreview();
    }

    private void startPreview() {
        if (mRecorder == null ) initRecorder();
        mRecorder.startPreview();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTextureView.getSurfaceTexture() != null) {
            startPreview();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }


}
