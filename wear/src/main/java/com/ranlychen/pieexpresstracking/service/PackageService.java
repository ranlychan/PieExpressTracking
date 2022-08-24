package com.ranlychen.pieexpresstracking.service;


import androidx.annotation.NonNull;

import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.KdwTraceLineEnum;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;

import io.reactivex.disposables.Disposable;

public class PackageService {

    private static final String TAG = "PackageNetService";

//    public static void savePackageDetail(@NonNull PiePackageItemBean<KdwRespBean> item, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
//        LocalPackageDataService.saveKdwPackageData(item, absRxSubscriber);
//    }

//    public static void savePackageDetail(@NonNull String markName, @NonNull String companyCode, @NonNull String number, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
//        PiePackageItemBean<KdwRespBean> item = new PiePackageItemBean<>();
//        item.setMarkName(markName);
//        item.setCompanyCode(companyCode);
//        item.setLogisticCode(number);
//        savePackageDetail(item, absRxSubscriber);
//    }

    /**
     * 使用接口查询快递详细信息
     *
     * @param companyCode
     * @param number
     * @param absRxSubscriber
     * @return
     */
    public static Disposable queryPackageDetail(@NonNull String markName, @NonNull String companyCode, @NonNull String number, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
        return PackageNetService.getItemInfo(companyCode, number, KdwTraceLineEnum.MULTI_LINE, null, new AbsRxSubscriber<PiePackageItemBean<KdwRespBean>>() {
            @Override
            public void onNext(PiePackageItemBean<KdwRespBean> kdwRespBeanPiePackageItemBean) {
                if(kdwRespBeanPiePackageItemBean!=null){
                    kdwRespBeanPiePackageItemBean.getLocalInfoBean().setMarkName(markName);
                    kdwRespBeanPiePackageItemBean.getLocalInfoBean().setCompanyCode(companyCode);
                    kdwRespBeanPiePackageItemBean.getLocalInfoBean().setLogisticCode(number);
                }
                absRxSubscriber.onNext(kdwRespBeanPiePackageItemBean);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                absRxSubscriber.onError(throwable);
            }
        });
    }
//
//    public static Disposable updatePackageDetail(@NonNull String companyCode, @NonNull String number, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
//        return PackageNetService.getItemInfo(companyCode, number, KdwTraceLineEnum.MULTI_LINE, null, new AbsRxSubscriber<PiePackageItemBean<KdwRespBean>>() {
//            @Override
//            public void onNext(PiePackageItemBean<KdwRespBean> kdwRespBeanPiePackageItemBean) {
//                LocalPackageDataService.saveKdwPackageData(kdwRespBeanPiePackageItemBean, new AbsRxSubscriber<PiePackageItemBean<KdwRespBean>>() {
//                    @Override
//                    public void onNext(PiePackageItemBean<KdwRespBean> kdwRespBeanPiePackageItemBean) {
//                        absRxSubscriber.onNext(kdwRespBeanPiePackageItemBean);
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        absRxSubscriber.onError(throwable);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                super.onError(throwable);
//                absRxSubscriber.onError(throwable);
//            }
//        });
//    }

    /**
     * 使用接口查询快递简要信息(一行路径信息)
     *
     * @param companyCode
     * @param number
     * @param absRxSubscriber
     * @return
     */
    public static Disposable queryPackageOverview(@NonNull String companyCode, @NonNull String number, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
        return PackageNetService.getItemInfo(companyCode, number, KdwTraceLineEnum.SINGLE_LINE, null, absRxSubscriber);
    }

//    /**
//     * 查询本地存储的快递详细
//     *
//     * @param number
//     * @param absRxSubscriber
//     * @return
//     */
//    public static void queryLocalPackageDetail(@NonNull String number, AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
//        LocalPackageDataService.getKdwPackageData(number, absRxSubscriber);
//    }
//
//    public static void queryLocalPackageDetailList(@NonNull AbsRxSubscriber<List<PiePackageItemBean<KdwRespBean>>> absRxSubscriber){
//        LocalPackageDataService.getKdwPackageDetailList(absRxSubscriber);
//    }
//
//    public static void deletePackageDetail(@NonNull String number, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
//        LocalPackageDataService.deleteKdwPackageData(number, absRxSubscriber);
//    }

}
