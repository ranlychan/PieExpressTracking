package com.ranlychen.pieexpresstracking.view;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mobvoi.android.speech.SpeechRecognitionApi;

public class VoiceInputActivity extends SpeechRecognitionApi.SpeechRecogActivity {
    EditText editText;


    public VoiceInputActivity(EditText editText){
        this.editText = editText;
    }
    public void startInput(){
        this.setVisible(false);
        startVoiceInput();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRecognitionSuccess(String text) {
        editText.setText(text);
        finish();
    }

    @Override
    public void onRecognitionFailed() {
        //failed
        finish();
    }
}