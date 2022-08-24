package com.ranlychen.pieexpresstracking.service;

import androidx.annotation.NonNull;

import com.ranlychen.pieexpresstracking.entity.KdwNumberList;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.utils.preference.JsonObjectPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class LocalPackageDataService {
    private static final String KDW_PACKAGE_NUMBER_LIST_KEY = "KDW_PACKAGE_NUMBER_LIST_KEY";

    public static void getKdwPackageDetailList(@NonNull AbsRxSubscriber<List<PiePackageItem<KdwRespBean>>> absRxSubscriber){
        getKdwPackageNumList(new AbsRxSubscriber<KdwNumberList>() {
            @Override
            public void onNext(KdwNumberList kdwNumberList) {
                List<String> numList = kdwNumberList.getList();
                List<PiePackageItem<KdwRespBean>> itemList = new ArrayList<>();

                if(numList == null || kdwNumberList.getList().isEmpty()){
                    absRxSubscriber.onNext(itemList);
                    return;
                }

                for (String number: numList) {
                    getKdwPackageData(number, new AbsRxSubscriber<PiePackageItem<KdwRespBean>>() {
                        @Override
                        public void onNext(PiePackageItem<KdwRespBean> kdwRespBean) {
                            itemList.add(kdwRespBean);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            absRxSubscriber.onNext(itemList);
                        }
                    });
                }
                absRxSubscriber.onNext(itemList);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                absRxSubscriber.onError(throwable);
            }
        });
    }

    public static void getKdwPackageNumList(@NonNull AbsRxSubscriber<KdwNumberList> absRxSubscriber){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JsonObjectPreferenceUtil<KdwNumberList> kdwPackageDataList = new JsonObjectPreferenceUtil<KdwNumberList>(new KdwNumberList(), KDW_PACKAGE_NUMBER_LIST_KEY){};
                    KdwNumberList kdwNumberList = kdwPackageDataList.get();
                    absRxSubscriber.onNext(kdwNumberList);
                } catch (Exception e){
                    absRxSubscriber.onError(e);
                }
            }
        }).start();
    }

    public static void getKdwPackageData(@NonNull String number, @NonNull AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JsonObjectPreferenceUtil<PiePackageItem<KdwRespBean>> kdwPackageData = new JsonObjectPreferenceUtil<PiePackageItem<KdwRespBean>>(new PiePackageItem<KdwRespBean>(), number){};
                    PiePackageItem<KdwRespBean> kdwRespBean = kdwPackageData.get();
                    absRxSubscriber.onNext(kdwRespBean);
                } catch (Exception e){
                    absRxSubscriber.onError(e);
                }
            }
        }).start();

    }

    public static void saveKdwPackageData(@NonNull String number, @NonNull PiePackageItem<KdwRespBean> kdwRespBean, @NonNull AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        try {
            JsonObjectPreferenceUtil<PiePackageItem<KdwRespBean>> kdwPackageData = new JsonObjectPreferenceUtil<PiePackageItem<KdwRespBean>>(kdwRespBean, number){};
            absRxSubscriber.onNext(kdwRespBean);
        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }

    public static void updateKdwPackageData(@NonNull String number, @NonNull PiePackageItem<KdwRespBean> kdwRespBean, @NonNull AbsRxSubscriber<PiePackageItem<KdwRespBean>> absRxSubscriber){
        try {
            JsonObjectPreferenceUtil<PiePackageItem<KdwRespBean>> kdwPackageData = new JsonObjectPreferenceUtil<PiePackageItem<KdwRespBean>>(new PiePackageItem<KdwRespBean>(), number){};
            kdwPackageData.update(kdwRespBean);
            absRxSubscriber.onNext(kdwPackageData.get());
        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }

    public static void deleteKdwPackageData(@NonNull String number, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
        try {
            JsonObjectPreferenceUtil<KdwRespBean> kdwPackageData = new JsonObjectPreferenceUtil<KdwRespBean>(new KdwRespBean(), number){};
            absRxSubscriber.onNext(kdwPackageData.delete());
        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }
}
