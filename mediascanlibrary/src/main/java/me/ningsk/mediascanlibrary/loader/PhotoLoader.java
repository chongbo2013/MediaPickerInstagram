package me.ningsk.mediascanlibrary.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.entity.LocalMedia;


public class PhotoLoader {
    @NonNull
    public static ArrayList<LocalMedia> getAllPhotos(@NonNull Context context, boolean showGif) {
        StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE);
        String[] selectionValues;
        if (showGif) {
            selection.append("=? or ").append(MediaStore.Images.Media.MIME_TYPE).append("=?");
            selectionValues = new String[]{
                    "image/jpeg", "image/png", "image/jpg", "image/gif",
                    "image/x-ms-bmp", "image/webp"};
        } else {
            selection.append("=?");
            selectionValues = new String[]{
                    "image/jpeg", "image/png", "image/jpg",
                    "image/x-ms-bmp", "image/webp"};
        }
        Cursor cursor = makePhotoCursor(context, selection.toString(), selectionValues, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        return getPhotos(cursor);
    }

    @NonNull
    private static ArrayList<LocalMedia> getPhotos(@Nullable final Cursor cursor) {
        ArrayList<LocalMedia> songs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getPhotoFromCursorImpl(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null)
            cursor.close();
        return songs;
    }

    public static LocalMedia getPhotoFromCursorImpl(Cursor cursor) {
        LocalMedia photoBean = new LocalMedia();
        photoBean.setId(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
        photoBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE)));
        photoBean.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
        photoBean.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)));
        photoBean.setMediaMimeType(MimeType.PHOTO);
        photoBean.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)));
        photoBean.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)));
        photoBean.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)));
        photoBean.setDateTaken(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)));
        photoBean.setDateAdded(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)) * 1000);
        photoBean.setDateModified(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED)) * 1000);
        return photoBean;
    }

    @Nullable
    private static Cursor makePhotoCursor(@NonNull final Context context, @Nullable final String selection, final String[] selectionValues, final String sortOrder) {
        try {
            return context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            BaseColumns._ID,
                            MediaStore.Images.ImageColumns.TITLE,
                            MediaStore.Images.ImageColumns.DATA,
                            MediaStore.Images.ImageColumns.DATE_TAKEN,
                            MediaStore.Images.ImageColumns.DATE_ADDED,
                            MediaStore.Images.ImageColumns.DATE_MODIFIED,
                            MediaStore.Images.ImageColumns.SIZE,
                            MediaStore.Images.ImageColumns.MIME_TYPE,
                            MediaStore.Images.ImageColumns.WIDTH,
                            MediaStore.Images.ImageColumns.HEIGHT
                    }, selection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}
