package com.ranlychen.pieexpresstracking.network

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitHolder {

    private const val APP_BASE_URL = "https://ranlychan.top/api/"
    private const val APP_BASE_URL_KD = "http://api.kuaidi.com/"

    //单例retrofit
    val kdwRetrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        createRetrofit(APP_BASE_URL_KD)
    }

    val mRetrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        createRetrofit(APP_BASE_URL)
    }


    private fun createRetrofit(baseUrl: String): Retrofit {

        //拦截器添加公共参数暂时不加
        var httpClientBuilder = OkHttpClient()
            .newBuilder()

        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder()
            .client(httpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
//            //RxJava转换器
//            .addCallAdapterFactory(RxCallAdapter())
            .baseUrl(baseUrl)
            .build()
        return retrofit


    }

}