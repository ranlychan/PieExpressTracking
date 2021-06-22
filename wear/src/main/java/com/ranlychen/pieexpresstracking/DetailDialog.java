package com.ranlychen.pieexpresstracking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class DetailDialog extends Dialog implements View.OnClickListener {

    private static final int UPDATECOMPLETED = 3;
    private static final int UPDATEFAIL = 4;
    private static final int DELETECOMPLETED = 5;
    private static final int DELETEFAIL = 6;
    private static final int UNKNOWNERR = 7;

    private int index = -1;

    private Context context;
    private ImageView iv_logo;
    private TextView tv_name;
    private TextView tv_No;
    private TextView tv_trace;
    private ImageButton bt_delete;
    private ImageButton bt_update;
    private Item item;
    private IOnCancelListener deleteListener;
    private IOnConfirmListener updateListener;

    //DetailDialog类的构造方法
    public DetailDialog(@NonNull Context context, int position, Item item) {
        super(context);
        this.context = context;
        this.index = position-1;
        this.item = item;
    }
    public DetailDialog(@NonNull Context context, int themeResId, int position, Item item) {
        super(context, themeResId);
        this.context = context;
        this.index = position-1;
        this.item = item;
    }

    public interface IOnCancelListener{
        void onDelete(int state);
    }
    public interface IOnConfirmListener{
        void onUpdate(int state);
    }

    public void setItem(Item item) {
        this.item = item;
    }
    public void setDelete(IOnCancelListener deleteListener) {
        this.deleteListener = deleteListener;
    }
    public void setUpdate(IOnConfirmListener updateListener){
        this.updateListener = updateListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_item_detail);
        tv_name = findViewById(R.id.detail_name);
        tv_No = findViewById(R.id.detail_No);
        tv_trace = findViewById(R.id.detail_trace);
        iv_logo = findViewById(R.id.detail_logo);
        bt_delete = findViewById(R.id.detail_bt_delete);
        bt_update = findViewById(R.id.detail_bt_update);

        updateView();

        //为两个按钮添加点击事件
        bt_update.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDATECOMPLETED:
                    updateView();
                    break;
                case UPDATEFAIL:
                    break;
                default:
                    updateView();
                    break;
            }
        }
    };

    private void updateView(){
        iv_logo.setImageResource(item.getIcon());
        tv_name.setText(context.getString(R.string.textview_item_name)+item.getName());
        tv_No.setText(context.getString(R.string.textview_item_No)+item.getLogisticCode());
        tv_trace.setText(context.getString(R.string.textview_item_state)+item.getState()+"\n");

        if(item.getStateCode().equals("0")){
            tv_trace.append("\n"+"原因："+item.getReason()+"\n");
        }
        else{
            for(int i = item.getTraces().length-1;i>0;i--){
                tv_trace.append("\n"+"时间："+item.getTraces()[i][0]);
                tv_trace.append("\n"+"地点："+item.getTraces()[i][1]+"\n");
                if(!item.getTraces()[i][2].equals("")){
                    tv_trace.append(""+"备注："+item.getTraces()[i][2]+"\n");
                }
            }
        }

    }

    //重写onClick方法
    public void onClick(View view) {

        final String indexFilePath = context.getString(R.string.folderpath)+context.getString(R.string.filename_expNo)+context.getString(R.string.filetail);
        final DataIO io = new DataIO(context);

        switch (view.getId()){
            case R.id.detail_bt_update:

                final KdniaoTrackQueryAPI tqapi = new KdniaoTrackQueryAPI();

                /**
                 * 保存详细信息操作
                 */
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        Message msg = new Message();
                        try{
                            String result = tqapi.getOrderTracesByJson(item.getShipperCode(), item.getLogisticCode());
                            JSONObject newResult = new JSONObject(result);
                            newResult.put("Name",item.getName());
                            result = newResult.toString();

                            if(io.saveToLocal(result, item.getLogisticCode()).equals("true") &&item.readJson(result)==null){
                                msg.what = UPDATECOMPLETED;
                                updateListener.onUpdate(UPDATECOMPLETED);//更新成功
                            }
                            else{
                                msg.what = UPDATEFAIL;
                                updateListener.onUpdate(UPDATEFAIL);//更新失败
                            }
                        } catch (Exception e) {
                            msg.what = UNKNOWNERR;
                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);

                    } }).start();
                break;

            case R.id.detail_bt_delete:

                KdnJsonReader rh = new KdnJsonReader();
                String indexFile =io.openJsonFile(indexFilePath);
                if (indexFile == null||index==-1){
                    deleteListener.onDelete(UNKNOWNERR);
                }else{
                    if(io.saveToLocal(rh.deleteJson(indexFile,index),null)=="true"){
                        deleteListener.onDelete(DELETECOMPLETED);//删除成功
                    }else{
                        deleteListener.onDelete(DELETEFAIL);//删除失败
                    }
                }
                dismiss();
                break;
        }
    }
}