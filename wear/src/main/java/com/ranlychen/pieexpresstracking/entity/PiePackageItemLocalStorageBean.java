package com.ranlychen.pieexpresstracking.entity;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.dbflow5.annotation.Unique;
import com.dbflow5.structure.BaseModel;
import com.ranlychen.pieexpresstracking.dbflow.AppDataBase;

import java.io.Serializable;

@Table(database = AppDataBase.class)
public class PiePackageItemLocalStorageBean extends BaseModel implements Serializable {
    @Unique
    @PrimaryKey
    String logisticCode;   //包裹单号

    @Column
    String markName;       //包裹备注名称

    @Column
    String companyCode;    //包裹单号公司代码

    @Column
    String itemJson;       //包裹信息item的json

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

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

    public String getItemJson() {
        return itemJson;
    }

    public void setItemJson(String itemJson) {
        this.itemJson = itemJson;
    }
}