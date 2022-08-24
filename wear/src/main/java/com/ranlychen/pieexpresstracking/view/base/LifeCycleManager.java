package com.ranlychen.pieexpresstracking.view.base;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * 生命周期管理
 * 1. 记录生命周期
 * 2. 根据生命周期以及{@link #setMessageLifeCycle(int)}设置的值进行消息注册/反注册
 * 3. 提供数据绑定能力，绑定的数据根据生命周期进行自动解绑
 *
 * @author carlosliu on 2016/12/2.
 */
public class LifeCycleManager {
    private static final String TAG = "LifeCycleManager";

    private static final int LIFE_CYCLE_CREATED = 5;
    private static final int LIFE_CYCLE_VIEW_CREATED = 4;
    private static final int LIFE_CYCLE_STARTED = 3;
    private static final int LIFE_CYCLE_RESUMED = 2;
    private static final int LIFE_CYCLE_VISIBLE_TO_USER = 1;
    private static final int LIFE_CYCLE_INVALID = 1000;
    private static final int LIFE_CYCLE_INVISIBLE_TO_USER = -LIFE_CYCLE_VISIBLE_TO_USER;
    private static final int LIFE_CYCLE_PAUSED = -LIFE_CYCLE_RESUMED;
    private static final int LIFE_CYCLE_STOPPED = -LIFE_CYCLE_STARTED;
    private static final int LIFE_CYCLE_VIEW_DESTROYED = -LIFE_CYCLE_VIEW_CREATED;
    private static final int LIFE_CYCLE_DESTROYED = -LIFE_CYCLE_CREATED;

    /**
     * 无效的生命周期组，设置该类型，不会收到任何的生命周期对的回调
     */
    public static final int MESSAGE_LIFECYCLE_INVALID = LIFE_CYCLE_INVALID;
    /**
     * onCreate-onDestroy两个生命周期的回调
     */
    public static final int MESSAGE_LIFECYCLE_CREATE_DESTROY = LIFE_CYCLE_CREATED;
    /**
     * onStart-onStop两个生命周期的回调
     */
    public static final int MESSAGE_LIFECYCLE_START_STOP = LIFE_CYCLE_STARTED;
    /**
     * onResume-onPause两个生命周期的回调
     */
    public static final int MESSAGE_LIFECYCLE_RESUME_PAUSE = LIFE_CYCLE_RESUMED;
    /**
     * onVisibleToUser-onInVisibleToUser两个生命周期的回调
     */
    public static final int MESSAGE_LIFECYCLE_VISIBLE_INVISIBLE = LIFE_CYCLE_VISIBLE_TO_USER;
    /**
     * onViewCreated-onDestroyView两个生命周期的回调
     */
    public static final int MESSAGE_LIFECYCLE_VIEW_CREATE_DESTROY = LIFE_CYCLE_VIEW_CREATED;
    //    private final BindingManager mBindingManager = new BindingManager();
    private final Object mReceiver;
    private final List<LifeCyclePairCallback> mPairObservers = new LinkedList<>();
    private final List<LifecycleCallback> mLifeCycleObservers = new LinkedList<>();
    private int mMessageLifeCycle = MESSAGE_LIFECYCLE_INVALID;
    private int mLifeCycleStatus = LIFE_CYCLE_INVALID;

    public LifeCycleManager(Object receiver) {
        mReceiver = receiver;
    }

    private void updateLifeCycleStatus(int status) {
        mLifeCycleStatus = status;
        checkLifeCycleEvent();
        checkMessageLifecycle();
    }

    public synchronized void addLifeCyclePairObserver(LifeCyclePairCallback observer) {
        int type = observer.getPairType();
        boolean entered = false;
        if (reached(type) && before(-type)) {
            entered = true;
        }
        observer.onAdded(entered);
        mPairObservers.add(observer);
    }

    public synchronized void removeLifeCyclePairObserver(LifeCyclePairCallback observer) {
        if (observer != null) {
            observer.onRemoved();
            mPairObservers.remove(observer);
        }
    }

    public synchronized void addLifecycleObserver(LifecycleCallback observer) {
        if (observer != null) {
            mLifeCycleObservers.add(observer);
        }
    }

    public synchronized void removeLifecycleObserver(LifecycleCallback observer) {
        if (observer != null) {
            mLifeCycleObservers.remove(observer);
        }
    }

    private void checkLifeCycleEvent() {
        LifeCyclePairCallback[] observers;
        synchronized (this) {
            observers = new LifeCyclePairCallback[mPairObservers.size()];
            mPairObservers.toArray(observers);
        }
        for (LifeCyclePairCallback observer : observers) {
            checkLifeCyclePairEvent(observer);
        }
    }

    private synchronized LinkedList<LifecycleCallback> getLifeCycleCallbackListCopy() {
        return new LinkedList<>(mLifeCycleObservers);
    }

    private void checkLifeCyclePairEvent(LifeCyclePairCallback observer) {
        int type = observer.getPairType();
        if (type == MESSAGE_LIFECYCLE_INVALID) {
            return;
        }
        if (type == mLifeCycleStatus) {
            observer.onEnter();
        } else if (type == -mLifeCycleStatus) {
            observer.onLeave();
        }
    }

    /**
     * 检查消息生命周期注册
     */
    private void checkMessageLifecycle() {
        int msgLifeCycleType = mMessageLifeCycle;
        if (mMessageLifeCycle == MESSAGE_LIFECYCLE_INVALID) {
            return;
        }
        if (msgLifeCycleType == mLifeCycleStatus) {
            Log.d(TAG, "checkMessageLifecycle, register receiver:%s");
        } else if (msgLifeCycleType == -mLifeCycleStatus) {
            Log.d(TAG, "checkMessageLifecycle, unregister receiver:%s");
        }
    }

    public void setMessageLifeCycle(int messageLifeCycle) {
        mMessageLifeCycle = messageLifeCycle;
    }

    public void onCreate(Bundle savedInstanceState) {
        updateLifeCycleStatus(LIFE_CYCLE_CREATED);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onCreate(savedInstanceState);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onStart() {
        updateLifeCycleStatus(LIFE_CYCLE_STARTED);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onStart();
        }
    }

    public void onResume() {
        updateLifeCycleStatus(LIFE_CYCLE_RESUMED);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onResume();
        }
    }

    public void onVisibleToUser() {
        updateLifeCycleStatus(LIFE_CYCLE_VISIBLE_TO_USER);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onVisibleToUser();
        }
    }

    public void onInVisibleToUser() {
        updateLifeCycleStatus(LIFE_CYCLE_INVISIBLE_TO_USER);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onInVisibleToUser();
        }
    }

    public void onPause() {
        updateLifeCycleStatus(LIFE_CYCLE_PAUSED);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onPause();
        }
    }

    public void onStop() {
        updateLifeCycleStatus(LIFE_CYCLE_STOPPED);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onStop();
        }
    }

    public void onDestroy() {
        updateLifeCycleStatus(LIFE_CYCLE_DESTROYED);
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onDestroy();
        }
        mLifeCycleObservers.clear();
