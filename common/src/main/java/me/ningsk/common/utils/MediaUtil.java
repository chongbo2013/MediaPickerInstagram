package me.ningsk.common.utils;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import me.ningsk.common.global.JRTag;

public class MediaUtil {
    public static int getFrameRate(String output) {

        MediaExtractor mediaExtractor = new MediaExtractor();
        int frameRate = 0;
        try {
            File file = new File(output);
            FileInputStream fi = new FileInputStream(file);
            FileDescriptor fd = fi.getFD();

            mediaExtractor.setDataSource(fd);
            int numTracks = mediaExtractor.getTrackCount();
            for (int i = 0; i < numTracks; i++) {
                MediaFormat format = mediaExtractor.getTrackFormat(i);
                String mime = format.getString("mime");
                if ((mime.startsWith("video/")) &&
                        (format.containsKey("frame-rate")))
                    frameRate = format.getInteger("frame-rate");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(JRTag.TAG, "get frame rate error", e);
        } finally {
            mediaExtractor.release();
        }
        return frameRate;

    }
}
