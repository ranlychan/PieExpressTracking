package com.ranlychen.pieexpresstracking.view.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ranlychen.pieexpresstracking.view.LoadingDialog


abstract class BaseBindingActivity<T : ViewDataBinding> : BaseActivity() {
    lateinit var binding: T
    private lateinit var viewModel: ViewModel
    val loadingDialog by lazy { LoadingDialog(this) }
    val handler by lazy { Handler() }
    //是否初始化initData
    var isInitData = false
    val TAG = "BaseBindingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            initCreate(savedInstanceState)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG,"initCreate failed",e)
        }
    }

    private fun initCreate(savedInstanceState: Bundle?) {
        setStatusMode()
        binding = DataBindingUtil.setContentView(this, initContentView())
        binding.lifecycleOwner = this
        initView()
    }


    override fun onResume() {
        super.onResume()
        //onResume执行后才会activity.makeVisible()渲染
        if (!isInitData) {
            handler.post {
                try {
                    initResume()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "initResume failed", e)
                }
            }
            isInitData = true
        }

    }

    private fun initResume() {
        initData()
    }

    /**
     * 是否展示加载框
     */
    fun showLoading(show: Boolean) {
        if (show) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }


    /**
     * 返回布局id
     */
    abstract fun initContentView(): Int

    /**
     * 返回ViewMode
     */
    open fun initViewModel(): ViewModel? {
        return null
    }


    /**
     * 设置状态栏字体颜色默认黑色
     */
    open fun setStatusMode() {
        setFullScreenBlack()
    }

    /**
     * 初始化view
     */
    abstract fun initView()

    /**
     * 初始化data
     */
    abstract fun initData()


    override fun onDestroy() {
        super.onDestroy()
        //防止内存泄漏
        handler.removeCallbacksAndMessages(null)
    }


    /**
     * 设置全屏Android 6.0 以上设置 黑色字体
     */
    fun Activity.setFullScreenBlack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.TRANSPARENT
        }
    }
    

    open fun doneEvent(): String? {
        return null
    }
}