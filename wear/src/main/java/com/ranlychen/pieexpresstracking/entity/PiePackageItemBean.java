package com.ranlychen.pieexpresstracking.entity;


public class PiePackageItemBean<T> {
    private PiePackageItemLocalStorageBean localInfoBean;  //包裹本地信息item

    private T onlineInfoBean;                              //包裹信息item

    public PiePackageItemLocalStorageBean getLocalInfoBean() {
        return localInfoBean;
    }

    public void setLocalInfoBean(PiePackageItemLocalStorageBean localInfoBean) {
        this.localInfoBean = localInfoBean;
    }

    public T getOnlineInfoBean() {
        return onlineInfoBean;
    }

    public void setOnlineInfoBean(T onlineInfoBean) {
        this.onlineInfoBean = onlineInfoBean;
    }
}


