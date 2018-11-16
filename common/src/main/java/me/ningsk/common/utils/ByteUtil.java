package me.ningsk.common.utils;


public class ByteUtil
{
    public static void putShort(byte[] b, short s, int index)
    {
        b[(index + 1)] = ((byte)(s >> 8));
        b[index] = ((byte)s);
    }

    public static short getShort(byte[] b, int index)
    {
        return (short)(b[(index + 1)] << 8 | b[index] & 0xFF);
    }

    public static byte[] short2byte(short[] array) {
        byte[] ba = new byte[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            putShort(ba, array[i], i * 2);
        }
        return ba;
    }

    public static short[] byte2short(byte[] array) {
        short[] sa = new short[array.length / 2];
        for (int i = 0; i < sa.length; i++) {
            sa[i] = getShort(array, i * 2);
        }

        return sa;
    }

    public static int byteArrayToInt(byte[] b)
    {
        int start = 0;
        int low = b[start] & 0xFF;
        int high = b[(start + 1)] & 0xFF;
        return high << 8 | low;
    }

    public static short byteArrayToShort(byte[] b)
    {
        int start = 0;
        short low = (short)(b[start] & 0xFF);
        short high = (short)(b[(start + 1)] & 0xFF);
        return (short)(high << 8 | low);
    }

    public static long byteArrayToLong(byte[] b)
    {
        int start = 0;
        int i = 0;
        int len = 4;
        int cnt = 0;
        byte[] tmp = new byte[len];
        for (i = start; i < start + len; i++) {
            tmp[cnt] = b[i];
            cnt++;
        }
        long accum = 0L;
        i = 0;
        for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
            accum |= (tmp[i] & 0xFF) << shiftBy;
            i++;
        }
        return accum;
    }

    public static String byteArrayToString(byte[] bytes) {
        StringBuilder sBuilder = new StringBuilder(bytes.length);
        for (byte b : bytes) {
            sBuilder.append((char)b);
        }
        return sBuilder.toString();
    }

    public static byte[] shortToByteArray(short sample)
    {
        return new byte[] { (byte)(sample & 0xFF), (byte)(sample >>> 8 & 0xFF) };
    }

    public static byte[] floatToByteArray(Float sample) {
        return intToByteArray(Float.floatToIntBits(sample.floatValue()));
    }

    public static byte[] intToByteArray(int bits) {
        byte[] bytes = new byte[4];
        bytes[0] = ((byte)(bits & 0xFF));
        bytes[1] = ((byte)(bits >> 8 & 0xFF));
        bytes[2] = ((byte)(bits >> 16 & 0xFF));
        bytes[3] = ((byte)(bits >> 24 & 0xFF));
        return bytes;
    }

    public static byte[] doubleToByteArray(Double sample) {
        return longToByteArray(Double.doubleToLongBits(sample.doubleValue()));
    }

    public static byte[] longToByteArray(long sample) {
        byte[] bytes = new byte[8];
        bytes[0] = ((byte)(int)(sample & 0xFF));
        bytes[1] = ((byte)(int)(sample >> 8 & 0xFF));
        bytes[2] = ((byte)(int)(sample >> 16 & 0xFF));
        bytes[3] = ((byte)(int)(sample >> 24 & 0xFF));
        bytes[4] = ((byte)(int)(sample >> 32 & 0xFF));
        bytes[5] = ((byte)(int)(sample >> 40 & 0xFF));
        bytes[6] = ((byte)(int)(sample >> 48 & 0xFF));
        bytes[7] = ((byte)(int)(sample >> 56 & 0xFF));
        return bytes;
    }
}
