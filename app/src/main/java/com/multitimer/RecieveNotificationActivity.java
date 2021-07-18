package com.multitimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Calendar;

public class RecieveNotificationActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_notification);

        chronometer = findViewById(R.id.chrono);


        TextView finTimerMessage = findViewById(R.id.finTimeMessTxt);
        TextView startTimerMessage = findViewById(R.id.startTimeMessText);
        Button resumeBtn = findViewById(R.id.resumeBtn);

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
    }

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