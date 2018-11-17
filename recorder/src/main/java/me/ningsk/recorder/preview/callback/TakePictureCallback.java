package me.ningsk.recorder.preview.callback;


public interface TakePictureCallback {
    void onPictureTaken(byte[] data);

    void onShutter();
}
