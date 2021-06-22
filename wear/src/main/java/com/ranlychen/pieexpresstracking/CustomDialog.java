package com.ranlychen.pieexpresstracking;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.support.wearable.view.CircledImageView;
import androidx.annotation.NonNull;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private TextView tv_message;
    private CircledImageView bt_cancel,bt_confirm;
    private String message;
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;


    public void setMessage(String message) {
        this.message = message;
    }
    public void setCancel(IOnCancelListener cancelListener) {
        this.cancelListener=cancelListener;
    }
    public void setConfirm(IOnConfirmListener confirmListener){
        this.confirmListener=confirmListener;
    }

    //CustomDialog类的构造方法
    public CustomDialog(@NonNull Context context) {
        super(context);
    }
    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog);
        tv_message=findViewById(R.id.dialog_message);
        bt_cancel=findViewById(R.id.dialog_bt_cancel);
        bt_confirm=findViewById(R.id.dialog_bt_confirm);

        if (!TextUtils.isEmpty(message)){
            tv_message.setText(message);
        }

        //为两个按钮添加点击事件
        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
    }

    //重写onClick方法
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_bt_cancel:
                if(cancelListener!=null){
                    cancelListener.onCancel(this);
                }
                dismiss();
                break;
            case R.id.dialog_bt_confirm:
                if(confirmListener!=null){
                    confirmListener.onConfirm(this);
                }
                dismiss();
                break;
        }
    }


    public interface IOnCancelListener{
        void onCancel(CustomDialog dialog);
    }
    public interface IOnConfirmListener{
        void onConfirm(CustomDialog dialog);
    }
}