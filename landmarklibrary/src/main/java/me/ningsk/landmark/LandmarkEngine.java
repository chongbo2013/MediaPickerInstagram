package me.ningsk.landmark;

import android.util.SparseArray;

/**
 * 人脸关键点引擎
 */
public final class LandmarkEngine {

    private static class EngineHolder {
        public static LandmarkEngine instance = new LandmarkEngine();
    }

    private LandmarkEngine() {
        mFaceArrays = new SparseArray<OneFace>();
    }

    public static LandmarkEngine getInstance() {
        return EngineHolder.instance;
    }

    private final Object mSyncFence = new Object();

    // 人脸对象列表
    // 由于人脸数据个数有限，图像中的人脸个数小于千级，而且人脸索引是连续的，用SparseArray比Hashmap性能要更好
    private final SparseArray<OneFace> mFaceArrays;

    /**
     * 设置人脸数
     * @param size
     */
    public void setFaceSize(int size) {
        synchronized (mSyncFence) {
            // 剔除脏数据，有可能在前一次检测的人脸多余当前人脸
            if (mFaceArrays.size() > size) {
                mFaceArrays.removeAtRange(size, mFaceArrays.size() - size);
            }
        }
    }

    /**
     * 是否存在人脸
     * @return
     */
    public boolean hasFace() {
        boolean result;
        synchronized (mSyncFence) {
            result = mFaceArrays.size() > 0;
        }
        return result;
    }

    /**
     * 获取一个人脸关键点数据对象
     * @return
     */
    public OneFace getOneFace(int index) {
        OneFace oneFace = null;
        synchronized (mSyncFence) {
            oneFace = mFaceArrays.get(index);
            if (oneFace == null) {
                oneFace = new OneFace();
            }
        }
        return oneFace;
    }

    /**
     * 插入一个人脸关键点数据对象
     * @param index
     */
    public void putOneFace(int index, OneFace oneFace) {
        synchronized (mSyncFence) {
            mFaceArrays.put(index, oneFace);
        }
    }

    /**
     * 获取人脸列表
     * @return
     */
    public SparseArray<OneFace> getFaceArrays() {
        return mFaceArrays;
    }

    /**
     * 清空所有人脸对象
     */
    public void clearAll() {
        synchronized (mSyncFence) {
            mFaceArrays.clear();
        }
    }

    /**
     * 计算额外顶点，新增8个额外顶点坐标
     * @param vertexPoints
     * @param index
     */
    public void calculateExtraPoints(float[] vertexPoints, int index) {
        if (vertexPoints == null || mFaceArrays.get(index) == null
                || mFaceArrays.get(index).vertexPoints.length + 8 * 2 > vertexPoints.length) {
            return;
        }
        OneFace oneFace = mFaceArrays.get(index);
        // 复制关键点的数据
        System.arraycopy(oneFace.vertexPoints, 0, vertexPoints, 0, oneFace.vertexPoints.length);
        // 新增的人脸关键点
        float[] point = new float[2];
        // 嘴唇中心
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.mouthUpperLipBottom * 2],
                vertexPoints[FaceLandmark.mouthUpperLipBottom * 2 + 1],
                vertexPoints[FaceLandmark.mouthLowerLipTop * 2],
                vertexPoints[FaceLandmark.mouthLowerLipTop * 2 + 1]
        );
        vertexPoints[FaceLandmark.mouthCenter * 2] = point[0];
        vertexPoints[FaceLandmark.mouthCenter * 2 + 1] = point[1];

        // 左眉心
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.leftEyebrowUpperMiddle * 2],
                vertexPoints[FaceLandmark.leftEyebrowUpperMiddle * 2 + 1],
                vertexPoints[FaceLandmark.leftEyebrowLowerMiddle * 2],
                vertexPoints[FaceLandmark.leftEyebrowLowerMiddle * 2 + 1]
        );
        vertexPoints[FaceLandmark.leftEyebrowCenter * 2] = point[0];
        vertexPoints[FaceLandmark.leftEyebrowCenter * 2 + 1] = point[1];

        // 右眉心
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.rightEyebrowUpperMiddle * 2],
                vertexPoints[FaceLandmark.rightEyebrowUpperMiddle * 2 + 1],
                vertexPoints[FaceLandmark.rightEyebrowLowerMiddle * 2],
                vertexPoints[FaceLandmark.rightEyebrowLowerMiddle * 2 + 1]
        );
        vertexPoints[FaceLandmark.rightEyebrowCenter * 2] = point[0];
        vertexPoints[FaceLandmark.rightEyebrowCenter * 2 + 1] = point[1];

        // 额头中心
        vertexPoints[FaceLandmark.headCenter * 2] = vertexPoints[FaceLandmark.eyeCenter * 2] * 2.0f - vertexPoints[FaceLandmark.noseLowerMiddle * 2];
        vertexPoints[FaceLandmark.headCenter * 2 + 1] = vertexPoints[FaceLandmark.eyeCenter * 2 + 1] * 2.0f - vertexPoints[FaceLandmark.noseLowerMiddle * 2 + 1];

        // 额头左侧，备注：这个点不太准确，后续优化
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.leftEyebrowLeftTopCorner * 2],
                vertexPoints[FaceLandmark.leftEyebrowLeftTopCorner * 2 + 1],
                vertexPoints[FaceLandmark.headCenter * 2],
                vertexPoints[FaceLandmark.headCenter * 2 + 1]
        );
        vertexPoints[FaceLandmark.leftHead * 2] = point[0];
        vertexPoints[FaceLandmark.leftHead * 2 + 1] = point[1];

        // 额头右侧，备注：这个点不太准确，后续优化
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.rightEyebrowRightTopCorner * 2],
                vertexPoints[FaceLandmark.rightEyebrowRightTopCorner * 2 + 1],
                vertexPoints[FaceLandmark.headCenter * 2],
                vertexPoints[FaceLandmark.headCenter * 2 + 1]
        );
        vertexPoints[FaceLandmark.rightHead * 2] = point[0];
        vertexPoints[FaceLandmark.rightHead * 2 + 1] = point[1];

        // 左脸颊中心
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.leftCheekEdgeCenter * 2],
                vertexPoints[FaceLandmark.leftCheekEdgeCenter * 2 + 1],
                vertexPoints[FaceLandmark.noseLeft * 2],
                vertexPoints[FaceLandmark.noseLeft * 2 + 1]
        );
        vertexPoints[FaceLandmark.leftCheekCenter * 2] = point[0];
        vertexPoints[FaceLandmark.leftCheekCenter * 2 + 1] = point[1];

        // 右脸颊中心
        FacePointsUtils.getCenter(point,
                vertexPoints[FaceLandmark.rightCheekEdgeCenter * 2],
                vertexPoints[FaceLandmark.rightCheekEdgeCenter * 2 + 1],
                vertexPoints[FaceLandmark.noseRight * 2],
                vertexPoints[FaceLandmark.noseRight * 2 + 1]
        );
        vertexPoints[FaceLandmark.rightCheekCenter * 2] = point[0];
        vertexPoints[FaceLandmark.rightCheekCenter * 2 + 1] = point[1];
    }
}
