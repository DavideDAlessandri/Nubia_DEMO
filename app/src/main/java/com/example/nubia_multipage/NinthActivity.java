package com.example.nubia_multipage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NinthActivity extends AppCompatActivity {

    ProgressBar T1ProgressBar, T2ProgressBar, T3ProgressBar, T4ProgressBar, T5ProgressBar, T6ProgressBar;
    TextView T1ProgressText, T2ProgressText, T3ProgressText, T4ProgressText, T5ProgressText, T6ProgressText;

    Boolean running=true;
    public static Activity fa9;
    View layout;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.ninth_ly_monitor_two);

        layout=findViewById(R.id.layout9);
        T1ProgressBar=findViewById(R.id.progressBarT1);
        T2ProgressBar=findViewById(R.id.progressBarT2);
        T3ProgressBar=findViewById(R.id.progressBarT3);
        T4ProgressBar=findViewById(R.id.progressBarT4);
        T5ProgressBar=findViewById(R.id.progressBarT5);
        T6ProgressBar=findViewById(R.id.progressBarT6);
        T1ProgressText=findViewById(R.id.Temp1Txt);
        T2ProgressText=findViewById(R.id.Temp2Txt);
        T3ProgressText=findViewById(R.id.Temp3Txt);
        T4ProgressText=findViewById(R.id.Temp4Txt);
        T5ProgressText=findViewById(R.id.Temp5Txt);
        T6ProgressText=findViewById(R.id.Temp6Txt);

        fa9 = this; //assign name activity 9

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1=event.getX();
                        y1=event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        x2=event.getX();
                        y2=event.getY();
                        if(x2>x1){
                            changeActivityTen();
                        }
                        break;
                }

                return false;
            }
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();

        setFakeTemperature(); //for DEMO

        MyService.currentPage=9;

        MainActivity.screenSaverOn=false;
        MainActivity.startStatus=false;                                                             //reset start status (stop when change activity)
        running=true;
        MyService.messageToActivity="null";

        if(MyService.connectStatus){                                                                // if tcp connected
            new Thread(new NinthActivity.Thread1()).start();
        }

    }

    class Thread1 implements Runnable {                                                             //Server message reader
        @Override
        public void run() {

            if (!running) return;                                                                   //Exit thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    receiveValue(MyService.messageToActivity);

                }
            });

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private  void receiveValue(String message){

        String referenceMessage=message.substring(0,3);                                             //Identify message reference

        if(message.equals("null")) {
            new Thread(new Thread1()).start();
        }else if(message.equals("PGD02")) {
            finish();
        }else if(message.equals("PGD03")) {
            finish();
        }else if(message.equals("PGD04")) {
            finish();
        }else if(message.equals("PGD05")) {
            finish();
        }else if(message.equals("PGD06")) {
            MyService.messageToActivity = "null";
            new Thread(new Thread1()).start();
        }else if(message.equals("PGD07")) {
            finish();
        }else if(message.equals("PGD01")) {
            finish();
        }else if(referenceMessage.equals("TOD")) { //TOD

            String val1 = message.substring(21, 24);                                                  //get progressbar 1 value
            String val2 = message.substring(24, 27);                                                  //get progressbar 1 value
            String val3 = message.substring(27, 30);
            String val4 = message.substring(30, 33);
            String val5 = message.substring(33, 36);
            String val6 = message.substring(36, 39);

            int number1 = Integer.parseInt(val1);                                                   //set progressbar 1 value
            T1ProgressBar.setProgress(number1);
            String val1s = Integer.toString(number1);
            T1ProgressText.setText(val1s);
            if(number1>=66){
                T1ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            }else if(number1>=33){
                T1ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            }else if(number1>=0){
                T1ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }

            int number2 = Integer.parseInt(val2);
            T2ProgressBar.setProgress(number2);
            String val2s = Integer.toString(number2);
            T2ProgressText.setText(val2s);
            if(number2>=66){
                T2ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            }else if(number2>=33){
                T2ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            }else if(number2>=0){
                T2ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }

            int number3 = Integer.parseInt(val3);
            T3ProgressBar.setProgress(number3);
            String val3s = Integer.toString(number3);
            T3ProgressText.setText(val3s);
            if(number3>=66){
                T3ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            }else if(number3>=33){
                T3ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            }else if(number3>=0){
                T3ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }

            int number4 = Integer.parseInt(val4);
            T4ProgressBar.setProgress(number4);
            String val4s = Integer.toString(number4);
            T4ProgressText.setText(val4s);
            if(number4>=66){
                T4ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            }else if(number4>=33){
                T4ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            }else if(number4>=0){
                T4ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }

            int number5 = Integer.parseInt(val5);
            T5ProgressBar.setProgress(number5);
            String val5s = Integer.toString(number5);
            T5ProgressText.setText(val5s);
            if(number5>=66){
                T5ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            }else if(number5>=33){
                T5ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            }else if(number5>=0){
                T5ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }

            int number6 = Integer.parseInt(val6);
            T6ProgressBar.setProgress(number6);
            String val6s = Integer.toString(number6);
            T6ProgressText.setText(val6s);
            if(number6>=66){
                T6ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            }else if(number6>=33){
                T6ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            }else if(number6>=0){
                T6ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }

            MyService.messageToActivity = "null";
            new Thread(new Thread1()).start();
        }else{
            MyService.messageToActivity = "null";
            new Thread(new Thread1()).start();
        }

        }

    private void changeActivityTen() {
        Intent Intent = new Intent(this, TenthActivity.class);
        startActivity(Intent);
        finish();
    }

    private void setFakeTemperature(){
        int number1 = 29;                                                   //set progressbar 1 value
        T1ProgressBar.setProgress(number1);
        String val1s = Integer.toString(number1);
        T1ProgressText.setText(val1s);
        if(number1>=66){
            T1ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
        }else if(number1>=33){
            T1ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
        }else if(number1>=0){
            T1ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
        }

        int number2 = 32;
        T2ProgressBar.setProgress(number2);
        String val2s = Integer.toString(number2);
        T2ProgressText.setText(val2s);
        if(number2>=66){
            T2ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
        }else if(number2>=33){
            T2ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
        }else if(number2>=0){
            T2ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
        }

        int number3 = 29;
        T3ProgressBar.setProgress(number3);
        String val3s = Integer.toString(number3);
        T3ProgressText.setText(val3s);
        if(number3>=66){
            T3ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
        }else if(number3>=33){
            T3ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
        }else if(number3>=0){
            T3ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
        }

        int number4 = 40;
        T4ProgressBar.setProgress(number4);
        String val4s = Integer.toString(number4);
        T4ProgressText.setText(val4s);
        if(number4>=66){
            T4ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
        }else if(number4>=33){
            T4ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
        }else if(number4>=0){
            T4ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
        }

        int number5 = 42;
        T5ProgressBar.setProgress(number5);
        String val5s = Integer.toString(number5);
        T5ProgressText.setText(val5s);
        if(number5>=66){
            T5ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
        }else if(number5>=33){
            T5ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
        }else if(number5>=0){
            T5ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
        }

        int number6 = 49;
        T6ProgressBar.setProgress(number6);
        String val6s = Integer.toString(number6);
        T6ProgressText.setText(val6s);
        if(number6>=66){
            T6ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
        }else if(number6>=33){
            T6ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
        }else if(number6>=0){
            T6ProgressBar.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        running=false;                                                                              //Stop thread 1
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);                     //animation out
    }
}
