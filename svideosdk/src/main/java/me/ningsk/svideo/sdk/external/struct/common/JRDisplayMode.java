package me.ningsk.svideo.sdk.external.struct.common;


public enum JRDisplayMode {
    DEFAULT;

    private JRDisplayMode() {
        throw new IllegalStateException("u can't instantiate me");
    }

    public static JRDisplayMode getInstanceByValue(int value) {
        switch(value) {
            case 0:
                return DEFAULT;
            default:
                return null;
        }
    }
}
