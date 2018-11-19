package me.ningsk.crop.util;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class common {
    public static final String SD_DIR = Environment.getExternalStorageDirectory().getParent() + "/";
    public static final String JR_NAME = "JRDemo";
    public static final String JR_DIR = SD_DIR + JR_NAME + "/";

    private static void copyFileToSD(Context cxt, String src, String dst) throws IOException
    {
        OutputStream myOutput = new FileOutputStream(dst);
        InputStream myInput = cxt.getAssets().open(src);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
    public static void copySelf(Context cxt, String root) {
        try {
            String[] files = cxt.getAssets().list(root);
            if (files.length > 0) {
                File subdir = new File(SD_DIR + root);
                if (!subdir.exists())
                    subdir.mkdirs();
                for (String fileName : files)
                    copySelf(cxt, root + "/" + fileName);
            }
            else {
                OutputStream myOutput = new FileOutputStream(SD_DIR + root);
                InputStream myInput = cxt.getAssets().open(root);
                byte[] buffer = new byte[1024];
                int length = myInput.read(buffer);
                while (length > 0)
                {
                    myOutput.write(buffer, 0, length);
                    length = myInput.read(buffer);
                }

                myOutput.flush();
                myInput.close();
                myOutput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyAll(Context cxt) {
        File dir = new File(JR_DIR);
        if (!dir.exists())
        {
            copySelf(cxt, JR_NAME);
            dir.mkdirs();
        }
    }

}
