package com.ranlychen.pieexpresstracking.view.base

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.mobvoi.android.speech.SpeechRecognitionApi

abstract class BaseBindingSpeechRecogActivity<T : ViewDataBinding> : BaseLiteBindingActivity<T>(){

    val DEFAULT_REQUEST_CODE = 57

    var onRecognitionCompleteListener: OnRecognitionCompleteListener? = null

    fun SpeechRecogActivity() {

    }

    fun startRecognition() {
        this.startRecognition(null as String?)
    }

    fun startRecognition(tips: String?) {
        SpeechRecognitionApi.startRecognition(this, DEFAULT_REQUEST_CODE, tips)
    }

    fun startVoiceInput(listener: OnRecognitionCompleteListener) {
        SpeechRecognitionApi.startVoiceInput(this, DEFAULT_REQUEST_CODE)
        onRecognitionCompleteListener = listener
    }

    fun startContactSearch() {
        SpeechRecognitionApi.startContactsSearch(this, DEFAULT_REQUEST_CODE)
    }

    fun onRecognitionSuccess(result: String?) {
        onRecognitionCompleteListener?.onRecognitionSuccess(result)
    }

    fun onRecognitionFailed() {
        onRecognitionCompleteListener?.onRecognitionFailed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_REQUEST_CODE) {
            if (data != null) {
                onRecognitionSuccess(data.extras!!.getString("speech_content"))
            } else {
                onRecognitionFailed()
            }
        }
    }

    interface OnRecognitionCompleteListener {
        abstract fun onRecognitionSuccess(result: String?)
        abstract fun onRecognitionFailed()
    }
}