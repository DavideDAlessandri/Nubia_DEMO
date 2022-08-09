package com.example.nubia_multipage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EightActivity extends AppCompatActivity {

    Boolean running=true;
    public static Activity fa8;

    SeekBar seekBarForce, seekBarSpeed, seekBarPos;
    Button openButton, closeButton, goButton;
    TextView forceTxt, speedTxt, posTxt, curPosTxt, statusTxt;
    ImageView gripperL1, gripperL2, gripperL3, gripperL4, gripperR1, gripperR2, gripperR3, gripperR4, objectIn, objectOutL, objectOutR;
    Integer progressForceVal, progressSpeedVal,progressPosVal;

    int fakeGripperPos=0;
    Boolean fakeGripperBool=true;
    int fakeObject=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.eight_ly_gripper);

        fa8 = this; //assign name activity 8

        seekBarForce=findViewById(R.id.seekBarForce);
        seekBarSpeed=findViewById(R.id.seekBarSpeed);
        seekBarPos=findViewById(R.id.seekBarPos);
        openButton=findViewById(R.id.openButton);
        closeButton=findViewById(R.id.closeButton);
        goButton=findViewById(R.id.goButton);
        forceTxt=findViewById(R.id.gripperForce);
        speedTxt=findViewById(R.id.gripperSpeed);
        posTxt=findViewById(R.id.gripperPos);
        curPosTxt=findViewById(R.id.gripperCursPos);
        statusTxt=findViewById(R.id.gripperStatus);

        gripperL1=findViewById(R.id.gripperL1);
        gripperL2=findViewById(R.id.gripperL2);
        gripperL3=findViewById(R.id.gripperL3);
        gripperL4=findViewById(R.id.gripperL4);
        gripperR1=findViewById(R.id.gripperR1);
        gripperR2=findViewById(R.id.gripperR2);
        gripperR3=findViewById(R.id.gripperR3);
        gripperR4=findViewById(R.id.gripperR4);
        objectIn=findViewById(R.id.objectIn);
        objectOutL=findViewById(R.id.objectOutL);
        objectOutR=findViewById(R.id.objectOutR);

        gripperL1.setVisibility(View.GONE);
        gripperL3.setVisibility(View.GONE);
        gripperL4.setVisibility(View.GONE);
        gripperR1.setVisibility(View.GONE);
        gripperR3.setVisibility(View.GONE);
        gripperR4.setVisibility(View.GONE);
        objectIn.setVisibility(View.GONE);
        objectOutL.setVisibility(View.GONE);
        objectOutR.setVisibility(View.GONE);

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gripperAnimation();
                new Thread(new EightActivity.Thread2("OPR")).start();
            }

        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gripperAnimation();
                new Thread(new EightActivity.Thread2("CLR")).start();
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for DEMO:
                if(fakeObject<3){
                    fakeObject++;
                }else{
                    fakeObject=1;
                }

                new Thread(new EightActivity.Thread2("GOR")).start();
            }
        });

        seekBarForce.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getProgressForce();                                                                   //get new value
                String progressForceString =progressForceVal.toString();                                   //convert int to string
                forceTxt.setText(progressForceString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                getProgressForce();                                                                   //get new value
                String progressForceString =progressForceVal.toString();                                 //convert int to string
                if(progressForceVal>=10 && progressForceVal<100){                                              //if<100 send 0ss
                    new Thread(new EightActivity.Thread2("FOR0"+progressForceString)).start();        // send value to server
                }else if(progressForceVal<10){
                    new Thread(new EightActivity.Thread2("FOR00"+progressForceString)).start();       // send value to server
                }else{
                    new Thread(new EightActivity.Thread2("FOR"+progressForceString)).start();
                }

            }
        });

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getProgressSpeed();                                                                   //get new value
                String progressSpeedString =progressSpeedVal.toString();                                   //convert int to string
                speedTxt.setText(progressSpeedString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                getProgressSpeed();                                                                   //get new value
                String progressSpeedString =progressSpeedVal.toString();                                //convert int to string
                if(progressSpeedVal>=10 && progressSpeedVal<100){                                              //if<100 send 0ss
                    new Thread(new EightActivity.Thread2("SPR0"+progressSpeedString)).start();        // send value to server
                }else if(progressSpeedVal<10){
                    new Thread(new EightActivity.Thread2("SPR00"+progressSpeedString)).start();       // send value to server
                }else{
                    new Thread(new EightActivity.Thread2("SPR"+progressSpeedString)).start();
                }
            }
        });

        seekBarPos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getProgressPos();                                                                   //get new value
                String progressPosString =progressPosVal.toString();                                   //convert int to string
                posTxt.setText(progressPosString);
                objectOutL.setVisibility(View.GONE);                                                //remove obj. detected
                objectOutR.setVisibility(View.GONE);
                objectIn.setVisibility(View.GONE);

                if(progressPosVal<25){
                    gripperL1.setVisibility(View.VISIBLE);
                    gripperR1.setVisibility(View.VISIBLE);
                    gripperR2.setVisibility(View.GONE);
                    gripperL2.setVisibility(View.GONE);
                    gripperR3.setVisibility(View.GONE);
                    gripperL3.setVisibility(View.GONE);
                    gripperR4.setVisibility(View.GONE);
                    gripperL4.setVisibility(View.GONE);
                }else if(progressPosVal>25 && progressPosVal<50){
                    gripperL2.setVisibility(View.VISIBLE);
                    gripperR2.setVisibility(View.VISIBLE);
                    gripperR1.setVisibility(View.GONE);
                    gripperL1.setVisibility(View.GONE);
                    gripperR3.setVisibility(View.GONE);
                    gripperL3.setVisibility(View.GONE);
                    gripperR4.setVisibility(View.GONE);
                    gripperL4.setVisibility(View.GONE);
                }else if(progressPosVal>50 && progressPosVal<80){
                    gripperL3.setVisibility(View.VISIBLE);
                    gripperR3.setVisibility(View.VISIBLE);
                    gripperR2.setVisibility(View.GONE);
                    gripperL2.setVisibility(View.GONE);
                    gripperR4.setVisibility(View.GONE);
                    gripperL4.setVisibility(View.GONE);
                    gripperR1.setVisibility(View.GONE);
                    gripperL1.setVisibility(View.GONE);
                }else if(progressPosVal>80){
                    gripperL4.setVisibility(View.VISIBLE);
                    gripperR4.setVisibility(View.VISIBLE);
                    gripperR3.setVisibility(View.GONE);
                    gripperL3.setVisibility(View.GONE);
                    gripperR1.setVisibility(View.GONE);
                    gripperL1.setVisibility(View.GONE);
                    gripperR2.setVisibility(View.GONE);
                    gripperL2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                getProgressPos();                                                                   //get new value
                String progressPosString =progressPosVal.toString();                                 //convert int to string
                if(progressPosVal>=10 && progressPosVal<100){                                              //if<100 send 0ss
                    new Thread(new EightActivity.Thread2("POR0"+progressPosString)).start();        // send value to server
                }else if(progressPosVal<10){
                    new Thread(new EightActivity.Thread2("POR00"+progressPosString)).start();       // send value to server
                }else{
                    new Thread(new EightActivity.Thread2("POR"+progressPosString)).start();
                }
            }
        });
    }

    @Override
    protected  void onResume(){
        super.onResume();

        MyService.currentPage=8;

        MainActivity.screenSaverOn=false;
        MainActivity.startStatus=false;                                                             //reset start status (stop when change activity)
        running=true;
        MyService.messageToActivity="null";

        if(MyService.connectStatus){                                                                //If tcp connected
            new Thread(new EightActivity.Thread2("PGR08")).start();
            new Thread(new EightActivity.Thread1()).start();
        }
    }

    class Thread1 implements Runnable {                             //Server message reader
        @Override
        public void run() {

            if (!running) return;                                   //Exit thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    receiveValue(MyService.messageToActivity);

                }
            });

        }
    }

    class Thread2 implements Runnable {                             //Phone message reader / sender
        private String message;
        Thread2(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            //removed for DEMO
            //MyService.output.write(message);
            //MyService.output.flush();
        }
    }

    private  void receiveValue(String message) {

        if (MainActivity.startStatus) {                                                               //if another activity is called stop this activity
            finish();
        }

        String referenceMessage = message.substring(0, 3);

        if (message.equals("null")) {
            new Thread(new EightActivity.Thread1()).start();
        } else if (message.equals("PGD02")) {
            finish();
        } else if (message.equals("PGD03")) {
            finish();
        } else if (message.equals("PGD05")) {
            finish();
            new Thread(new EightActivity.Thread1()).start();
        } else if (message.equals("PGD04")) {
            finish();
        } else if (message.equals("PGD06")) {
            finish();
        } else if (message.equals("PGD08")) {
            MyService.messageToActivity = "null";
        } else if (message.equals("PGD01")) {
            finish();
        } else if (referenceMessage.equals("CPD")) {

            String val1 = message.substring(3, 6);
            int number1 = Integer.parseInt(val1);
            String val1s = Integer.toString(number1);
            curPosTxt.setText(val1s);
            seekBarPos.setProgress(number1);

            MyService.messageToActivity = "null";
            new Thread(new EightActivity.Thread1()).start();

        } else if (referenceMessage.equals("VLD")) {

            String val1 = message.substring(3, 6);             //get progressbar 1 value
            String val2 = message.substring(6, 9);             //get progressbar 2 value
            String val3 = message.substring(9, 12);

            int number1 = Integer.parseInt(val1);
            seekBarForce.setProgress(number1);

            int number2 = Integer.parseInt(val2);
            seekBarSpeed.setProgress(number2);

            int number3 = Integer.parseInt(val3);
            seekBarPos.setProgress(number3);

            MyService.messageToActivity = "null";
            new Thread(new EightActivity.Thread1()).start();

        } else if (referenceMessage.equals("STD")) {

            String val1 = message.substring(3, 8);
            String val2 = message.substring(8, 11);

            int number2 = Integer.parseInt(val2);
            String val2s = Integer.toString(number2);
            curPosTxt.setText(val2s);

            if(val1.equals("inpos")){
                statusTxt.setText("In Pos.");
                //seekBarPos.setProgress(number2);
                objectIn.setVisibility(View.GONE);
                objectOutL.setVisibility(View.GONE);
                objectOutR.setVisibility(View.GONE);


            }else if(val1.equals("inmov")){
                statusTxt.setText("In motion");
                seekBarPos.setProgress(number2);

            }else if(val1.equals("objin")){
                statusTxt.setText("Object det.");

                gripperL2.setVisibility(View.VISIBLE);  //set object in graphic
                gripperR2.setVisibility(View.VISIBLE);
                gripperR1.setVisibility(View.GONE);
                gripperL1.setVisibility(View.GONE);
                gripperR3.setVisibility(View.GONE);
                gripperL3.setVisibility(View.GONE);
                gripperR4.setVisibility(View.GONE);
                gripperL4.setVisibility(View.GONE);

                objectIn.setVisibility(View.VISIBLE);
                objectOutL.setVisibility(View.GONE);
                objectOutR.setVisibility(View.GONE);


            }else if(val1.equals("objou")){
                statusTxt.setText("Object det.");

                gripperL1.setVisibility(View.VISIBLE);  //set object out graphic
                gripperR1.setVisibility(View.VISIBLE);
                gripperR2.setVisibility(View.GONE);
                gripperL2.setVisibility(View.GONE);
                gripperR3.setVisibility(View.GONE);
                gripperL3.setVisibility(View.GONE);
                gripperR4.setVisibility(View.GONE);
                gripperL4.setVisibility(View.GONE);

                objectOutL.setVisibility(View.VISIBLE);
                objectOutR.setVisibility(View.VISIBLE);
                objectIn.setVisibility(View.GONE);

            }

            MyService.messageToActivity = "null";
            new Thread(new EightActivity.Thread1()).start();

        }else{
            MyService.messageToActivity = "null";
            new Thread(new EightActivity.Thread1()).start();

        }
    }


    private void getProgressForce(){
        progressForceVal=Integer.valueOf(seekBarForce.getProgress());
    }

    private void getProgressSpeed(){
        progressSpeedVal=Integer.valueOf(seekBarSpeed.getProgress());
    }

    private void getProgressPos(){
        progressPosVal=Integer.valueOf(seekBarPos.getProgress());
    }

    private void gripperAnimation() {

        Handler handler = new Handler();                                                    //Start thread 1 after 1 second
        handler.postDelayed(new Runnable() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void run() {

                switch (fakeObject){
                    case 1:
                        if (fakeGripperPos < 99 && fakeGripperBool) {
                            fakeGripperPos = fakeGripperPos + 1;
                        } else {
                            fakeGripperBool = false;
                        }
                        if (fakeGripperPos > 1 && !fakeGripperBool) {
                            fakeGripperPos = fakeGripperPos - 1;
                        } else {
                            fakeGripperBool = true;
                        }

                        int number1 = fakeGripperPos;
                        String val1s = Integer.toString(number1);
                        curPosTxt.setText(val1s);
                        seekBarPos.setProgress(number1);

                        if(fakeGripperPos>1 && fakeGripperPos<99){
                            statusTxt.setText("In motion");
                            gripperAnimation();
                        }else{
                            statusTxt.setText("In pos.");
                        }
                        break;
                    case 2:
                        fakeObject++;
                        receiveValue("STDobjin060");
                        break;
                    case 3:
                        fakeObject=1;
                        receiveValue("STDobjou030");
                        break;

                }






            }
        }, 15);

    }

    @Override
    protected void onStop() {

        super.onStop();
        running=false;
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);                     //animation out
    }
}
