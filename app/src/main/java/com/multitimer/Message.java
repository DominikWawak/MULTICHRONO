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

    // change to a random string
    private String topic = getRandomString();


    /*
     *   https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
     */
    private String getRandomString() {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

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


        /**
         * builds the message to be sent as a push notification
         */
        JSONObject mainObject = new JSONObject();
        try{
            mainObject.put("to","/topics/"+topic);
            JSONObject notificationObj = new JSONObject();
            // puts the title in the push notification
            notificationObj.put("title",title);
            // puts the body in the push notification
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
                    header.put("authorization","key= "+BuildConfig.F_KEY.toString());
                    return header;

                }
            };

            mRequestQueue.add(request);

        } catch (JSONException e ){
            e.printStackTrace();
        }


    }
}
