package com.example.nubia_multipage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FourthActivity extends AppCompatActivity {

    Boolean running=true;
    ToggleButton displayButton;
    ImageView connectedImage;
    public static int SERVER_PORT=8080;

    public static Activity fa4;

    TextView battery;
    ImageView chargingImage;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            battery.setText(String.valueOf(level)+"%");


            //See if in charge or not
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            if(isCharging){
                chargingImage.setVisibility(View.VISIBLE);
            }else{
                chargingImage.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.fourth_ly_settings);

        fa4=this; //assign name activity 4

        displayButton=findViewById(R.id.displayButton);
        connectedImage=findViewById(R.id.connectedImage);
        chargingImage=findViewById(R.id.charging);
        chargingImage.setVisibility(View.GONE);

        battery=this.findViewById(R.id.battery);
        this.registerReceiver(this.mBatInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(displayButton.isChecked()){

                    SERVER_PORT=8081;
                    saveData();

                }else{

                    SERVER_PORT=8080;
                    saveData();

                }
            }
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();

        MainActivity.screenSaverOn=false;
        MyService.currentPage=4;

        if (MyService.connectStatus.equals(true)) {
            connectedImage.setVisibility(View.VISIBLE);
        }else{
            connectedImage.setVisibility(View.GONE);
        }

            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);            //remember which display we are
        Integer port = sh.getInt("port", 0);
        String portString=port.toString();

        if(portString.equals("8080")){
            SERVER_PORT=8080;                                                                       //if display 1 port=8080
            displayButton.setChecked(false);
        }else{
            SERVER_PORT=8081;                                                                       //if display 2 port=8081 button=checked
            displayButton.setChecked(true);

        }

        //portView.setText(port);


        MainActivity.startStatus=false;                                                             //reset start status (stop when change activity)
        running=true;
        MyService.messageToActivity="null";

        if(MyService.connectStatus){                                                                // if tcp connected
            new Thread(new FourthActivity.Thread2("PGR04")).start();                                // send current page name to server
            new Thread(new FourthActivity.Thread1()).start();
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
            MyService.output.write(message);
            MyService.output.flush();
        }
    }


    private  void receiveValue(String message){

        if(MainActivity.startStatus){                                                               //if another activity is called stop this activity
            finish();
        }

        if(message.equals("null")) {
            new Thread(new FourthActivity.Thread1()).start();

        }else if(message.equals("PGD02")) {
            finish();
        }else if(message.equals("PGD03")) {
            finish();
        }else if(message.equals("PGD05")) {
            finish();
        }else if(message.equals("PGD04")) {
            MyService.messageToActivity="null";
            new Thread(new FourthActivity.Thread1()).start();
        }else if(message.equals("PGD06")) {
            finish();
        }else if(message.equals("PGD07")) {
            finish();
        }else if(message.equals("PGD01")) {
            finish();
        }else{

            MyService.messageToActivity="null";
            new Thread(new FourthActivity.Thread1()).start();
        }

    }

    public void saveData(){                                                                         //save display: if 1 port 8080 if 2 port 8081
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putInt("port", SERVER_PORT);
        myEdit.apply();

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
