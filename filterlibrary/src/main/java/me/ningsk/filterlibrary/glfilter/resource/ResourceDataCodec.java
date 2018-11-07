package me.ningsk.filterlibrary.glfilter.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

/**
 * <p>描述：数据解码器<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 10 50<br>
 * 版本：v1.0<br>
 */
public class ResourceDataCodec extends ResourceCodec {

    public ResourceDataCodec(String indexPath, String dataPath) {
        super(indexPath, dataPath);
    }

    /**
     * 根据文件名加载bitmap图片
     * @param name
     * @return
     */
    public Bitmap loadBitmap(String name) {
        Pair pair = mIndexMap.get(name);
        if (pair == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(mDataBuffer.array(),
                mDataBuffer.arrayOffset() + (Integer) pair.first, (Integer) pair.second);
    }

    /**
     * 获取文件缓冲
     * @return
     */
    public byte[] getBufferArray() {
        return mDataBuffer.array();
    }

    /**
     * 获取资源描述对象
     * @param path
     * @return
     */
    public Pair<Integer, Integer> getResourcePair(String path) {
        Pair pair = (Pair) mIndexMap.get(path);
        if (pair == null) {
            return null;
        }
        return new Pair<>(((Integer) pair.first) + mDataBuffer.arrayOffset(),
                (Integer) pair.second);
    }
}

