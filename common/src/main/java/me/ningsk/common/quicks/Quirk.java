package me.ningsk.common.quicks;

import me.ningsk.common.media.ColorRange;

public enum Quirk
{
    FRONT_CAMERA_PREVIEW_DATA_MIRRORED(Boolean.valueOf(false)),
    FRONT_CAMERA_PICTURE_DATA_ROTATION(Integer.valueOf(0)),

    CAMERA_RECORDING_HINT(Boolean.valueOf(false)),
    CAMERA_NO_AUTO_FOCUS_CALLBACK(Boolean.valueOf(false)),

    CAMERA_FOCUS_AREA(Integer.valueOf(100)),

    BROKEN_CAMERA_AF_LOCK(Boolean.valueOf(false)),

    CAMERA_ASPECT_RATIO_DEDUCTION(Boolean.valueOf(false)),

    CAMERA_COLOR_RANGE(ColorRange.JPEG),

    CAMERA_KEEP_PREVIEW_SURFACE(Boolean.valueOf(false));

    private final Class<?> _Type;
    private final Object _DefaultValue;

    private <T> Quirk(T defValue) {
        this._Type = (defValue == null ? null : defValue.getClass());
        this._DefaultValue = defValue;
    }

    public Class<?> getType() {
        return this._Type;
    }

    public Object getDefaultValue() {
        return this._DefaultValue;
    }
}
