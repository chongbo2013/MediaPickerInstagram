package me.ningsk.photoselector.utils;

import java.util.Comparator;

import me.ningsk.photoselector.bean.MediaBean;

public class MediaComparator implements Comparator<MediaBean> {
    @Override
    public int compare(MediaBean mediaBean, MediaBean other) {
        Long time, compareTime;
        time = getTime(mediaBean);
        compareTime = getTime(other);
        int flag = compareTime.compareTo(time);
        if (flag == 0) {
            // 如果同出一个时间 按照title排序
            return other.getTitle().compareTo(mediaBean.getTitle());
        } else {
            return flag;
        }
    }

    private long getTime(MediaBean mediaBean) {
        return mediaBean.getDateTaken() != null ? mediaBean.getDateTaken() : mediaBean.getDateModified();
    }
}