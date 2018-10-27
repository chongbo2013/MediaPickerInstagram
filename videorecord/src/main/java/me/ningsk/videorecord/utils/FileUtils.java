package me.ningsk.videorecord.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {


    public static String getStorageMp4(String s){
        File file;
        String parent = Environment.getExternalStorageDirectory().getAbsolutePath() + "/aserbao";
        File file1 = new File(parent);
        if (!file1.exists()) {
            file1.mkdir();
        }
        file = new File(parent, s + ".mp4");

        return file.getPath();
    }
}
