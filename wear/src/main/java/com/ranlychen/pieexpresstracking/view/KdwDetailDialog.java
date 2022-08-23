package com.ranlychen.pieexpresstracking.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.KdwStatusEnum;
import com.ranlychen.pieexpresstracking.entity.KdwTraceBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;
import com.ranlychen.pieexpresstracking.factory.PackageCompanyIconFactory;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.service.PackageService;
import com.ranlychen.pieexpresstracking.utils.DataIOUtil;

import java.util.List;

public class KdwDetailDialog extends Dialog implements View.OnClickListener {

    private ImageView iv_logo;
    private TextView tv_name;
    private TextView tv_No;
    private TextView tv_trace;
    private ImageButton bt_delete;
    private ImageButton bt_update;
    private PiePackageItem<KdwRespBean> item;

    private PackageCompanyIconFactory<KdwRespBean> iconFactory = new PackageCompanyIconFactory<>();

    //DetailDialog类的构造方法
    public KdwDetailDialog(@NonNull Context context, PiePackageItem<KdwRespBean> item) {
        super(context);
        this.item = item;
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


        //为两个按钮添加点击事件
        bt_update.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
    }

    private void onUpdateSuccess(PiePackageItem<KdwRespBean> newItem){
        iv_logo.setImageResource(iconFactory.getComIconResId(newItem));
        tv_name.setText(String.format("%s%s", getContext().getString(R.string.textview_item_name), newItem.getMarkName()));
        tv_No.setText(String.format("%s%s", getContext().getString(R.string.textview_item_No), newItem.getLogisticCode()));

        KdwStatusEnum kdwStatusEnum = KdwStatusEnum.Companion.fromInt(newItem.getItem().getStatus());
        String statusName = "未知状态";
        if(kdwStatusEnum != null){
            statusName = kdwStatusEnum.getStatusName();
        }
        tv_trace.setText(String.format("%s%s\n", getContext().getString(R.string.textview_item_state), statusName));
        if(kdwStatusEnum == KdwStatusEnum.TRANSPORT_PROBLEM){
            tv_trace.append(String.format("\n原因：%s\n", newItem.getItem().getReason()));
        } else {
            List<KdwTraceBean> traceBeanList = newItem.getItem().getData();
            if(traceBeanList == null){
                tv_trace.append("\n无轨迹信息\n");
            } else {
                for(KdwTraceBean traceBean: traceBeanList){
                    tv_trace.append("\n"+"时间："+ traceBean.getTime());
                    tv_trace.append("\n"+"地点："+ traceBean.getContext()+"\n");
                }
            }
        }

    }

    //重写onClick方法
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detail_bt_update:
                if(item == null){
                    ToastUtils.showShort("失败,请退出重进");
                    return;
                }
                PackageService.updatePackageDetail(item.getCompanyCode(), item.getLogisticCode(), new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
                    @Override
                    public void onNext(PiePackageItem<KdwRespBean> kdwRespBeanPiePackageItem) {
                        onUpdateSuccess(kdwRespBeanPiePackageItem);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("更新失败");
                    }
                });

                break;

            case R.id.detail_bt_delete:
                if(item == null){
                    ToastUtils.showShort("失败,请退出重进");
                    return;
                }
                PackageService.deletePackageDetail(item.getLogisticCode(), new AbsRxSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        ToastUtils.showShort("删除成功");
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("删除失败");
                    }
                });
                break;
        }
    }
}