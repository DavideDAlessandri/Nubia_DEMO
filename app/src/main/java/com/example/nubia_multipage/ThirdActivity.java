package com.example.nubia_multipage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    ImageView zoom1, zoom2;
    Button zoomButton;
    Boolean sw=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.third_ly_teach);

        zoom1=findViewById(R.id.zoom1);
        zoom2=findViewById(R.id.zoom2);
        zoomButton=findViewById(R.id.zoom);

        zoom2.setVisibility(View.GONE);

        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sw){

                    Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
                    animation.setDuration(250); //1 second duration for each animation cycle
                    animation.setInterpolator(new LinearInterpolator());

                    zoom1.startAnimation(animation); //to start animation

                    Animation animation2 = new AlphaAnimation(0, 1); //to change visibility from visible to invisible
                    animation2.setDuration(500); //1 second duration for each animation cycle
                    animation2.setInterpolator(new LinearInterpolator());

                    zoom2.startAnimation(animation2); //to start animation

                    zoom1.setVisibility(View.GONE);
                    zoom2.setVisibility(View.VISIBLE);

                    sw=false;
                }else{

                    Animation animation = new AlphaAnimation(0, 1); //to change visibility from visible to invisible
                    animation.setDuration(500); //1 second duration for each animation cycle
                    animation.setInterpolator(new LinearInterpolator());

                    zoom1.startAnimation(animation); //to start animation

                    Animation animation2 = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
                    animation2.setDuration(250); //1 second duration for each animation cycle
                    animation2.setInterpolator(new LinearInterpolator());

                    zoom2.startAnimation(animation2); //to start animation

                    zoom1.setVisibility(View.VISIBLE);
                    zoom2.setVisibility(View.GONE);
                    sw=true;
                }

            }
        });


    }


    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);                     //animation out
    }
}


