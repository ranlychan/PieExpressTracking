package com.ranlychen.pieexpresstracking.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.ranlychen.pieexpresstracking.utils.DataIOUtil;
import com.ranlychen.pieexpresstracking.sdk.KdApiOrderDistinguish;
import com.ranlychen.pieexpresstracking.utils.KdnJsonReaderUtil;
import com.ranlychen.pieexpresstracking.sdk.KdniaoTrackQueryAPI;
import com.ranlychen.pieexpresstracking.R;

import org.json.JSONObject;

public class AddItemDialog extends Dialog implements View.OnClickListener{

    private static final int ADDCOMPLETED = 0;
    private static final int UNSUPPORTEDNO = 1;
    private static final int ADDFAIL = 2;
    private static final int UNKNOWNERR = 7;

    private static final int NAME = 1;
    private static final int NO = 2;

    final private Context context;
    private Button bt_confirm,bt_cancel;
    String inputName;
    String inputExpNo;
    EditText inputNameText;
    EditText inputExpNoText;
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;
    private View.OnClickListener voiceListenerForName;
    private View.OnClickListener voiceListenerForNo;

    public interface IOnCancelListener{
        void onCancel(AddItemDialog dialog);
    }
    public interface IOnConfirmListener{
        void onConfirm(int state);
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
    public AddItemDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    public AddItemDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_item);
        bt_cancel=findViewById(R.id.dialog_bt_cancel);
        bt_confirm=findViewById(R.id.dialog_bt_confirm);
        inputNameText=findViewById(R.id.inputName);
        inputExpNoText=findViewById(R.id.inputExpNo);

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
                inputName = inputNameText.getText().toString().trim();
                inputExpNo = convertString( inputExpNoText.getText().toString().trim());

                //System.out.println("inputName:"+inputName);
                //System.out.println("inputNo:"+inputExpNo.trim());

                if(TextUtils.isEmpty(inputName)){
                    inputName = context.getResources().getString((R.string.unNamedItem));
                }

                final KdApiOrderDistinguish odapi = new KdApiOrderDistinguish();
                final KdniaoTrackQueryAPI tqapi = new KdniaoTrackQueryAPI();
                final KdnJsonReaderUtil rh = new KdnJsonReaderUtil();
                final DataIOUtil io = new DataIOUtil(context);

                /**
                 * 在Android4.0以后，只要是写在主线程（就是Activity）中的HTTP请求，运行时都会报错
                 * Android这个设计是为了防止网络请求时间过长而导致界面假死的情况发生。
                 * 故需要在此启动子线程进行调用
                 */
                new Thread(new Runnable(){

                    @Override
                    public void run() {

                        String expCode;

                        /**
                         *调用菜鸟快递API查询快递信息
                         * 由于没钱买API套餐，没办法只用运单号直接查
                         * 现在需要先查询单号公司，再根据公司代码和运单号查询快递信息
                         */

                        try {

                            expCode = rh.readShipperCode(odapi.getOrderTracesByJson(inputExpNo).trim());
                            /**
                             * 若返回了expCode，则运单有效
                             * 运单有效，则查询运单详细信息为result并保存
                             * [问题所在]->用DataIO.openJsonFile();获取本地运单号引索expNos,读取为String，和当前窗口获取的inputExp一起传入ResultHandler.updatJson();
                             * 得到String类的新expNos引索，Json格式
                             * 新expNos引索传入DataIO.saveToLOcal
                             */

                            if(expCode!=null&&(expCode.equals("ZTO")||expCode.equals("YTO")||expCode.equals("STO"))){


                                /**
                                 * 更新expNos.json操作
                                 */
                                String oldExpNos = io.openJsonFile(context.getString(R.string.folderpath) + context.getString(R.string.filename_expNo) + context.getString(R.string.filetail));
                                String newExpNos = rh.addNewJson(oldExpNos,inputExpNo);
                                io.saveToLocal(newExpNos,null);


                                /**
                                 * 保存详细信息操作
                                 */

                                String result = tqapi.getOrderTracesByJson(expCode,inputExpNo);
                                System.out.println(result.trim());
                                JSONObject newResult = new JSONObject(result);
                                newResult.put("Name",inputName);
                                result = newResult.toString();

                                if(io.saveToLocal(result,inputExpNo).equals("true")){
                                    confirmListener.onConfirm(ADDCOMPLETED);
                                }
                                else{
                                    confirmListener.onConfirm(ADDFAIL);
                                }
                            }
                            else{
                                confirmListener.onConfirm(UNSUPPORTEDNO);
                            }

                        } catch (Exception e) {
                            confirmListener.onConfirm(UNKNOWNERR);
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

    public void setEditViewText(int editViewName,String s){
        switch (editViewName){
            case NAME: inputNameText.setText(s);break;
            case NO: inputExpNoText.setText(s);break;
        }
    }

}