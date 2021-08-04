package com.multitimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Calendar;

public class RecieveNotificationActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private Message message ;


    private TextView lapView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_notification);

        message = new Message();
        message.setmRequestQueue(Volley.newRequestQueue(this));

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        chronometer = findViewById(R.id.chrono);
        lapView = findViewById(R.id.lapView);


        TextView finTimerMessage = findViewById(R.id.finTimeMessTxt);
        TextView startTimerMessage = findViewById(R.id.startTimeMessText);
        Button resumeBtn = findViewById(R.id.resumeBtn);

        Button lapBtn = findViewById(R.id.lapBtn);

        if(getIntent().hasExtra("finishTime")){
            String finishTimer =  getIntent().getStringExtra("finishTime");
            startTimerMessage.setText(finishTimer);


        }





        if(getIntent().hasExtra("startTime")){
            String startTimer =  getIntent().getStringExtra("startTime");
           startTimerMessage.setText(startTimer);
           startChronometer();

        }

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChronometer();
                finTimerMessage.setText(Calendar.getInstance().getTime().toString());
            }
        });

        lapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.sendNotification("lap","lap");
            }
        });
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String str = intent.getStringExtra("key");
                lapView.setText("LAPPPPPPPP");

            }
        }
    };



    public void startChronometer(){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);
            chronometer.start();
            running = true;
        }

    }
    public void stopChronometer(){

        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

    }
    public void resetChronometer(){

        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;


    }
}