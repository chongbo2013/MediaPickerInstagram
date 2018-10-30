package me.ningsk.videorecord;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import butterknife.BindView;
import butterknife.OnClick;
import me.ningsk.videorecord.base.MyApplication;
import me.ningsk.videorecord.base.activity.BaseActivity;

public  class CaptureVideoActivity extends BaseActivity {


    @Override
    protected int setLayoutId() {
        return 0;
    }
}
