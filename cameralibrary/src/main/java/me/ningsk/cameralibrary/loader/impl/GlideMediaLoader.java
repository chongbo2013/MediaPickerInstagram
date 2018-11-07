package me.ningsk.cameralibrary.loader.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import me.ningsk.cameralibrary.loader.MediaLoader;

/**
 * <p>描述：图片加载器<p>
 * 作者：ningsk<br>
 * 日期：2018/11/7 09 49<br>
 * 版本：v1.0<br>
 */
public class GlideMediaLoader implements MediaLoader {

    @Override
    public void loadThumbnail(Context context, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int width, int height, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .override(width, height)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .centerCrop())
                .into(imageView);
    }

    @Override
    public void loadGif(Context context, int width, int height, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(new RequestOptions()
                        .override(width, height)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }
}