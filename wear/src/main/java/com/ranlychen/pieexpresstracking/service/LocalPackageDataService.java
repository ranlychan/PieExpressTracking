package com.ranlychen.pieexpresstracking.service;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.dbflow5.config.FlowManager;
import com.dbflow5.query.SQLite;
import com.ranlychen.pieexpresstracking.dbflow.AppDataBase;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemLocalStorageBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemLocalStorageBean_Table;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;

import java.util.ArrayList;
import java.util.List;

public class LocalPackageDataService {
//    private static final String KDW_PACKAGE_NUMBER_LIST_KEY = "KDW_PACKAGE_NUMBER_LIST_KEY";
//
//    public static void getKdwPackageDetailList(@NonNull AbsRxSubscriber<List<PiePackageItemBean<KdwRespBean>>> absRxSubscriber){
//        getKdwPackageNumList(new AbsRxSubscriber<KdwNumberList>() {
//            @Override
//            public void onNext(KdwNumberList kdwNumberList) {
//                List<String> numList = kdwNumberList.getList();
//                List<PiePackageItemBean<KdwRespBean>> itemList = new ArrayList<>();
//
//                if(numList == null || kdwNumberList.getList().isEmpty()){
//                    absRxSubscriber.onNext(itemList);
//                    return;
//                }
//
//                for (String number: numList) {
//                    getKdwPackageData(number, new AbsRxSubscriber<PiePackageItemBean<KdwRespBean>>() {
//                        @Override
//                        public void onNext(PiePackageItemBean<KdwRespBean> kdwRespBean) {
//                            itemList.add(kdwRespBean);
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            super.onError(throwable);
//                            absRxSubscriber.onNext(itemList);
//                        }
//                    });
//                }
//                absRxSubscriber.onNext(itemList);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                super.onError(throwable);
//                absRxSubscriber.onError(throwable);
//            }
//        });
//    }
//
//    public static void getKdwPackageNumList(@NonNull AbsRxSubscriber<KdwNumberList> absRxSubscriber){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JsonObjectPreferenceUtil<KdwNumberList> kdwPackageDataList = new JsonObjectPreferenceUtil<KdwNumberList>(new KdwNumberList(), KDW_PACKAGE_NUMBER_LIST_KEY){};
//                    KdwNumberList kdwNumberList = kdwPackageDataList.get();
//                    absRxSubscriber.onNext(kdwNumberList);
//                } catch (Exception e){
//                    absRxSubscriber.onError(e);
//                }
//            }
//        }).start();
//    }
//
//    public static void getKdwPackageData(@NonNull String number, @NonNull AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JsonObjectPreferenceUtil<PiePackageItemBean<KdwRespBean>> kdwPackageData = new JsonObjectPreferenceUtil<PiePackageItemBean<KdwRespBean>>(new PiePackageItemBean<KdwRespBean>(), number){};
//                    PiePackageItemBean<KdwRespBean> kdwRespBean = kdwPackageData.get();
//                    absRxSubscriber.onNext(kdwRespBean);
//                } catch (Exception e){
//                    absRxSubscriber.onError(e);
//                }
//            }
//        }).start();
//
//    }
//
//    public static void saveKdwPackageData(@NonNull PiePackageItemBean<KdwRespBean> kdwRespBean, @NonNull AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
//        try {
//            JsonObjectPreferenceUtil<PiePackageItemBean<KdwRespBean>> kdwPackageData = new JsonObjectPreferenceUtil<PiePackageItemBean<KdwRespBean>>(kdwRespBean, kdwRespBean.getLogisticCode()){};
//            absRxSubscriber.onNext(kdwRespBean);
//        } catch (Exception e){
//            absRxSubscriber.onError(e);
//        }
//    }
//

//
//    public static void deleteKdwPackageData(@NonNull String number, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
//        try {
//            JsonObjectPreferenceUtil<KdwRespBean> kdwPackageData = new JsonObjectPreferenceUtil<KdwRespBean>(new KdwRespBean(), number){};
//            absRxSubscriber.onNext(kdwPackageData.delete());
//        } catch (Exception e){
//            absRxSubscriber.onError(e);
//        }
//    }

    public static void queryAllKdwPackageDataList(@NonNull AbsRxSubscriber<List<PiePackageItemBean<KdwRespBean>>> absRxSubscriber){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //批量查询
                    List<PiePackageItemLocalStorageBean> localItemList = SQLite.select()
                            .from(PiePackageItemLocalStorageBean.class)
                            .queryList(FlowManager.getDatabase(AppDataBase.class));

                    List<PiePackageItemBean<KdwRespBean>> list = new ArrayList<>();
                    for(PiePackageItemLocalStorageBean localItem: localItemList){
                        PiePackageItemBean<KdwRespBean> item = new PiePackageItemBean<>();
                        item.setLocalInfoBean(localItem);
                        item.setOnlineInfoBean(GsonUtils.fromJson(localItem.getItemJson(), KdwRespBean.class));
                        list.add(item);
                    }
                    absRxSubscriber.onNext(list);
                } catch (Exception e){
                    absRxSubscriber.onError(e);
                }
            }
        }).start();

    }

    public static void queryKdwPackageData(@NonNull String logisticCode, @NonNull AbsRxSubscriber<PiePackageItemBean<KdwRespBean>> absRxSubscriber){
        try {
            //指定条件查询
            PiePackageItemLocalStorageBean localItem = SQLite.select()
                    .from(PiePackageItemLocalStorageBean.class)
                    .where(PiePackageItemLocalStorageBean_Table.logisticCode.eq(logisticCode))
                    .querySingle(FlowManager.getDatabase(AppDataBase.class));

            if(localItem != null){
                PiePackageItemBean<KdwRespBean> itemBean = new PiePackageItemBean<>();
                itemBean.setLocalInfoBean(localItem);
                if(!TextUtils.isEmpty(localItem.getItemJson())){
                    itemBean.setOnlineInfoBean(GsonUtils.fromJson(localItem.getItemJson(), KdwRespBean.class));
                }
                absRxSubscriber.onNext(itemBean);
            } else {
                absRxSubscriber.onNext(null);
            }

        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }

    public static void updateKdwPackageData(@NonNull PiePackageItemLocalStorageBean item, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
        try {
            absRxSubscriber.onNext(item.update(FlowManager.getDatabase(AppDataBase.class)));
        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }

    public static void saveKdwPackageData(@NonNull PiePackageItemLocalStorageBean item, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
        try {
            absRxSubscriber.onNext(item.save(FlowManager.getDatabase(AppDataBase.class)));
        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }

    public static void deleteKdwPackageData(@NonNull PiePackageItemLocalStorageBean item, @NonNull AbsRxSubscriber<Boolean> absRxSubscriber){
        try {
            item.delete(FlowManager.getDatabase(AppDataBase.class));
        } catch (Exception e){
            absRxSubscriber.onError(e);
        }
    }

}
