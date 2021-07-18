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


    private RequestQueue mRequestQueue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private String topic = "def";
    private  EditText topicSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().hasExtra("startTime")){
            Intent intent= new Intent(MainActivity.this,RecieveNotificationActivity.class);
            intent.putExtra("startTime",getIntent().getStringExtra("startTime"));
            intent.putExtra("finishTime",Calendar.getInstance().getTime());
            startActivity(intent);
        }

        Button sendButton = findViewById(R.id.sendButton);
         topicSub = findViewById(R.id.topicSubscription);
         Button subToTopic = findViewById(R.id.subToTopic);



        mRequestQueue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic(topic);


        subToTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topic = topicSub.getText().toString();
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
            }
        });





        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();




            }
        });


    }

    private void sendNotification() {


        //Json Object sample
//        {
//            "to" :  "topics/topic name",
//            notification:
//                title :"title",
//                body: "body"
//        }

        topic = topicSub.getText().toString();

        JSONObject mainObject = new JSONObject();
        try{
            mainObject.put("to","/topics/"+topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","timer has started");
            notificationObj.put("body","Please click to control");

            JSONObject extraData = new JSONObject();
            extraData.put("startTime", Calendar.getInstance().getTime());


            mainObject.put("notification",notificationObj);
            mainObject.put("data",extraData);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=******************************PASTEKEYHERE****************************************");
                    return header;

                }
            };

            mRequestQueue.add(request);

        } catch (JSONException e ){
            e.printStackTrace();
        }


    }



}