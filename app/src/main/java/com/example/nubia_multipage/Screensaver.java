package com.example.nubia_multipage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Screensaver extends AppCompatActivity {

    View layout;
    ImageView screensaverColor;
    public static Activity ssActivity;
    public static Boolean inScreenSaver=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.screensaver);

        ssActivity=this; //assign name screen saver activity

        layout=findViewById(R.id.layoutS);
        screensaverColor=findViewById(R.id.screensaverColor);

        Animation animation;
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.screen_saver);
        screensaverColor.startAnimation(animation);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    @Override
    protected  void onResume(){                                                                     //When enter page:
        super.onResume();
        MyService.currentPage=100;

        inScreenSaver=true;
        MainActivity.screenSaverIn=true;

    }

    @Override
    protected void onStop() {
        super.onStop();

        inScreenSaver=false;

    }


}
