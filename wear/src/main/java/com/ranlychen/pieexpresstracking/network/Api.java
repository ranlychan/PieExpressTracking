package com.ranlychen.pieexpresstracking.network;

import com.ranlychen.pieexpresstracking.entity.KdwRespBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface Api {

    /**
     * id=[]
     * com=[]
     * nu=[]
     * show=[0|1]
     * muti=[0|1]
     * order=[desc|asc]
     */
    @GET("openapi.html?")
    Flowable<KdwRespBean> getItemInfo(@QueryMap Map<String, Object> params);
}
