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
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean;
import com.ranlychen.pieexpresstracking.factory.PackageCompanyIconFactory;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.service.LocalPackageDataService;
import com.ranlychen.pieexpresstracking.service.PackageService;

import java.util.List;

public class KdwDetailDialog extends Dialog implements View.OnClickListener {

    private ImageView iv_logo;
    private TextView tv_name;
    private TextView tv_No;
    private TextView tv_trace;
    private ImageButton bt_delete;
    private ImageButton bt_update;
    private PiePackageItemBean<KdwRespBean> item;

    private MainActivity.NotifyItemChangedListener notifyItemChangedListener;

    private PackageCompanyIconFactory<KdwRespBean> iconFactory = new PackageCompanyIconFactory<>();

    //DetailDialog类的构造方法
    public KdwDetailDialog(@NonNull Context context, PiePackageItemBean<KdwRespBean> item, MainActivity.NotifyItemChangedListener listener) {
        super(context);
        this.item = item;
        this.notifyItemChangedListener = listener;
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

        initData(item);
    }

    private void initData(PiePackageItemBean<KdwRespBean> newItem){
        iv_logo.post(new Runnable() {
            @Override
            public void run() {
                iv_logo.setImageResource(iconFactory.getComIconResId(newItem));
            }
        });
        tv_name.post(new Runnable() {
            @Override
            public void run() {
                tv_name.setText(String.format("%s%s", getContext().getString(R.string.textview_item_name), newItem.getLocalInfoBean().getMarkName()));
            }
        });
        tv_No.post(new Runnable() {
            @Override
            public void run() {
                tv_No.setText(String.format("%s%s", getContext().getString(R.string.textview_item_No), newItem.getLocalInfoBean().getLogisticCode()));
            }
        });
        tv_trace.post(new Runnable() {
            @Override
            public void run() {
                KdwStatusEnum kdwStatusEnum = KdwStatusEnum.Companion.fromInt(newItem.getOnlineInfoBean().getStatus());
                String statusName = "未知状态";
                if(kdwStatusEnum != null){
                    statusName = kdwStatusEnum.getStatusName();
                }
                tv_trace.setText(String.format("%s%s\n", getContext().getString(R.string.textview_item_state), statusName));
                if(kdwStatusEnum == KdwStatusEnum.TRANSPORT_PROBLEM){
                    tv_trace.append(String.format("\n原因：%s\n", newItem.getOnlineInfoBean().getReason()));
                } else {
                    List<KdwTraceBean> traceBeanList = newItem.getOnlineInfoBean().getData();
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
        });

    }

    private void onUpdateSuccess(PiePackageItemBean<KdwRespBean> newItem){
        initData(newItem);
        notifyItemChangedListener.notifyItemChanged();
    }

    //重写onClick方法
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detail_bt_update:
                if(item == null){
                    ToastUtils.showShort("失败,请退出重进");
                    return;
                }
                PackageService.queryPackageDetail(
                        item.getLocalInfoBean().getMarkName(),
                        item.getLocalInfoBean().getCompanyCode(),
                        item.getLocalInfoBean().getLogisticCode(),
                        new AbsRxSubscriber<PiePackageItemBean<KdwRespBean>>() {
                            @Override
                            public void onNext(PiePackageItemBean<KdwRespBean> kdwRespBeanPiePackageItemBean) {

                                item = kdwRespBeanPiePackageItemBean;
                                LocalPackageDataService.updateKdwPackageData(item.getLocalInfoBean(), new AbsRxSubscriber<Boolean>() {
                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        ToastUtils.showShort("更新成功");
                                        onUpdateSuccess(item);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        super.onError(throwable);
                                        ToastUtils.showShort("本地信息更新失败");
                                    }
                                });

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                ToastUtils.showShort("联网查询失败");
                            }
                });



                break;

            case R.id.detail_bt_delete:
                if(item == null){
                    ToastUtils.showShort("失败,请退出重进");
                    return;
                }
                LocalPackageDataService.deleteKdwPackageData(item.getLocalInfoBean(), new AbsRxSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        ToastUtils.showShort("删除成功");
                        notifyItemChangedListener.notifyItemChanged();
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