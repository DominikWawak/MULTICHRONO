package com.multitimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    Message message ;
    private  EditText topicSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = new Message();
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("startTime")){
            Intent intent= new Intent(MainActivity.this,RecieveNotificationActivity.class);
            intent.putExtra("startTime",getIntent().getStringExtra("startTime"));
            intent.putExtra("finishTime",Calendar.getInstance().getTime());
            startActivity(intent);
        }

        // start button
        Button sendButton = findViewById(R.id.sendButton);
        // field for channel
        topicSub = findViewById(R.id.topicSubscription);
        // sub button
        Button subToTopic = findViewById(R.id.subToTopic);

        message.setmRequestQueue(Volley.newRequestQueue(this));

        subToTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setTopic(topicSub.getText().toString());
                FirebaseMessaging.getInstance().subscribeToTopic(message.getTopic());
                //message.sendNotification("Success","Successfully subscribed to the topic");
            }
        });





        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.sendNotification("timer has started","Please don't Click");

            }
        });


    }




}