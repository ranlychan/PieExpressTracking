package com.ranlychen.pieexpresstracking.service;


import androidx.annotation.NonNull;

import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.KdwTraceLineEnum;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PackageService {

    private static final String TAG = "PackageNetService";

    /**
     * 使用接口查询快递详细信息
     *
     * @param company
     * @param number
     * @param absRxSubscriber
     * @return
     */
    public static Disposable queryPackageDetail(@NonNull String company, @NonNull String number, AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        return PackageNetService.getItemInfo(company, number, KdwTraceLineEnum.MULTI_LINE, null, new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
            @Override
            public void onNext(PiePackageItem<KdwRespBean> kdwRespBeanPiePackageItem) {
                LocalPackageDataService.saveKdwPackageData(number, kdwRespBeanPiePackageItem, new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
                    @Override
                    public void onNext(PiePackageItem<KdwRespBean> kdwRespBeanPiePackageItem) {
                        absRxSubscriber.onNext(kdwRespBeanPiePackageItem);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        absRxSubscriber.onError(throwable);
                    }
                });

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                absRxSubscriber.onError(throwable);
            }
        });
    }

    public static Disposable updatePackageDetail(@NonNull String company, @NonNull String number, AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        return PackageNetService.getItemInfo(company, number, KdwTraceLineEnum.MULTI_LINE, null, new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
            @Override
            public void onNext(PiePackageItem<KdwRespBean> kdwRespBeanPiePackageItem) {
                LocalPackageDataService.saveKdwPackageData(number, kdwRespBeanPiePackageItem, new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
                    @Override
                    public void onNext(PiePackageItem<KdwRespBean> kdwRespBeanPiePackageItem) {
                        absRxSubscriber.onNext(kdwRespBeanPiePackageItem);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        absRxSubscriber.onError(throwable);
                    }
                });

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                absRxSubscriber.onError(throwable);
            }
        });
    }

    /**
     * 使用接口查询快递简要信息(一行路径信息)
     *
     * @param company
     * @param number
     * @param absRxSubscriber
     * @return
     */
    public static Disposable queryPackageOverview(@NonNull String company, @NonNull String number, AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        return PackageNetService.getItemInfo(company, number, KdwTraceLineEnum.SINGLE_LINE, null, absRxSubscriber);
    }

    /**
     * 查询本地存储的快递详细
     *
     * @param number
     * @param absRxSubscriber
     * @return
     */
    public static void queryLocalPackageDetail(@NonNull String number, AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        LocalPackageDataService.getKdwPackageData(number, absRxSubscriber);
    }

    public static void queryLocalPackageDetailList(@NonNull AbsRxSubscriber<List<PiePackageItem<KdwRespBean>>> absRxSubscriber){
        LocalPackageDataService.getKdwPackageDetailList(absRxSubscriber);
    }

    public static void deletePackageDetail(@NonNull String number, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
        LocalPackageDataService.deleteKdwPackageData(number, absRxSubscriber);
    }

}
