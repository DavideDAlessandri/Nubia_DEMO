package com.example.nubia_multipage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    ImageView color, menu, label, proximity;
    FrameLayout layout;
    Boolean running = true;
    Boolean menuStatus = true;        //if menu status true => change color, if false => change numbers
    TextView step1, step2, etf;
    ProgressBar progressBarProximity;
    public static Activity fa2;
    int fakeProximity = 20;
    boolean fakeProximityBool = true;
    Button switchColorButton;
    int noButtonPress=0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.second_ly_run);

        fa2 = this; //assign name activity 2

        color = findViewById(R.id.color);
        menu = findViewById(R.id.menu);
        label = findViewById(R.id.label);
        proximity = findViewById(R.id.proximity);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        etf = findViewById(R.id.etf);
        progressBarProximity = findViewById(R.id.progressBarProximity);
        switchColorButton=findViewById(R.id.switchColorButton);

        layout = findViewById(R.id.layout2);

        switchColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noButtonPress<4){
                    noButtonPress++;
                }else{
                    noButtonPress=0;
                }
                switch (noButtonPress){
                    case 1:
                        changeColor("TXTr1screwing error");
                        progressBarProximity.setVisibility(View.GONE);
                        break;
                    case 2:
                        changeColor("TXTr2emergency stop");
                        progressBarProximity.setVisibility(View.GONE);
                        break;
                    case 3:
                        changeColor("TXTy1collision detected");
                        progressBarProximity.setVisibility(View.GONE);
                        break;
                    case 4:
                        changeColor("TXTg1ready");
                        progressBarProximity.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        MyService.currentPage = 2;
        randomProximity();

        MainActivity.screenSaverOn = false;
        MainActivity.startStatus = false;                                                             //reset start status (stop when change activity)
        running = true;
        changeColor(MyService.pg2Message);                                                          //resume the page on the last status

        if (MyService.connectStatus) {                                                                //If tcp connected
            new Thread(new SecondActivity.Thread2("PGR02")).start();                                // send current page name to server
        }
    }


    class Thread1 implements Runnable {
        @Override
        public void run() {

            if (!running) return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    changeColor(MyService.messageToActivity);

                }
            });

        }
    }

    class Thread2 implements Runnable {                                                             //Phone message reader / sender
        private String message;

        Thread2(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            MyService.output.write(message);
            MyService.output.flush();
        }
    }


    private void changeColor(String message) {

        TextView txtMarquee;                                                                        //Sliding text
        txtMarquee = findViewById(R.id.marqueeText);

        //Import images
        int redColor = getResources().getIdentifier("@drawable/statusbar_red", null, this.getPackageName());
        int greenColor = getResources().getIdentifier("@drawable/statusbar_green", null, this.getPackageName());
        int yellowColor = getResources().getIdentifier("@drawable/statusbar_yellow", null, this.getPackageName());
        int greenMenu = getResources().getIdentifier("@drawable/runtime_bg", null, this.getPackageName());
        int greenLabel = getResources().getIdentifier("@drawable/runtime_green", null, this.getPackageName());
        int greenProximity = getResources().getIdentifier("@drawable/runtime_bar", null, this.getPackageName());
        int redLabel = getResources().getIdentifier("@drawable/runtime_red", null, this.getPackageName());
        int yellowLabel = getResources().getIdentifier("@drawable/runtime_yellow", null, this.getPackageName());

        if (MainActivity.startStatus) {                                                               //if another activity is called stop this activity
            finish();
        }

        String referenceMessage = message.substring(0, 3);                                             //Identify message reference

        if (message.equals("PGD02")) {
            MyService.messageToActivity = "null";
            new Thread(new SecondActivity.Thread1()).start();
        } else if (message.equals("PGD03")) {
            finish();
        } else if (message.equals("PGD04")) {
            finish();
        } else if (message.equals("PGD05")) {
            finish();
        } else if (message.equals("PGD06")) {
            finish();
        } else if (message.equals("PGD07")) {
            finish();
        } else if (message.equals("null")) {
            new Thread(new SecondActivity.Thread1()).start();
        } else if (referenceMessage.equals("TXT")) {

            MyService.pg2Message = message;                                                           //Save old message in case of reentering the page

            String screenMessage = message.substring(5);                                              //Subtract initial value of message to find color and option (Ex: G1)
            String colorMessage = message.substring(3, 5);

            if (colorMessage.equals("r2")) {
                color.setImageResource(redColor);
                menu.setImageResource(0);
                proximity.setImageResource(0);
                label.setImageResource(redLabel);
                txtMarquee.setText(screenMessage + " " + screenMessage + " " + screenMessage + " ");
                txtMarquee.setSelected(true);

                step1.setText(null);                                                                    //remove menu values
                step2.setText(null);
                etf.setText(null);
                progressBarProximity.setProgress(0);

                Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
                animation.setDuration(1000); //1 second duration for each animation cycle
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
                animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
                color.startAnimation(animation); //to start animation

            }
            if (colorMessage.equals("r1")) {
                color.setImageResource(redColor);
                menu.setImageResource(0);
                proximity.setImageResource(0);
                label.setImageResource(redLabel);
                txtMarquee.setText(screenMessage + " " + screenMessage + " " + screenMessage + " ");
                txtMarquee.setSelected(true);
                color.clearAnimation();
                step1.setText(null);                                                                    //remove menu values
                step2.setText(null);
                etf.setText(null);
                progressBarProximity.setProgress(0);
            }

            if (colorMessage.equals("g1")) {
                color.setImageResource(greenColor);
                menu.setImageResource(greenMenu);
                proximity.setImageResource(greenProximity);
                label.setImageResource(greenLabel);
                txtMarquee.setText(screenMessage + " " + screenMessage + " " + screenMessage + " ");
                txtMarquee.setSelected(true);
                color.clearAnimation();
            }

            if (colorMessage.equals("y1")) {
                color.setImageResource(yellowColor);
                menu.setImageResource(0);
                proximity.setImageResource(0);
                label.setImageResource(yellowLabel);
                txtMarquee.setText(screenMessage + " " + screenMessage + " " + screenMessage + " ");
                txtMarquee.setSelected(true);
                color.clearAnimation();
                step1.setText(null);                                                                    //remove menu values
                step2.setText(null);
                etf.setText(null);
                progressBarProximity.setProgress(0);
            }

            MyService.messageToActivity = "null";
            new Thread(new SecondActivity.Thread1()).start();


        } else if (referenceMessage.equals("INF")) {

            String stringMessage = message.substring(3);                                              //display proximity info
            String messageProximity = stringMessage.substring(0, 3);
            String messageStep1 = stringMessage.substring(3, 5);                                    //display menu info
            String messageStep2 = stringMessage.substring(5, 7);
            String messageEtf = stringMessage.substring(7);

            step1.setText(messageStep1);
            step2.setText(messageStep2);
            etf.setText(messageEtf);

            int messageProximityInt = Integer.parseInt(messageProximity);

            progressBarProximity.setProgress(messageProximityInt);
            if (messageProximityInt >= 66) {
                progressBarProximity.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
            } else if (messageProximityInt >= 33) {
                progressBarProximity.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
            } else if (messageProximityInt >= 0) {
                progressBarProximity.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
            }


            MyService.messageToActivity = "null";
            new Thread(new SecondActivity.Thread1()).start();

        } else if (referenceMessage.equals("IMG")) {

            //to do
            MyService.messageToActivity = "null";
            new Thread(new SecondActivity.Thread1()).start();

        } else {

            if (message.equals("start")) {
                color.setImageResource(greenColor);
                menu.setImageResource(greenMenu);
                label.setImageResource(greenLabel);
                txtMarquee.setText("Ready Ready Ready");
                txtMarquee.setSelected(true);
                color.clearAnimation();
            }
            if (message.equals("PGD01")) {
                finish();
            }

            MyService.messageToActivity = "null";
            new Thread(new SecondActivity.Thread1()).start();

        }

    }

    private void randomProximity() {

        Handler handler = new Handler();                                                    //Start thread 1 after 1 second
        handler.postDelayed(new Runnable() {
            @SuppressLint("UseCompatLoadingForDrawables")
            public void run() {
                if (fakeProximity < 99 && fakeProximityBool) {
                    fakeProximity = fakeProximity + 1;
                } else {
                    fakeProximityBool = false;
                }
                if (fakeProximity > 10 && !fakeProximityBool) {
                    fakeProximity = fakeProximity - 1;
                } else {
                    fakeProximityBool = true;
                }

                progressBarProximity.setProgress(fakeProximity);
                if (fakeProximity >= 66) {
                    progressBarProximity.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_red));
                } else if (fakeProximity >= 33) {
                    progressBarProximity.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_yellow));
                } else if (fakeProximity >= 0) {
                    progressBarProximity.setProgressDrawable(getDrawable(R.drawable.custom_progress_bg_green));
                }
                randomProximity();


            }
        }, 60); //5 seconds.                                                              //try to connect every 10 second

    }


    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);                     //animation out
    }

}

