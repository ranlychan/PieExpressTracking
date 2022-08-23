package com.ranlychen.pieexpresstracking.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.entity.KdwExpCompanyEnum;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.sdk.KdApiOrderDistinguish;
import com.ranlychen.pieexpresstracking.sdk.KdniaoTrackQueryAPI;
import com.ranlychen.pieexpresstracking.service.PackageService;
import com.ranlychen.pieexpresstracking.utils.DataIOUtil;
import com.ranlychen.pieexpresstracking.utils.KdnJsonReaderUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PieAddItemDialog extends Dialog implements View.OnClickListener{

    private Button bt_confirm,bt_cancel;
    EditText inputNameText;
    EditText inputExpNoText;
    private Spinner spCompany;
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;
    private View.OnClickListener voiceListenerForName;
    private View.OnClickListener voiceListenerForNo;

    public interface IOnCancelListener{
        void onCancel(PieAddItemDialog dialog);
    }
    public interface IOnConfirmListener{
        void onConfirm(boolean isSuccess);
    }


    public void setCancel(IOnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }
    public void setConfirm(IOnConfirmListener confirmListener){
        this.confirmListener = confirmListener;
    }
    public void setVoiceListenerForName(View.OnClickListener voiceListener){
        this.voiceListenerForName = voiceListener;
    }
    public void setVoiceListenerForNo(View.OnClickListener voiceListener){
        this.voiceListenerForNo = voiceListener;
    }


    //AddItemDialog类的构造方法
    public PieAddItemDialog(@NonNull Context context) {
        super(context);
    }
    public PieAddItemDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_item);
        bt_cancel=findViewById(R.id.dialog_bt_cancel);
        bt_confirm=findViewById(R.id.dialog_bt_confirm);
        inputNameText=findViewById(R.id.inputName);
        inputExpNoText=findViewById(R.id.inputExpNo);
        spCompany=findViewById(R.id.sp_company);

        CircledImageView voice = findViewById(R.id.dialog_bt_voice);
        CircledImageView voice2 = findViewById(R.id.dialog_bt_voice2);


        //小写字母显示成大写字母
        inputExpNoText.setTransformationMethod(new ReplacementTransformationMethod() {
            @Override
            protected char[] getOriginal() {
                char[] originalCharArr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
                return originalCharArr;
            }

            @Override
            protected char[] getReplacement() {
                char[] replacementCharArr = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
                return replacementCharArr;
            }
        });

        //为两个按钮添加点击事件
        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        voice.setOnClickListener(voiceListenerForName);
        voice2.setOnClickListener(voiceListenerForNo);

        ArrayAdapter<KdwExpCompanyEnum> companyAdapter = new ArrayAdapter<KdwExpCompanyEnum>(getContext(), R.layout.item_spinner_company, KdwExpCompanyEnum.values());
        spCompany.setAdapter(companyAdapter);


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

                String markName = inputNameText.getText().toString().trim();
                String expNumber = convertString( inputExpNoText.getText().toString().trim());

                // TODO: 2022/8/23
                String companyCode = "";

                //默认备注名
                if(TextUtils.isEmpty(markName)){
                    markName = getContext().getResources().getString((R.string.unNamedItem));
                }

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            PackageService.queryPackageDetail(companyCode, expNumber, new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
                                @Override
                                public void onNext(PiePackageItem<KdwRespBean> kdwRespBeanPiePackageItem) {
                                    ToastUtils.showLong("添加成功");
                                    confirmListener.onConfirm(true);
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                    ToastUtils.showLong("添加失败");

                                    confirmListener.onConfirm(false);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }).start();
                dismiss();//按钮按之后会消失
                break;
        }
    }

    /**
     * 方 法 名：convertString(String str)
     * 功    能：将传入的字符串中的小写字母转为大写字母
     * 参    数：String str
     * 返 回 值：String
     */
    public static String convertString(String str)
    {
        String upStr = str.toUpperCase();
        StringBuffer buf = new StringBuffer(str.length());
        for(int i=0;i<str.length();i++) {
            buf.append(upStr.charAt(i));
        }
        return   buf.toString();
    }

}