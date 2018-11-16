package me.ningsk.common.quicks;

import android.os.Build;

import java.util.EnumMap;
import java.util.regex.Pattern;

import me.ningsk.common.media.ColorRange;

public class QuirksStorage
{
    private static final EnumMap<Quirk, Object> _Current = new EnumMap(Quirk.class);

    public static boolean getBoolean(Quirk quirk)
    {
        return ((Boolean)get(quirk)).booleanValue();
    }

    public static int getInteger(Quirk quirk) {
        return ((Integer)get(quirk)).intValue();
    }

    public static Object get(Quirk quirk)
    {
        Object val = _Current.get(quirk);
        if (val != null) {
            return val;
        }
        return quirk.getDefaultValue();
    }

    static void add(Quirk quirk, Object val) {
        _Current.put(quirk, val);
    }

    static void addModel(Quirk quirk, Object val, String[] models) {
        for (String model : models)
            if (Build.MODEL.equals(model))
                _Current.put(quirk, val);
    }

    static void addModelSeries(Quirk quirk, Object val, String[] series_list)
    {
        for (String series : series_list)
            if (Pattern.matches(series, Build.MODEL))
                _Current.put(quirk, val);
    }

    static
    {
        addModel(Quirk.FRONT_CAMERA_PREVIEW_DATA_MIRRORED, Boolean.valueOf(true), new String[] { Model.ZTE_U930 });
        addModelSeries(Quirk.FRONT_CAMERA_PICTURE_DATA_ROTATION, Integer.valueOf(180), new String[] { Model.MEIZU_MX2_SERIES});

        addModel(Quirk.CAMERA_RECORDING_HINT, Boolean.valueOf(true), new String[] { Model.XIAOMI_MI_3 });

        addModel(Quirk.CAMERA_NO_AUTO_FOCUS_CALLBACK, Boolean.valueOf(true), new String[] { Model.XIAOMI_NOTE});

        addModel(Quirk.CAMERA_ASPECT_RATIO_DEDUCTION, Boolean.valueOf(true), new String[] { Model.XIAOMI_MI_3 });

        addModelSeries(Quirk.CAMERA_COLOR_RANGE, ColorRange.MPEG, new String[] { Model.SAMSUNG_NOTE_3_SERIES });

        addModel(Quirk.CAMERA_KEEP_PREVIEW_SURFACE, Boolean.valueOf(true), new String[] { Model.XIAOMI_MI_3 });
    }
}
