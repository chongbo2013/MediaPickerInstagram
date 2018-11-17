package me.ningsk.svideo.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import me.ningsk.svideo.R;
import me.ningsk.svideo.base.utils.VideoTrimmerUtil;


/**
 * <p>描述：裁剪区域视频快照适配器<p>
 * 作者：ningsk<br>
 * 日期：2018/11/14 11 15<br>
 * 版本：v1.0<br>
 */
public class VideoTrimmerAdapter extends RecyclerView.Adapter{

    private List<Bitmap> mBitmaps = new ArrayList<>();
    private LayoutInflater mInflater;

    public VideoTrimmerAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrimmerViewHolder(mInflater.inflate(R.layout.item_video_thumb_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TrimmerViewHolder) holder).thumbImageView.setImageBitmap(mBitmaps.get(position));
    }

    @Override
    public int getItemCount() {
        return mBitmaps.size();
    }

    public void addBitmap(Bitmap bitmap) {
        mBitmaps.add(bitmap);
        notifyDataSetChanged();
    }

    private final class TrimmerViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbImageView;
        public TrimmerViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumb);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) thumbImageView.getLayoutParams();
            layoutParams.width = VideoTrimmerUtil.VIDEO_FRAMES_WIDTH / VideoTrimmerUtil.MAX_COUNT_RANGE;
            thumbImageView.setLayoutParams(layoutParams);
        }
    }
}
