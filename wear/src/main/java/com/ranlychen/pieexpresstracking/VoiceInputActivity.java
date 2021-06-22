package com.ranlychen.pieexpresstracking;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.mobvoi.android.speech.SpeechRecognitionApi;

//ticwear的maven库失效了，这里会飘红，语音输入模块用不了了
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