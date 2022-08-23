package com.ranlychen.pieexpresstracking;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobvoi.android.speech.SpeechRecognitionApi;

public class BaseSpeechActivity extends SpeechRecognitionApi.SpeechRecogActivity{

    @Override
    public void onRecognitionSuccess(String s) {

    }

    @Override
    public void onRecognitionFailed() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);
        bindView();
        onViewBound();
        return view;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    protected void bindView(){

    }

    protected void onViewBound(){

    }

    protected void initData(){

    }
}
