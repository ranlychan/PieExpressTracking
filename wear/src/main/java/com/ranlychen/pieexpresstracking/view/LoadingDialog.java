package com.ranlychen.pieexpresstracking.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ranlychen.pieexpresstracking.R;
import com.victor.loading.rotate.RotateLoading;

public class LoadingDialog extends Dialog {
    private TextView showTip;
    private RotateLoading rotateLoading;
    protected Context mContext;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.dialog_loading_style);
        mContext = context;
        init();
    }

    protected void init() {
        setContentView(R.layout.dialog_waiting_lay);
        setCanceledOnTouchOutside(false);
        rotateLoading = findViewById(R.id.rotate_loading);
    }

    @Override
    public void show() {
        super.show();
        rotateLoading.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        rotateLoading.stop();
        rotateLoading.clearAnimation();
    }
}