//        ThreadUtils.runOnMainThread(new Runnable() {
//            @Override
//            public void run() {
//                autoRelease();
//            }
//        });
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        updateLifeCycleStatus(LIFE_CYCLE_VIEW_CREATED);
    }

    public void onDestroyView() {
        updateLifeCycleStatus(LIFE_CYCLE_VIEW_DESTROYED);
    }

    public boolean isVisibleToUser() {
        return reached(LIFE_CYCLE_VISIBLE_TO_USER) && before(LIFE_CYCLE_INVISIBLE_TO_USER);
    }

    public boolean isViewCreated() {
        return reached(LIFE_CYCLE_VIEW_CREATED) && before(LIFE_CYCLE_VIEW_DESTROYED);
    }

    public boolean isCreated() {
        return reached(LIFE_CYCLE_CREATED) && before(LIFE_CYCLE_DESTROYED);
    }

    public boolean isResumed() {
        return reached(LIFE_CYCLE_RESUMED) && before(LIFE_CYCLE_PAUSED);
    }

    public boolean isStarted() {
        return reached(LIFE_CYCLE_STARTED) && before(LIFE_CYCLE_STOPPED);
    }

    public boolean isInvisibleToUser() {
        return reached(LIFE_CYCLE_INVISIBLE_TO_USER);
    }

    public boolean isPaused() {
        return reached(LIFE_CYCLE_PAUSED);
    }

    public boolean isStopped() {
        return reached(LIFE_CYCLE_STOPPED);
    }

    public boolean isDestroyed() {
        return reached(LIFE_CYCLE_DESTROYED);
    }

    public boolean isViewDestroyed() {
        return reached(LIFE_CYCLE_VIEW_DESTROYED);
    }

    private boolean before(int status) {
        int index = mLifeCycleStatus;
        return index > status;
    }

    private boolean reached(int status) {
        int index = mLifeCycleStatus;
        return index <= status;
    }

