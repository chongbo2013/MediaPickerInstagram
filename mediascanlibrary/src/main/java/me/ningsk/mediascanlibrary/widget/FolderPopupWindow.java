package me.ningsk.mediascanlibrary.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.adapter.FolderAdapter;
import me.ningsk.mediascanlibrary.docaration.RecycleViewDivider;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.entity.LocalMediaFolder;
import me.ningsk.mediascanlibrary.utils.AttrsUtils;
import me.ningsk.mediascanlibrary.utils.ScreenUtils;
import me.ningsk.mediascanlibrary.utils.StringUtils;


public class FolderPopupWindow extends PopupWindow implements View.OnClickListener{

    private Context mContext;
    private View window;
    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private Animation animationIn, animationOut;
    private boolean isDismiss = false;
    private LinearLayout layoutRoot;
    private TextView tvTitle;
    private Drawable drawableUp, drawableDown;

    public FolderPopupWindow(Context context, int mediaMimeType) {
        this.mContext = context;
        window = LayoutInflater.from(mContext).inflate(R.layout.photo_window_folder, null);
        this.setContentView(window);
        this.setWidth(ScreenUtils.getScreenWidth(mContext));
        this.setHeight(ScreenUtils.getScreenHeight(mContext));
        this.setAnimationStyle(R.style.WindowStyle);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(123, 0 ,0 ,0)));
        drawableUp = AttrsUtils.getTypeValuePopWindowImg(mContext, R.attr.photo_arrow_up_icon);
        drawableDown = AttrsUtils.getTypeValuePopWindowImg(mContext, R.attr.photo_arrow_down_icon);
        animationIn = AnimationUtils.loadAnimation(mContext, R.anim.photo_album_show);
        animationOut = AnimationUtils.loadAnimation(mContext, R.anim.photo_album_dismiss);
        initView();
    }

    public void initView() {
        layoutRoot = window.findViewById(R.id.layout_root);
        adapter = new FolderAdapter(mContext);
        recyclerView = window.findViewById(R.id.folder_list);
        recyclerView.getLayoutParams().height = (int) (ScreenUtils.getScreenHeight(mContext));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, ScreenUtils.dip2px(mContext, 0), ContextCompat.getColor(mContext, R.color.transparent)));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        layoutRoot.setOnClickListener(this);
    }

    public void bindFolder(List<LocalMediaFolder> folders) {
        adapter.bindFolderData(folders);
    }

    public void setPhotoTitleView(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    @Override
    public void showAsDropDown(View anchor) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                Rect rect = new Rect();
                anchor.getGlobalVisibleRect(rect);
                int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                setHeight(h);
            }
            super.showAsDropDown(anchor);
            isDismiss = false;
            recyclerView.startAnimation(animationIn);
            StringUtils.modifyTextViewDrawable(tvTitle, drawableUp, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(FolderAdapter.OnItemClickListener onItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void dismiss() {
        if (isDismiss) {
            return;
        }
        StringUtils.modifyTextViewDrawable(tvTitle, drawableDown, 2);
        isDismiss = true;
        recyclerView.startAnimation(animationOut);
        dismiss();
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDismiss = false;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    dismiss4Pop();
                } else {
                    dismiss();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 在android4.1.1和4.1.2版本关闭PopWindow
     */
    private void dismiss4Pop() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        });
    }


    /**
     * 设置选中状态
     */
    public void notifyDataCheckedStatus(List<LocalMedia> medias) {
        try {
            // 获取选中图片
            List<LocalMediaFolder> folders = adapter.getFolderData();
            for (LocalMediaFolder folder : folders) {
                folder.setCheckedNum(0);
            }
            if (medias.size() > 0) {
                for (LocalMediaFolder folder : folders) {
                    int num = 0;// 记录当前相册下有多少张是选中的
                    List<LocalMedia> images = folder.getMedias();
                    for (LocalMedia media : images) {
                        String path = media.getPath();
                        for (LocalMedia m : medias) {
                            if (path.equals(m.getPath())) {
                                num++;
                                folder.setCheckedNum(num);
                            }
                        }
                    }
                }
            }
            adapter.bindFolderData(folders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.layout_root) {
            dismiss();
        }
    }
}
