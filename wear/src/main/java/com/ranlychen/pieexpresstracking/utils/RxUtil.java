package com.ranlychen.pieexpresstracking.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxUtil {
    /**
     * 从其他线程转到主线程.
     *
     * @param scheduler Schedulers.io()等等
     * @param <T>       t
     * @return FlowableTransformer
     */
    public static <T> FlowableTransformer<T, T> toMain(Scheduler scheduler) {
        return upstream -> upstream.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> toMain() {
        return toMain(Schedulers.io());
    }


    public static class RetryWithDelay implements Function<Flowable<? extends Throwable>, Flowable<?>> {
        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
            this.retryCount = 0;
        }

        @Override
        public Flowable<?> apply(final Flowable<? extends Throwable> attempts) {
            return attempts
                    .flatMap(new Function<Throwable, Flowable<?>>() {
                        @Override
                        public Flowable<?> apply(final Throwable throwable) {
                            if (++retryCount < maxRetries) {
                                // When this Observable calls onNext, the original
                                // Observable will be retried (i.e. re-subscribed).
                                return Flowable.timer(retryDelayMillis,
                                        TimeUnit.MILLISECONDS);
                            }

                            // Max retries hit. Just pass the error along.
                            return Flowable.error(throwable);
                        }
                    });
        }
    }
}
