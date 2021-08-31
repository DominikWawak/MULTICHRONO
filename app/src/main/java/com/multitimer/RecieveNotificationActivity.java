package com.multitimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.time.Duration;
import java.util.Calendar;

import hallianinc.opensource.timecounter.StopWatch;

public class RecieveNotificationActivity extends AppCompatActivity {

    private StopWatch stopWatch;
    //private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private Message message ;

    private TextView timerText;
    private TextView lapView;

    private long lastLap = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_notification);

        message = new Message();
        message.setmRequestQueue(Volley.newRequestQueue(this));

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        timerText = findViewById(R.id.timerText);
        lapView = findViewById(R.id.lapView);


        TextView finTimerMessage = findViewById(R.id.finTimeMessTxt);
        TextView startTimerMessage = findViewById(R.id.startTimeMessText);
        Button resumeBtn = findViewById(R.id.resumeBtn);
        Button lapBtn = findViewById(R.id.lapBtn);

        // displays the final time
        if(getIntent().hasExtra("finishTime")){
            String finishTimer =  getIntent().getStringExtra("finishTime");
            startTimerMessage.setText(finishTimer);
        }
        // displays the start timer
        if(getIntent().hasExtra("startTime")){
            String startTimer =  getIntent().getStringExtra("startTime");
            startTimerMessage.setText(startTimer);
            stopWatch = new StopWatch(timerText);
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

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.sendNotification("res","res");
            }
        });
    }


    /**
     * Code for pressing the lap button
     */
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {


                    if(intent.getStringExtra("key").equals("key")) {

                        long timeStopped = stopWatch.getTime();
                        //SystemClock.elapsedRealtime() - chronometer.get.getBase();

                        long t = timeStopped - lastLap;


                        lapView.setText(lapView.getText() + "\n" + "LAP  │ " + stopWatch.getTime() + "| " + t / 1000 + " s");
                        lastLap = timeStopped;

                    }

            }
        }
    };


    /**
     * starts the chronometer
     * should implement a wait for user to start
     */
    public void startChronometer(){
        if(!running){
            //stopWatch.start().setBase(SystemClock.elapsedRealtime()- pauseOffset);
            stopWatch.start();
            running = true;
        }
    }

    /**
     * pauses the chronometer
     * prob should rename to pauseChronometer and have another stopChronometer that completely stops it
     */
    public void stopChronometer(){
        if(running){
            int timeToSave = stopWatch.getTime();
            stopWatch.pause();
            //pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

    }


    /**
     * resets the chronometer back to the start - discards the current timer
     */
    public void resetChronometer(){
        stopWatch.stop();
        //setPauseOffset(0);
    }

    private long getPauseOffset() {
        return pauseOffset;
    }

    public void setPauseOffset(long pauseOffset) {
        this.pauseOffset = pauseOffset;
    }
}