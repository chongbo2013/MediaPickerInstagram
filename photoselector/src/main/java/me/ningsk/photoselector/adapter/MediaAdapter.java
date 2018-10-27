package me.ningsk.photoselector.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ningsk.photoselector.MimeType;
import me.ningsk.photoselector.R;
import me.ningsk.photoselector.SelectionOptions;
import me.ningsk.photoselector.anim.OptAnimationLoader;
import me.ningsk.photoselector.bean.MediaBean;
import me.ningsk.photoselector.utils.DateUtils;
import me.ningsk.photoselector.utils.StringUtils;
import me.ningsk.photoselector.utils.ToastManage;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder>{

    private final static int DURATION = 450;
    private RecyclerView mRecyclerView;

    private final Context mContext;
    private ArrayList<MediaBean> mMedias = new ArrayList<>();
    private SelectionOptions mOptions;
    private int mMaxSelectable;
    private ArrayList<MediaBean> selectMedias = new ArrayList<>();

    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private Animation animation;
    private boolean zoomAnim;
    private boolean isGo = false;
    public MediaAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        clearRvAnim(mRecyclerView);
        mOptions = SelectionOptions.getOptions();
        mMaxSelectable = mOptions.maxSelectable;
        animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
        zoomAnim = true;
    }

    public void bindSelectImages(ArrayList<MediaBean> medias) {
        this.mMedias = medias;
        notifyDataSetChanged();
    }

    public void bindSelectImages(List<MediaBean> medias) {
        // 这里重新构造一个新集合，不然会产生已选集合一变，结果集合也会添加的问题
        ArrayList<MediaBean> selection = new ArrayList<>();
        for (MediaBean media: medias) {
            selection.add(media);
        }
        this.selectMedias = selection;
        subSelectPosition();
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectMedias);
        }

    }







    public void clearRvAnim(RecyclerView rv) {
        if (rv != null)
            return;
        RecyclerView.ItemAnimator animator = rv.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rv.getItemAnimator().setChangeDuration(333);
        rv.getItemAnimator().setMoveDuration(333);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_media_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MediaBean media = mMedias.get(position);
        media.position = holder.getAdapterPosition();
        final String path = media.getPath();
        final int mediaMimeType = media.getMediaMimeType();
        notifyCheckChanged(holder, media);
        selectMedia(holder, isSelected(media), false);
        if (mediaMimeType == MimeType.VIDEO) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.video_icon);
            StringUtils.modifyTextViewDrawable(holder.tvDuration, drawable, 0);
            holder.tvDuration.setVisibility(View.VISIBLE);
        } else if (mediaMimeType == MimeType.PHOTO) {
            if (mOptions.showGif && mOptions.showGifFlag && "image/gif".equals(media.getMimeType())) {
                holder.tvIsGif.setVisibility(View.VISIBLE);
            } else {
                holder.tvIsGif.setVisibility(View.GONE);
            }
        }
        long duration =media.getDuration();
        holder.tvDuration.setText(DateUtils.timeParse(duration));
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(160, 160);
        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(path)
                .apply(options)
                .into(holder.ivPicture);
         holder.contentView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!new File(path).exists()) {
                     ToastManage.s(mContext, MimeType.s(mContext, mediaMimeType));
                     return;
                 }
                 changeCheckBoxState(holder, position, media);
             }
         });
    }

    @Override
    public int getItemCount() {
        return mMedias.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture;
        TextView check;
        TextView tvDuration, tvIsGif;
        View contentView;
        LinearLayout layoutCheck;
        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            ivPicture = itemView.findViewById(R.id.iv_picture);
            check = itemView.findViewById(R.id.check);
            layoutCheck =  itemView.findViewById(R.id.ll_check);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvIsGif = (TextView) itemView.findViewById(R.id.tv_isGif);
        }
    }

    public void setOnPhotoSelectChangeListener(OnPhotoSelectChangedListener listener) {
        this.imageSelectChangedListener = listener;
    };

    public boolean isSelected(MediaBean mediaBean) {
        for (MediaBean media: selectMedias) {
            if (media.getPath().equals(mediaBean.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 选择按钮更新
     * @param viewHolder
     * @param mediaBean
     */
    private void notifyCheckChanged(ViewHolder viewHolder, MediaBean mediaBean) {
        viewHolder.check.setText("");
        for (MediaBean media: selectMedias) {
            mediaBean.setNum(media.getNum());
            media.setPosition(mediaBean.position);
            viewHolder.check.setText(String.valueOf(mediaBean.getNum()));
        }
    }


    /**
     * 改变图片选中状态
     * @param contentHolder
     * @param position
     * @param item
     */
    private void changeCheckBoxState(ViewHolder contentHolder, int position, MediaBean item) {
        if (item == null) return;
        boolean isChecked = contentHolder.check.isSelected();
        int mediaType = selectMedias.size() > 0 ? selectMedias.get(0).getMediaMimeType() : MimeType.OTHER;
        if (mediaType == item.getMediaMimeType() && mediaType != MimeType.VIDEO) {
            // 暂时只支持多张图片
            ToastManage.s(mContext, mContext.getString(R.string.photo_ruler));
            return;
        }
        if (selectMedias.size() >= mMaxSelectable && isChecked) {
            ToastManage.s(mContext, mContext.getString(R.string.photo_message_video_max_num, mMaxSelectable));
            return;
        }
        if (isChecked) {
            for (MediaBean media : selectMedias) {
                if (media.getPath().equals(item.getPath())) {
                    selectMedias.remove(media);
                    subSelectPosition();
                    disZoom(contentHolder.ivPicture);
                    break;
                }
            }
        } else {
            // 如果是单选，则晴空已选中的并刷新列表（作单一选择）
            if (mMaxSelectable == 1) {
                singleRadioMediaImage();
            }
            selectMedias.add(item);
            item.setNum(selectMedias.size());
            zoom(contentHolder.ivPicture);
        }
        // 通知点击项发生了改变
        notifyItemChanged(contentHolder.getAdapterPosition());
        selectMedia(contentHolder, !isChecked, true);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectMedias);
        }
    }

    public interface OnPhotoSelectChangedListener {
        /**
         * 已选Media回调
         * @param selectMedias
         */
        void onChange(List<MediaBean> selectMedias);
    }

    private void singleRadioMediaImage() {
        if (selectMedias != null
                && selectMedias.size() > 0) {
            isGo = true;
            MediaBean media = selectMedias.get(0);
            notifyItemChanged(isGo? media.position : media.position > 0 ? media.position - 1 : 0);
            selectMedias.clear();
        }
    }


    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        int size = selectMedias.size();
        for (int index = 0, length = size; index < length; index++) {
            MediaBean media = selectMedias.get(index);
            media.setNum(index + 1);
            notifyItemChanged(media.position);
        }
    }

    /**
     * 选中的图片并执行动画
     * @param holder
     * @param isChecked
     * @param isAnim
     */
    public void selectMedia(ViewHolder holder, boolean isChecked, boolean isAnim) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            if (isAnim) {
                if (animation != null) {
                    holder.check.startAnimation(animation);
                }
            }
            holder.ivPicture.setColorFilter(ContextCompat.getColor
                    (mContext, R.color.image_overlay_true), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.ivPicture.setColorFilter(ContextCompat.getColor
                    (mContext, R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);
        }
    }



    private void zoom(ImageView ivImg) {
        if (zoomAnim) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(ivImg, "scaleX", 1f, 1.12f),
                    ObjectAnimator.ofFloat(ivImg, "scaleY", 1f, 1.12f)
            );
            set.setDuration(DURATION);
            set.start();
        }
    }

    private void disZoom(ImageView ivImg) {
        if (zoomAnim) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(ivImg, "scaleX", 1.12f, 1f),
                    ObjectAnimator.ofFloat(ivImg, "scaleY", 1.12f, 1f)
            );
            set.setDuration(DURATION);
            set.start();
        }
    }

}
