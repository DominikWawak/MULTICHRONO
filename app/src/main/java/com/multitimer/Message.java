package com.multitimer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Message {

    private RequestQueue mRequestQueue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private String topic = "def";

    public Message() {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    public void setmRequestQueue(RequestQueue mRequestQueue) {
        this.mRequestQueue = mRequestQueue;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public void sendNotification(String title, String body) {


        //Json Object sample
//        {
//            "to" :  "topics/topic name",
//            notification:
//                title :"title",
//                body: "body"
//        }



        JSONObject mainObject = new JSONObject();
        try{
            mainObject.put("to","/topics/"+topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",title);
            notificationObj.put("body",body);

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
                    //header.put("authorization","key=******************************PASTEKEYHERE****************************************");
                    header.put("authorization","key=AAAAEEquS0c:APA91bEmQ1QBuLvvpXFAyvzZiEQMaes87eJ0mQj2p0t5QlQfoKfr6L5rU_YeJ4W9Diw9Qltm8d3BdgSu_i4HO-5IPFE3migOAW4H6gFZIjp5oZbIKyL-ToqFUDi8-ENLfJ0k3P4ZbY5Q ");
                    return header;

                }
            };

            mRequestQueue.add(request);

        } catch (JSONException e ){
            e.printStackTrace();
        }


    }
}
