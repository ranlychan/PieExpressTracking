package com.ranlychen.pieexpresstracking.service;


import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.ranlychen.pieexpresstracking.entity.KdwQueryFailException;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.KdwTraceLineEnum;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemLocalStorageBean;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.network.Api;
import com.ranlychen.pieexpresstracking.network.RetrofitHolder;
import com.ranlychen.pieexpresstracking.utils.KeyUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @description 网络请求服务
 * @author chencanyi
 * @time 2022/8/23 13:32
 */

public class PackageNetService {

    private static final String TAG = "PackageNetService";

    /**
     *
     * @param com 要查询的快递公司代
     * @param nu 要查询的快递单号，请勿带特殊符号，不支持中文（大小不敏感）
     * @param muti [0|1] 返回信息数量，0:返回多行完整的信息，1:只返回一行信息。不填默认返回多行
     * @param order [desc|asc] 排序。desc：按时间由新到旧排列，asc：按时间由旧到新排列。不填默认desc（大小不敏感）
     * @param absRxSubscriber
     */
    protected static Disposable getItemInfo(@NonNull String com, @NonNull String nu, KdwTraceLineEnum muti, String order, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
        Map<String, Object> param = new HashMap<>();
        param.put("id", KeyUtil.getKdwRequestId());
        param.put("com", com);
        param.put("nu", nu);
        if(muti != null){
            param.put("muti", muti.getTraceLine());
        }
        if(order != null){
            param.put("order", order);
        }

        return AbsRxSubscriber.addSubscribe(RetrofitHolder.INSTANCE.getKdwRetrofit().create(Api.class).getItemInfo(param), new AbsRxSubscriber<KdwRespBean>() {
            @Override
            public void onNext(KdwRespBean response) {
                Log.i(TAG, "getItemInfo is success");
                if(response.getSuccess()){
                    if (absRxSubscriber != null) {
                        PiePackageItemBean<KdwRespBean> piePackageItemBean = new PiePackageItemBean<>();
                        PiePackageItemLocalStorageBean localStorageBean = new PiePackageItemLocalStorageBean();
                        try {
                            localStorageBean.setItemJson(GsonUtils.toJson(response));
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        piePackageItemBean.setOnlineInfoBean(response);
                        piePackageItemBean.setLocalInfoBean(localStorageBean);

                        absRxSubscriber.onNext(piePackageItemBean);
                    }
                } else {
                    String reason = "";
                    if(!TextUtils.isEmpty(response.getReason())){
                        reason = response.getReason();
                    }
                    absRxSubscriber.onError(new KdwQueryFailException(reason));
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                Log.i(TAG, "getItemInfo is error", throwable);
                if (absRxSubscriber != null) {
                    absRxSubscriber.onError(throwable);
                }
            }
        });
    }
}
