package me.ningsk.crop.supply;


import android.graphics.Rect;

import me.ningsk.crop.struct.CropParam;

public interface JRICrop {
    int startCrop();

    int startCropImage(String inputPath, String outPath, Rect rect);

    int startCropAudio(String inputPath, String outputPath, long startTime, long endTime);

    int startCrop(boolean needVideo);

    void setUserHW(boolean hw);

    void setCropParam(CropParam param);

    void setCropCallback(CropCallback callback);

    void cancel();

    void dispose();

    long getVideoDuration(String videoPath) throws Exception;

    String version();

}
