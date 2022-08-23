package com.ranlychen.pieexpresstracking.factory;


import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.entity.KdwExpCompanyEnum;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;

public class PackageCompanyIconFactory<T> {

    private static final int DEFAULT_COM_ICON = R.mipmap.ic_launcher_foreground;

    public int getComIconResId(PiePackageItem<T> item){
        int resId = DEFAULT_COM_ICON;

        if(item == null || item.getItem() == null){
            return DEFAULT_COM_ICON;
        }

        if(item.getItem() instanceof KdwRespBean){
            try{
                resId = KdwExpCompanyEnum.valueOf(item.getCompanyCode()).getCompanyIconResId();
            } catch (Exception e){
                return DEFAULT_COM_ICON;
            }
        }
        return resId;
    }

}
