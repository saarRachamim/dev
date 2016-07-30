package com.example.saar.locationalert;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Saar on 1/16/2016.
 */
public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://alertlocation.netau.net/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserData(User user, GetUserCallBack callBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, callBack).execute();
    }

    public void getUserData(User user, GetUserCallBack callBack) {
        progressDialog.show();
        new GetUserDataAsyncTask(user, callBack).execute();
    }

    public class GetUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallback;
        private OkHttpClient client;

        public GetUserDataAsyncTask(User user, GetUserCallBack userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected void onPostExecute(User user) {
            progressDialog.dismiss();
            userCallback.done(user);

            super.onPostExecute(user);
        }

        @Override
        public User doInBackground(Void... params) {

//            RequestBody requestBody = new FormEncodingBuilder().add("username", user.userName).add("password", user.password).build();
//            Request request = new Request.Builder().url(SERVER_ADDRESS + "getUserData.php").post(requestBody).build();
//            Response response = null;
//            User user = null;

            try {
                client = new OkHttpClient();
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                JSONObject Jobject = new JSONObject();
                //Jobject.put("username", user.userName);
                //Jobject.put("password", user.password);
                RequestBody requestBody = new FormEncodingBuilder().add("username", user.userName).add("password", user.password).build();
                //RequestBody body = RequestBody.create(JSON, Jobject.toString());
                Request request = new Request.Builder()
                        .url(SERVER_ADDRESS + "GetUserData.php")
                        .post(requestBody)
                        .build();
                Response response = client.newCall(request).execute();
//                response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonData);

                if (jsonArray.length() == 0) {
                    user = null;
                } else {
                    Jobject = jsonArray.getJSONObject(0);
                    String userName = Jobject.getString("username");
                    String password = Jobject.getString("password");
                    String name = Jobject.getString("name");
                    user = new User(userName, password, name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallBack callBack;
        private OkHttpClient client;

        public StoreUserDataAsyncTask(User user, GetUserCallBack callBack) {
            this.user = user;
            this.callBack = callBack;
            client = new OkHttpClient();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callBack.done(null);

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                RequestBody requestBody = new FormEncodingBuilder().add("username", user.userName).add("password", user.password).add("name", user.name).build();
                Request request = new Request.Builder().url(SERVER_ADDRESS + "Register.php").post(requestBody).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

}
