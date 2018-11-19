package me.ningsk.crop;


import android.content.Context;
import android.graphics.Rect;

import me.ningsk.crop.struct.CropParam;
import me.ningsk.crop.supply.CropCallback;
import me.ningsk.crop.supply.JRICrop;

public class JrCrop implements JRICrop {

    public JrCrop(Context context) {

    }

    @Override
    public int startCrop() {
        return 0;
    }

    @Override
    public int startCropImage(String inputPath, String outPath, Rect rect) {
        return 0;
    }

    @Override
    public int startCropAudio(String inputPath, String outputPath, long startTime, long endTime) {
        return 0;
    }

    @Override
    public int startCrop(boolean needVideo) {
        return 0;
    }

    @Override
    public void setUserHW(boolean hw) {

    }

    @Override
    public void setCropParam(CropParam param) {

    }

    @Override
    public void setCropCallback(CropCallback callback) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public long getVideoDuration(String videoPath) throws Exception {
        return 0;
    }

    @Override
    public String version() {
        return null;
    }
}
