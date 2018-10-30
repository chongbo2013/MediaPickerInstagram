package me.ningsk.cameralibrary.engine.model;

/**
 * <p>描述：相机信息<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 16 09<br>
 * 版本：v1.0<br>
 */
public class CameraInfo {
    int facing;
    int orientation;

    public CameraInfo(int facing, int orientation) {
        this.facing = facing;
        this.orientation = orientation;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}

