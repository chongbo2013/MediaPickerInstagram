package me.ningsk.videotracklibrary.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.ningsk.utilslibrary.utils.JRMediaPlayer;
import me.ningsk.videotracklibrary.adapter.VideoTrimmerAdapter;
import me.ningsk.videotracklibrary.interfaces.IVideoTrimmerView;
import me.ningsk.videotracklibrary.interfaces.VideoTrimListener;

import static me.ningsk.videotracklibrary.utils.VideoTrimmerUtil.VIDEO_FRAMES_WIDTH;

/**
 * <p>描述：裁剪区域视图<p>
 * 作者：ningsk<br>
 * 日期：2018/11/14 10 59<br>
 * 版本：v1.0<br>
 */
public class VideoTrimmerView extends FrameLayout implements IVideoTrimmerView{

    private static final String TAG = VideoTrimmerView.class.getSimpleName();

    private int mMaxWidth = VIDEO_FRAMES_WIDTH;
        private Context mContext;
        private RelativeLayout mLinearVideo;
        private JRMediaPlayer  mJRMediaPlayer;
        private ImageView mPlayerView;
        private RecyclerView mVideoThumbRecyclerView;
        private RangeSeekBarView mRangeSeekBarView;
        private LinearLayout mSeekBarLayout;
        private ImageView mRedProgressIcon;
        private TextView mVideoShootTipTv;
        private float mAverageMsPx; // 每毫秒所占的px
        private float mAveragePxMs; // 每px所占的ms毫秒
        private Uri mSourceUri;
        private String mFinalPath;
        private VideoTrimListener mOnTrimVideoListener;
        private int mDuration = 0;
        private VideoTrimmerAdapter mVideoTrimmerAdapter;
        private boolean isFromRestore = false;
        // new
        private  long mLeftProgressPos, mRightProgressPos;



    public VideoTrimmerView(@NonNull Context context) {
        super(context);
    }

    public VideoTrimmerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTrimmerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDestroy() {

    }
}
