package com.ranlychen.pieexpresstracking.view.base;


import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;

import com.ranlychen.pieexpresstracking.view.LoadingDialog;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends RxAppCompatActivity implements LifeCycleHolder {
        private static final String TAG = "BaseActivity";
        protected LifeCycleManager mLifeCycleManager = new LifeCycleManager(this);
        protected boolean mHasStateSaved = false;
        private OnDestroyCallback mOnDestroyCallback;
        private OnConfigChangeCallback mOnConfigChangeCallback;

        public static float sMarkChannelBrightness = -1;
        public static boolean sBrightnessDynamicSwitch = true;

        private final List<Runnable> mPendingTasksOnStart = new ArrayList<>();

        private LoadingDialog loadingView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mLifeCycleManager.setMessageLifeCycle(getMessageLifeCycle());
            mLifeCycleManager.onCreate(savedInstanceState);
        }

        @Override
        public LifeCycleManager getLifecycleManager() {
            return mLifeCycleManager;
        }

        @Override
        protected void onStart() {
            super.onStart();
            mLifeCycleManager.onStart();
//        tryInitialImmersion();

            if (!mPendingTasksOnStart.isEmpty()) {
                for (Runnable task : mPendingTasksOnStart) {
                    task.run();
                }
                mPendingTasksOnStart.clear();
            }
        }

