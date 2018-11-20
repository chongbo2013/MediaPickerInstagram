package me.ningsk.mediascanlibrary.entity;

public enum VideoDisplayMode {
    SCALE(0),
    FILL(1);

    private int displayMode;

    private VideoDisplayMode(int mode) {
        this.displayMode = mode;
    }

    public int getDisplayMode() {
        return this.displayMode;
    }

    public static VideoDisplayMode valueOf(int mode) {
        return values()[mode];
    }
}
