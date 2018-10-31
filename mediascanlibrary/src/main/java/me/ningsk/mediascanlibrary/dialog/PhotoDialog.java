package me.ningsk.mediascanlibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import me.ningsk.mediascanlibrary.R;


public class PhotoDialog extends Dialog {
    public Context context;

    public PhotoDialog(Context context) {
        super(context, R.style.photo_alert_dialog);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.DialogWindowStyle);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_alert_dialog);
    }
}
