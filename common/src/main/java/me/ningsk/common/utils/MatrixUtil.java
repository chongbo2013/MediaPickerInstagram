package me.ningsk.common.utils;


import android.graphics.Matrix;

public class MatrixUtil
{
    public static float[] getTransform(Matrix m)
    {
        float[] transform = new float[9];
        m.getValues(transform);
        return transform;
    }
}
