package com.ranlychen.pieexpresstracking.network;

import android.util.Log;

import org.reactivestreams.Subscription;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxCallAdapter extends CallAdapter.Factory {
    private static String TAG = "RxCallAdapter";

    private CallAdapter.Factory mFactory = RxJava2CallAdapterFactory.create();

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        final CallAdapter adapter = mFactory.get(returnType, annotations, retrofit);
        if (adapter == null) {
            return null;
        }
        return new CallAdapter<Object, Object>() {
            @Override
            public Type responseType() {
                return adapter.responseType();
            }

            @Override
            public Object adapt(Call<Object> call) {
                Object adapt = adapter.adapt(call);
                final Request request = call.request();
                if (adapt instanceof Observable) {
                    return ((Observable<?>) adapt)
                            .doOnSubscribe(new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    Log.i(TAG, "request url="+request.url().toString());
                                }
                            })
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    handleError(request, throwable);
                                }
                            });
                } else if (adapt instanceof Flowable) {
                    return ((Flowable<?>) adapt)
                            .doOnSubscribe(new Consumer<Subscription>() {
                                @Override
                                public void accept(Subscription subscription) throws Exception {
                                    Log.i(TAG, "request url="+request.url().toString());
                                }
                            })
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    handleError(request, throwable);
                                }
                            });
                }

                return adapter;
            }
        };
    }

    private void handleError(Request request, Throwable throwable) {
        if  (throwable instanceof IOException) {
            Log.e(TAG, "request error, url=%s, exp=" + request.url().toString(), throwable);
        } else {
            Log.e(TAG, "request error, url=" + request.url().toString(), throwable);
        }
    }
}
