package com.CGI.HackDon2018;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.JsonHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuestionUtils  {




    final String HackDon_URL = "https://apihackdon2018teamcgi.azurewebsites.net/api";
    final String LOGCAT_TAG = "HackDon";

    public IQuestionListener Questionlistener = null;

    // Recommend using LocationManager.NETWORK_PROVIDER on physical devices (reliable & fast!)
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    private Gson gson =new Gson();



    public void getQuestionList() {
        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.

        RequestParams params = new RequestParams();
//        params.put("id", "");
        String URL = HackDon_URL + "/profiles/Affinities";
        client.get(URL, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e(LOGCAT_TAG, "Fail ");

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONArray response) {

                Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());
                QuestionModel[] questionsModel;
                String temp = response.toString();
                try
                {
                     questionsModel = gson.fromJson(temp, QuestionModel[].class);

                    if (Questionlistener != null)
                    {
                        Questionlistener.onReceiveList(questionsModel);
                    }

                }
                catch (Exception e )
                {
                    Log.d("test", e.getMessage());
                }





            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, org.json.JSONArray response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                //Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();

                Log.d(LOGCAT_TAG, "Status code " + statusCode);
                Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e(LOGCAT_TAG, "Fail ");

            }


        });


    }

    public void submitQuestion(Context context, ProfileModel pm) {
        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        String jsonString = gson.toJson(pm);
        try
        {
            StringEntity entity = new StringEntity(jsonString);
            String url = HackDon_URL + "/profiles";
            client.post(context, url, entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                            Log.i("xml","StatusCode : "+i);
                        }

                        @Override
                        public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                            Log.i("xml","Sending failed");
                        }

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            Log.i("xml","Progress : "+bytesWritten);
                        }
                    });

        }
        catch (Exception e)
        {


        }

    }



/*
    private void letsDoSomeNetworking3() {
        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.

        RequestParams params = new RequestParams();
//        params.put("id", "");

        client.get(HackDon_URL,new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.e(LOGCAT_TAG, "Fail " );

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(LOGCAT_TAG, "Success! JSON: " + response.toString());

                TestModel testModel= gson.fromJson(response.toString(), TestModel.class);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e(LOGCAT_TAG, "Fail " + e.toString());
                Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();

                Log.d(LOGCAT_TAG, "Status code " + statusCode);
                Log.d(LOGCAT_TAG, "Here's what we got instead " + response.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e(LOGCAT_TAG, "Fail ");

            }

        });



    }

*/



}
