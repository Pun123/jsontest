package com.example.pun.htttptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.loopj.android.http.*;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("httptest", String.valueOf(a));


        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://deewaste.ddns.net/?q=services/session/token", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("httptest", "Fetch Passed");
                        String csrf = new String(responseBody);

                        Log.d("httptest", csrf);
                        fetchData();
                        login(csrf);



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("httptest", "Fetch Failed");

                    }
        });


        //view node



}

public static void fetchData(){
    AsyncHttpClient client = new AsyncHttpClient();
    client.get("http://deewaste.ddns.net/?q=my_endpoint/node/10.json", null, new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            Log.d("httptest", "Success");
            String vid = response.optString("vid");
            Log.d("httptest", "Node ID is " + String.valueOf(vid));
            String title = response.optString("title");
            Log.d("httptest", "Title is " + String.valueOf(title));


        }


    });

}

/*public static void register(){
    AsyncHttpClient client = new AsyncHttpClient();
    client.addHeader("Content-Type", "application/x-www-form-urlencoded");
    client.addHeader("Accept", "application/json");

    RequestParams params = new RequestParams();
    params.put("name", "Test2");
    params.put("mail", "test2@gmail.com");
    params.put("pass", "1222333");
    params.put("pass2","1222333");

    client.post("http://deewaste.ddns.net/?q=my_endpoint/user/login.json", params, new JsonHttpResponseHandler(){

        public void OnSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("httptest", "User created");
        }
    });
} */


public static void login(String csrf){
    AsyncHttpClient client = new AsyncHttpClient();
    client.addHeader("Content-Type", "application/x-www-form-urlencoded");
    client.addHeader("Accept", "application/json");

    client.addHeader("X-CSRF-Token", csrf);


    RequestParams params = new RequestParams();
    params.put("username", "admin");
    params.put("password", "pun1112355687");

    client.post("http://deewaste.ddns.net/?q=my_endpoint/user/login.json", params, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // If the response is JSONObject instead of expected JSONArray
            Log.d("httptest", "Login Sucessful");
            String token = response.optString("token");

            Log.d("httptest", token );
            try {
                createNode(token);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
            // Pull out the first event on the public timeline
            Log.d("httptest", "Login Failed");


        }


    });
}

public static void createNode(String token) throws UnsupportedEncodingException {
    AsyncHttpClient client = new AsyncHttpClient();
    client.addHeader("Content-Type", "application/x-www-form-urlencoded");
    client.addHeader("Accept", "application/json");

    client.addHeader("X-CSRF-Token", token);

    JSONObject jsonParams = new JSONObject();
    try {
        jsonParams.put("title","testtitle");
        jsonParams.put("type", "report");
        

        StringEntity entity = new StringEntity(jsonParams.toString());
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post("http://deewaste.ddns.net/?q=my_endpoint/node.json", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("httptest", "success");


            }

        });


    } catch (JSONException e) {
        e.printStackTrace();
    }





}



}














        /*client.get("http://deewaste.ddns.net/?q=services/session/token", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("httptest", "Fetch Passed");
                String csrf = new String(responseBody);

                Log.d("httptest", csrf);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("httptest", "Fetch Failed");

            }
        }); */



      /*  client.post("http://deewaste.ddns.net/?q=my_endpoint/system/connect.json", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("httptest", "Success");
                String sessid = response.optString("sessid");

                try {
                    JSONObject userObject = response.getJSONObject("user");
                    int uid = userObject.optInt("uid");
                    Log.d("httptest", "User ID is " + String.valueOf(uid));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("httptest", "User object does not exist");

                }


                Log.d("httptest", String.valueOf(response));


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline


            }
        });

    }
} */
