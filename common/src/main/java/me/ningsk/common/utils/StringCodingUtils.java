package me.ningsk.common.utils;

import java.nio.charset.Charset;

public class StringCodingUtils
{
    public static byte[] getBytes(String src, Charset charSet)
    {
        return src.getBytes(charSet);
    }
}
