package com.ranlychen.pieexpresstracking.network;

import com.ranlychen.pieexpresstracking.utils.RxUtil;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public abstract class AbsRxSubscriber<T> extends ResourceSubscriber<T> {

    @Override public void onError(Throwable throwable) {

    }


    @Override public void onComplete() {

    }

    public static <T> Disposable addSubscribe(Flowable<T> flowable, AbsRxSubscriber<T> subscriber) {
        return flowable.compose(RxUtil.toMain(Schedulers.io())) //Transformer实际上就是一个Func1<Observable<T>, Observable<R>>，换言之就是：可以通过它将一种类型的Observable转换成另一种类型的Observable，和调用一系列的内联操作符是一模一样的
                .subscribeOn(Schedulers.io()) // 指定订阅发生的线程(事件产生的线程) 网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 运行在的线程(事件消费的线程) 回调处理
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }
}

