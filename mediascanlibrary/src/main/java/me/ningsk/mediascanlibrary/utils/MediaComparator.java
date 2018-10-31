package me.ningsk.mediascanlibrary.utils;

import java.util.Comparator;

import me.ningsk.mediascanlibrary.entity.LocalMedia;

public class MediaComparator implements Comparator<LocalMedia> {
    @Override
    public int compare(LocalMedia localMedia, LocalMedia other) {
        Long time, compareTime;
        time = getTime(localMedia);
        compareTime = getTime(other);
        int flag = compareTime.compareTo(time);
        if (flag == 0) {
            // 如果同出一个时间 按照title排序
            return other.getTitle().compareTo(localMedia.getTitle());
        } else {
            return flag;
        }
    }

    private long getTime(LocalMedia localMedia) {
        return localMedia.getDateTaken() != null ? localMedia.getDateTaken() : localMedia.getDateModified();
    }
}