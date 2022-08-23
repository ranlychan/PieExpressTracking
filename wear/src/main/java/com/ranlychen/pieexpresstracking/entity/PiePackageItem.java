package com.ranlychen.pieexpresstracking.entity;


public class PiePackageItem<T> {
    private String markName;       //包裹备注名称
    private String companyCode;    //包裹单号公司代码
    private String logisticCode;   //包裹单号
    private T item;                //包裹信息item

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
