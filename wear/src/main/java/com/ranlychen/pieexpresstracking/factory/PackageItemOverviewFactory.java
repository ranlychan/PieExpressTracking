package com.ranlychen.pieexpresstracking.factory;


import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.KdwTraceBean;

import java.util.List;

public class PackageItemOverviewFactory<T> {
    private static final String BREAKER = "\n";

    public String getPackageItemOverview(T item){
        String overview = "";
        if(item == null ){
            return overview;
        }
        if(item instanceof KdwRespBean){
            List<KdwTraceBean> traceBeanList = ((KdwRespBean) item).getData();
            if(traceBeanList != null && !traceBeanList.isEmpty()){
                KdwTraceBean traceBean = traceBeanList.get(0);
                overview = traceBean.getTime() + BREAKER + traceBean.getContext();
                return overview;
            }
        }
        return overview;
    }
}