//    private void tryInitialImmersion() {
//        if (useImmersionMode()) {
//            int result = StatusBarUtil.StatusBarLightMode(this, useDarkStatusBarMode());
//            if (result == 1) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    // 小米7.0以上
//                    updateStatusBarColorForM(useDarkStatusBarMode());
//                } else {
//                    updateStatusBarColorForM(useDarkStatusBarMode());
//                    StatusBarUtil.setImmersionMode(this, useDarkStatusBarMode());
//                }
//            } else if (result == 2) {
//                // 魅族
//                StatusBarUtil.setImmersionMode(this, useDarkStatusBarMode());
//            } else {
//                updateStatusBarColorForM(useDarkStatusBarMode());
//            }
//        }
//    }
//
//    private void updateStatusBarColorForM(boolean isDark) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            final Window window = getWindow();
//            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            //设置状态栏颜色
//            window.setStatusBarColor(BaseApp.gContext.getColor(isDark ? android.R.color.black : android.R.color.white));
//
//            int ui = window.getDecorView().getSystemUiVisibility();
//            if (isDark) {
//                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            } else {
//                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            }
//
//            window.getDecorView().setSystemUiVisibility(ui);
//        }
//    }

        /**
         * 默认使用沉浸模式 若不使用 则返回false
         *
         * @return
         */
        public boolean useImmersionMode() {
            return true;
        }

        public boolean useDarkStatusBarMode() {
            return false;
        }

        /**
         * @return default return {@link LifeCycleManager#MESSAGE_LIFECYCLE_RESUME_PAUSE}
         */
        protected int getMessageLifeCycle() {
            return LifeCycleManager.MESSAGE_LIFECYCLE_RESUME_PAUSE;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            mLifeCycleManager.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        protected void onResume() {
            super.onResume();
//        if (sBrightnessDynamicSwitch && sMarkChannelBrightness != -1) {
//            //动态配置的夜间模式下 将全局亮度修改为直播间亮度
//            Window localWindow = this.getWindow();
//            if (null != localWindow) {
//                WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
//                localLayoutParams.screenBrightness = sMarkChannelBrightness;
//                localWindow.setAttributes(localLayoutParams);
//            }
//        }
            mHasStateSaved = false;
            mLifeCycleManager.onResume();
            //TODO: move to the lifecycle when decor view is really visible to user
            mLifeCycleManager.onVisibleToUser();
        }

        @Override
        protected void onPause() {
            super.onPause();
            //TODO: move to the lifecycle when decor view is really invisible to user
            mLifeCycleManager.onInVisibleToUser();
            mLifeCycleManager.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
            mLifeCycleManager.onStop();
        }

        public boolean hasStateSaved() {
            return mHasStateSaved;
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            mHasStateSaved = true;
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onBackPressed() {
            if (!mHasStateSaved) {
                super.onBackPressed();
            } else {
                finish();
            }
        }

        @Override
        protected void onPostResume() {
            super.onPostResume();
        }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState) {
            mHasStateSaved = false;
            super.onRestoreInstanceState(savedInstanceState);
        }

        protected boolean canBack() {
            return true;
        }

        @Override
        protected void onDestroy() {
//        ArkUtils.unregister(this);
            if (mOnDestroyCallback != null) {
                mOnDestroyCallback.onDestroy();
                mOnDestroyCallback = null;
            }
            if (mOnConfigChangeCallback != null) {
                mOnConfigChangeCallback = null;
            }
            super.onDestroy();
            mLifeCycleManager.onDestroy();
        }

        public boolean isActivityCreated() {
            return mLifeCycleManager.isCreated();
        }

        public boolean isActivityResumed() {
            return mLifeCycleManager.isResumed();
        }

        public boolean isActivityStarted() {
            return mLifeCycleManager.isStarted();
        }

        public boolean isActivityPaused() {
            return mLifeCycleManager.isPaused();
        }

        public boolean isActivityStopped() {
            return mLifeCycleManager.isStopped();
        }

        public boolean isActivityDestroyed() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return isDestroyed();
            }
            return mLifeCycleManager.isDestroyed();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            if (Build.VERSION.SDK_INT >= 24) {//fixbug:7.0手机加载插件资源后,getResources().getConfiguration().orientation不正确
                getResources().getConfiguration().setTo(newConfig);
            }
            if (mOnConfigChangeCallback != null) {
                mOnConfigChangeCallback.onConfigChange(newConfig);
            }
            super.onConfigurationChanged(newConfig);
        }

        @Override
        public void startActivityForResult(final Intent intent, final int requestCode) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResultSafely(intent, requestCode);
                    }
                });
                return;
            }
            startActivityForResultSafely(intent, requestCode);
        }

        public void startActivityForResultSafely(Intent intent, int requestCode) {
            try {
                super.startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                if (e instanceof ActivityNotFoundException) {
                    if (intent != null && intent.hasCategory("android.intent.category.BROWSABLE")) {
                        return;
                    }
                }
            }
        }

        @TargetApi(16)
        @Override
        public void startActivityForResult(final Intent intent, final int requestCode, final Bundle options) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResultSafely(intent, requestCode, options);
                    }
                });
                return;
            }
            startActivityForResultSafely(intent, requestCode, options);
        }

        @TargetApi(16)
        public void startActivityForResultSafely(Intent intent, int requestCode, Bundle options) {
            try {
                super.startActivityForResult(intent, requestCode, options);
            } catch (Exception e) {
                if (e instanceof ActivityNotFoundException) {
                    e.printStackTrace();
                    if (intent != null && intent.hasCategory("android.intent.category.BROWSABLE")) {
                        return;
                    }
                }
            }
        }

        @Override
        public void startActivity(Intent intent) {
            try {
                super.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void startActivityUnsafe(Intent intent) {
            super.startActivity(intent);
        }

        public int getUploadResultViewMarginTop() {
            return 0;
        }

        public void setOnDestroyCallback(OnDestroyCallback onDestroyCallback) {
            mOnDestroyCallback = onDestroyCallback;
        }

        public void clearOnDestroyCallback() {
            mOnDestroyCallback = null;
        }

        public interface OnDestroyCallback {
            void onDestroy();
        }

        public void setOnConfigChangeCallback(OnConfigChangeCallback callback) {
            this.mOnConfigChangeCallback = callback;
        }

        public void clearOnConfigChangeCallback() {
            mOnConfigChangeCallback = null;
        }

        public void scheduleTaskOnStart(Runnable task) {
            if (mLifeCycleManager.isStarted()) {
                task.run();
            } else {
                mPendingTasksOnStart.add(task);
            }
        }

        public interface OnConfigChangeCallback {
            /**
             * 屏幕旋转回调
             */
            void onConfigChange(Configuration newConfig);
        }

        public void showLoadingView() {
            if (loadingView == null) {
                loadingView = new LoadingDialog(this);
            }
            loadingView.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            if (!loadingView.isShowing()) {
                loadingView.show();
            }
        }


        public void hideLoadingView() {
            if (loadingView != null && loadingView.isShowing()) {
                loadingView.dismiss();
            }
        }
    }
