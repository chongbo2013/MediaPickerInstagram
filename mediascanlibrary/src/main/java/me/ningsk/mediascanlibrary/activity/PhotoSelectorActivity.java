package me.ningsk.mediascanlibrary.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.ningsk.mediascanlibrary.R;
import me.ningsk.mediascanlibrary.adapter.FolderAdapter;
import me.ningsk.mediascanlibrary.adapter.MediaAdapter;
import me.ningsk.mediascanlibrary.docaration.GridSpacingItemDecoration;
import me.ningsk.mediascanlibrary.entity.LocalMedia;
import me.ningsk.mediascanlibrary.entity.LocalMediaFolder;
import me.ningsk.mediascanlibrary.fragment.PhotoSelectorFragment;
import me.ningsk.mediascanlibrary.loader.MediaLoader;
import me.ningsk.mediascanlibrary.permissions.RxPermissions;
import me.ningsk.mediascanlibrary.utils.DataTransferStation;
import me.ningsk.mediascanlibrary.utils.LogUtils;
import me.ningsk.mediascanlibrary.utils.ScreenUtils;
import me.ningsk.mediascanlibrary.utils.ToastManage;
import me.ningsk.mediascanlibrary.widget.FolderPopupWindow;

public class PhotoSelectorActivity extends PhotoBaseActivity{
    private static final String FRAGMENT_PHOTO = "fragment_photo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
        if (null == savedInstanceState) {
            PhotoSelectorFragment fragment = new PhotoSelectorFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, FRAGMENT_PHOTO)
                    .addToBackStack(FRAGMENT_PHOTO)
                    .commit();
        }
    }
}
