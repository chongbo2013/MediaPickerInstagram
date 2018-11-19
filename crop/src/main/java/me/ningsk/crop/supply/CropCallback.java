package me.ningsk.crop.supply;


public interface CropCallback {
    void onProgress(int progress);

    void onError(int errorCode);

    void onComplete(long durations);

    void onCancelComplete();
}
