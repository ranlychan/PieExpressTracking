package com.ranlychen.pieexpresstracking.view.base

import android.os.Bundle
import android.os.Handler
import android.support.wearable.activity.WearableActivity
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseLiteBindingActivity<T : ViewDataBinding> : WearableActivity() {
    lateinit var binding: T

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
        binding = DataBindingUtil.setContentView(this, initContentView())
        initView()
    }


    override fun onResume() {
        super.onResume()
        //onResume执行后才会activity.makeVisible()渲染
        if (!isInitData) {
            handler.post {
                initResume()
            }
            isInitData = true
        }

    }

    private fun initResume() {
        initData()
    }

    /**
     * 返回布局id
     */
    abstract fun initContentView(): Int

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

}