//    public <T, O> void bind(@NonNull final T target, @NonNull DependencyProperty.Entity<O> entity, @NonNull final ViewBinder<? super T, ? super O> viewBinder) {
//        bind(target, entity, new LiveDataObserver<O>() {
//            @Override
//            public void onPropChange(O value) {
//                viewBinder.bindView(target, value);
//            }
//
//            @Override
//            public Looper getDeliverLooper() {
//                return viewBinder.getDeliverLooper();
//            }
//        });
//    }
//
//    public <T, O> void bind(@NonNull T target, @NonNull DependencyProperty.Entity<O> entity, @NonNull LiveDataObserver<O> observer) {
//        LiveDataObserver<O> last = (LiveDataObserver<O>) mBindingManager.getBindingObserver(target, entity);
//        if (last != null) {
//            unbind(target, entity);
//        }
//        addLifeCyclePairObserver(observer);
//        mBindingManager.bind(target, entity, observer);
//    }
//
//    public <T, O> void unbind(@NonNull T target, @NonNull DependencyProperty.Entity<O> entity) {
//        removeLifeCyclePairObserver((LifeCyclePairCallback) mBindingManager.unbind(target, entity));
//    }
//
//    private void autoRelease() {
//        Collection<DependencyProperty.Observer> observers = mBindingManager.unbindAll();
//        for (DependencyProperty.Observer observer : observers) {
//            removeLifeCyclePairObserver((LifeCyclePairCallback) observer);
//        }
//    }

    public void onSaveInstanceState(Bundle state) {
        for (LifecycleCallback callback : getLifeCycleCallbackListCopy()) {
            callback.onSaveInstanceState(state);
        }
    }

    /**
     * @author carlosliu on 2019/1/3.
     */
    public interface LifecycleCallback {
        void onCreate(Bundle bundle);

        void onViewCreated(View view, Bundle savedInstanceState);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onStart();

        void onResume();

        void onVisibleToUser();

        void onInVisibleToUser();

        void onPause();

        void onStop();

        void onDestroyView();

        void onDestroy();

        void onSaveInstanceState(@Nullable Bundle state);

    }

    public static class LifecycleCallbackAdapter implements LifecycleCallback {

        @Override
        public void onCreate(Bundle bundle) {

        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onVisibleToUser() {

        }

        @Override
        public void onInVisibleToUser() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroyView() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void onSaveInstanceState(@Nullable Bundle state) {

        }
    }

    /**
     * 监听指定的(通过{@link #getPairType()}指定)生命周期范围，当进入到指定的生命周期范围时，回调{@link #onEnter()}，当退出指定的范围时，回调{@link #onLeave()}
     */
    public interface LifeCyclePairCallback {
        /**
         * 监听添加成功的回调
         *
         * @param entered 添加时是否已经进入指定的生命周期
         */
        void onAdded(boolean entered);

        /**
         * 监听移除成功
         */
        void onRemoved();

        /**
         * 当进入到指定的生命周期范围时回调
         */
        void onEnter();

        /**
         * 当退出指定的生命周期范围时回调
         */
        void onLeave();

        /**
         * @see LifeCycleManager#MESSAGE_LIFECYCLE_INVALID
         * @see LifeCycleManager#MESSAGE_LIFECYCLE_CREATE_DESTROY
         * @see LifeCycleManager#MESSAGE_LIFECYCLE_START_STOP
         * @see LifeCycleManager#MESSAGE_LIFECYCLE_RESUME_PAUSE
         * @see LifeCycleManager#MESSAGE_LIFECYCLE_VIEW_CREATE_DESTROY
         * @see LifeCycleManager#MESSAGE_LIFECYCLE_VISIBLE_INVISIBLE
         */
        int getPairType();
    }
}
