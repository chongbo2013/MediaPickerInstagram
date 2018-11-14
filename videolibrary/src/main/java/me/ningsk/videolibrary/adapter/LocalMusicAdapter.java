package me.ningsk.videolibrary.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.ningsk.utilslibrary.utils.StringUtils;
import me.ningsk.videolibrary.R;
import me.ningsk.videolibrary.bean.Music;

/**
 * <p>描述：本地音乐适配器<p>
 * 作者：ningsk<br>
 * 日期：2018/10/31 09 25<br>
 * 版本：v1.0<br>
 */
public class LocalMusicAdapter extends LocalCursorAdapter<RecyclerView.ViewHolder> {

    private OnMusicItemSelectedListener mListener;

    public LocalMusicAdapter(Cursor cursor) {
        super(cursor);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_music_select_view, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        MusicViewHolder viewHolder = (MusicViewHolder) holder;
        final Music music = Music.valueof(cursor);
        viewHolder.mTextName.setText(music.getName());
        viewHolder.mTexDuration.setText(StringUtils.generateMillisTime((int) music.getDuration()));
        viewHolder.mLayoutMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMusicItemSelected(music);
                }
            }
        });
    }

    /**
     * 设置选中音乐监听器
     * @param listener
     */
    public void setOnMusicSelectedListener(OnMusicItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * 音乐选中监听器
     */
    public interface OnMusicItemSelectedListener {
        // 选中音乐
        void onMusicItemSelected(Music music);
    }


    class MusicViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLayoutMusic;
        private TextView mTextName;
        private TextView mTexDuration;

        public MusicViewHolder(View itemView) {
            super(itemView);
            mLayoutMusic = (LinearLayout) itemView.findViewById(R.id.layout_item_music);
            mTextName = (TextView) itemView.findViewById(R.id.tv_music_name);
            mTexDuration = (TextView) itemView.findViewById(R.id.tv_music_duration);
        }
    }

}

