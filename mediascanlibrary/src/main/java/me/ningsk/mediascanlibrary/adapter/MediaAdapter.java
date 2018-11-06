package me.ningsk.mediascanlibrary.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.anim.OptAnimationLoader;
import me.ningsk.mediascanlibrary.config.MimeType;
import me.ningsk.mediascanlibrary.config.PhotoSelectorConfig;
import me.ningsk.mediascanlibrary.config.SelectionOptions;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.utils.DateUtils;
import me.ningsk.mediascanlibrary.utils.StringUtils;
import me.ningsk.mediascanlibrary.utils.ToastManage;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int DURATION = 450;
    private Context context;
    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<LocalMedia>();
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();
    private int selectMode;
    private int overrideWidth, overrideHeight;
    private float sizeMultiplier;
    private Animation animation;
    private SelectionOptions options;
    private boolean zoomAnim;
    /**
     * 单选图片
     */
    private boolean isGo;

    public MediaAdapter(Context context, SelectionOptions options) {
        this.context = context;
        this.options = options;
        this.selectMode = options.selectionMode;
        this.maxSelectNum = options.maxSelectNum;
        this.overrideWidth = options.overrideWidth;
        this.overrideHeight = options.overrideHeight;
        this.sizeMultiplier = options.sizeMultiplier;
        this.zoomAnim = options.zoomAnim;
        animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
    }


    public void bindImagesData(List<LocalMedia> images) {
        this.images = images;
        notifyDataSetChanged();
    }


    public List<LocalMedia> getSelectedImages() {
        if (selectImages == null) {
            selectImages = new ArrayList<>();
        }
        return selectImages;
    }

    public List<LocalMedia> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }

    @Override
    public int getItemViewType(int position) {
            return PhotoSelectorConfig.TYPE_PICTURE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.photo_item_media_content, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            final ViewHolder contentHolder = (ViewHolder) holder;
            final LocalMedia image = images.get(position);
            image.position = contentHolder.getAdapterPosition();
            final String path = image.getPath();
            final String pictureType = image.getMimeType();
            notifyCheckChanged(contentHolder, image);
            selectImage(contentHolder, isSelected(image), false);

            final int mediaMimeType = image.getMediaMimeType();
            boolean gif = MimeType.isGif(pictureType);

            contentHolder.tvIsGif.setVisibility(gif ? View.VISIBLE : View.GONE);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.video_icon);
            StringUtils.modifyTextViewDrawable(contentHolder.tvDuration, drawable, 0);
            contentHolder.tvDuration.setVisibility(mediaMimeType == MimeType.VIDEO
                    ? View.VISIBLE : View.GONE);


            long duration = image.getDuration();
            contentHolder.tvDuration.setText(DateUtils.timeParse(duration));

            RequestOptions options = new RequestOptions();
            if (overrideWidth <= 0 && overrideHeight <= 0) {
                options.sizeMultiplier(sizeMultiplier); }
                else {
                options.override(overrideWidth, overrideHeight);
            }
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            options.centerCrop();
            options.placeholder(R.drawable.image_placeholder);
            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(contentHolder.ivPhoto);

            contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如原图路径不存在或者路径存在但文件不存在
                    if (!new File(path).exists()) {
                        ToastManage.s(context, MimeType.s(context, mediaMimeType));
                        return;
                    }
                    int index =  position;
                    imageSelectChangedListener.onPictureClick(image, index);
                    // 如果是单选，并且已经被选中，禁止取消
                    if (selectMode == PhotoSelectorConfig.SINGLE && contentHolder.check.isSelected()) {
                        return;
                    }
                    changeCheckboxState(contentHolder, image);

                }
            });
        if (selectMode == PhotoSelectorConfig.SINGLE) {
            ((ViewHolder) holder).check.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return  images.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView check;
        TextView tvDuration, tvIsGif;
        View contentView;
        LinearLayout ll_check;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            ivPhoto = itemView.findViewById(R.id.iv_picture);
            check = itemView.findViewById(R.id.check);
            ll_check = itemView.findViewById(R.id.ll_check);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvIsGif = itemView.findViewById(R.id.tv_isGif);
        }
    }

    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 选择按钮更新
     */
    private void notifyCheckChanged(ViewHolder viewHolder, LocalMedia imageBean) {
        viewHolder.check.setText("");
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(imageBean.getPath())) {
                imageBean.setNum(media.getNum());
                media.setPosition(imageBean.getPosition());
                viewHolder.check.setText(String.valueOf(imageBean.getNum()));
            }
        }
    }

    /**
     * 改变图片选中状态
     *
     * @param contentHolder
     * @param image
     */

    private void changeCheckboxState(ViewHolder contentHolder, LocalMedia image) {
        boolean isChecked = contentHolder.check.isSelected();
        int mediaMimeType = selectImages.size() > 0 ? selectImages.get(0).getMediaMimeType() : MimeType.OTHER;
        if (mediaMimeType != MimeType.OTHER) {
            boolean toEqual = (image.getMediaMimeType() == mediaMimeType);
            if (!toEqual && selectMode == PhotoSelectorConfig.MULTIPLE) {
                ToastManage.s(context, context.getString(R.string.photo_ruler));
                return;
            }
        }
        if (selectImages.size() >= maxSelectNum && !isChecked) {
            boolean eqImg = (image.getMediaMimeType() == mediaMimeType);
            String str = eqImg ? context.getString(R.string.photo_message_max_num, maxSelectNum)
                    : context.getString(R.string.photo_message_min_num, maxSelectNum);
            ToastManage.s(context, str);
            return;
        }

        if (isChecked) {
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    imageSelectChangedListener.onRemove(media);
                    subSelectPosition();
                    disZoom(contentHolder.ivPhoto);
                    break;
                }
            }
        } else {
            // 如果是单选，则清空已选中的并刷新列表(作单一选择)
            if (selectMode == PhotoSelectorConfig.SINGLE) {
                singleRadioMediaImage();
            }
            selectImages.add(image);
            image.setNum(selectImages.size());
            zoom(contentHolder.ivPhoto);
        }
        //通知点击项发生了改变
        notifyItemChanged(contentHolder.getAdapterPosition());
        selectImage(contentHolder, !isChecked, true);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    /**
     * 单选模式
     */
    private void singleRadioMediaImage() {
        if (selectImages != null
                && selectImages.size() > 0) {
            isGo = true;
            LocalMedia media = selectImages.get(0);
            notifyItemChanged(
                    isGo ? media.position : media.position > 0 ? media.position - 1 : 0);
            selectImages.clear();
        }
    }

    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        int size = selectImages.size();
        for (int index = 0, length = size; index < length; index++) {
            LocalMedia media = selectImages.get(index);
            media.setNum(index + 1);
            notifyItemChanged(media.position);
        }
    }

    /**
     * 选中的图片并执行动画
     *
     * @param holder
     * @param isChecked
     * @param isAnim
     */
    public void selectImage(ViewHolder holder, boolean isChecked, boolean isAnim) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            if (isAnim) {
                if (animation != null) {
                    holder.check.startAnimation(animation);
                }
            }
            holder.ivPhoto.setColorFilter(ContextCompat.getColor
                    (context, R.color.image_overlay_true), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.ivPhoto.setColorFilter(ContextCompat.getColor
                    (context, R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public interface OnPhotoSelectChangedListener {

        void onRemove(LocalMedia media);

        /**
         * 已选Media回调
         *
         * @param selectImages
         */
        void onChange(List<LocalMedia> selectImages);

        /**
         * 图片预览回调
         *
         * @param media
         * @param position
         */
        void onPictureClick(LocalMedia media, int position);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener
                                                        imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }

    private void zoom(ImageView iv_img) {
        if (zoomAnim) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(iv_img, "scaleX", 1f, 1.12f),
                    ObjectAnimator.ofFloat(iv_img, "scaleY", 1f, 1.12f)
            );
            set.setDuration(DURATION);
            set.start();
        }
    }

    private void disZoom(ImageView iv_img) {
        if (zoomAnim) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(iv_img, "scaleX", 1.12f, 1f),
                    ObjectAnimator.ofFloat(iv_img, "scaleY", 1.12f, 1f)
            );
            set.setDuration(DURATION);
            set.start();
        }
    }
}
