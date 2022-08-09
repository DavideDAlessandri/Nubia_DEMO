package com.example.nubia_multipage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class TenthActivity extends AppCompatActivity {

    Boolean running=true;
    public static Activity fa10;
    View layout;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.tenth_ly_monitor_three);

        layout=findViewById(R.id.layout10);

        fa10 = this; //assign name activity 10

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
                            changeActivitySix();
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

        MyService.currentPage=10;

        MainActivity.screenSaverOn=false;
        MainActivity.startStatus=false;                                                             //reset start status (stop when change activity)
        running=true;
        MyService.messageToActivity="null";

        if(MyService.connectStatus){                                                                // if tcp connected
            new Thread(new TenthActivity.Thread1()).start();
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

    private  void receiveValue(String message) {

        String referenceMessage = message.substring(0, 3);

        if(message.equals("null")) {
            new Thread(new TenthActivity.Thread1()).start();
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
            new Thread(new TenthActivity.Thread1()).start();
        }else if(message.equals("PGD07")) {
            finish();
        }else if(message.equals("PGD01")) {
            finish();
        }else if(referenceMessage.equals("TBD")) {
            //to-do

            MyService.messageToActivity = "null";
            new Thread(new TenthActivity.Thread1()).start();
        }else{
            MyService.messageToActivity = "null";
            new Thread(new TenthActivity.Thread1()).start();
        }

        }

        private void changeActivitySix() {
        Intent Intent = new Intent(this, SixthActivity.class);
        startActivity(Intent);
        finish();
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
