package com.example.forest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
TextView txtmsg,txtbname,txtA,txtmonth,txtbfname;
EditText editTextname;
Button button;

    String ServerURL = "http://192.168.0.3/exam/Backup/month.php";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    JSONObject jsonObject=new JSONObject();
    String strname;
    private String res;
    private String TAG="log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtmsg=(TextView)findViewById(R.id.textView);
        txtbname=(TextView)findViewById(R.id.txt_name);
        txtA=(TextView)findViewById(R.id.textViewA);
        txtmonth=(TextView)findViewById(R.id.textViewmonth);
        txtbfname=(TextView)findViewById(R.id.textViewbfname);
        editTextname=(EditText)findViewById(R.id.txtname);
        button=(Button)findViewById(R.id.btnsubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strname=editTextname.getText().toString();

                try {
                    jsonObject.put("name",strname);
                    Log.d(TAG, "txtname"+jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new getdata(MainActivity.this).execute();

            }
        });
    }

    private  class getdata extends AsyncTask<String,String,String> {

        Context context;

        getdata(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
            Log.d(TAG, "Back JSON value: " + requestBody);

            Request request = new Request.Builder().url(ServerURL).post(requestBody).build();
            Log.d(TAG, "resresultrequest" + request);

            try (Response response = okHttpClient.newCall(request).execute()) {
                res = response.body().string();
                Log.d(TAG, "resresultResponce" + res);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }


        @Override
        protected void onPostExecute(String w) {
            super.onPostExecute(w);

            if (res.matches("(.*)Successfully(.*)"))
            {
                JSONArray jsonArray = null;
                try {
                    //JSONObject jsonObject=new JSONObject(w);
                    //jsonObject.getString("month");
                    jsonArray = new JSONArray(w);
                    Log.d(TAG, "onPostExecute jsonobject" + jsonArray);

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.d(TAG, "onPostExecute jsonobject" + jsonObject);
                    txtmsg.setText(jsonObject.getString("month"));
                    txtbname.setText(jsonObject.getString("name"));
                    txtmonth.setText("Month");
                    txtA.setText("A");
                    txtbfname.setText("Bird Found In");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
                {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(w);

                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        txtmsg.setText(jsonObject.getString("month"));
                        txtbname.setText(jsonObject.getString("name"));
                        txtmonth.setText("Month");
                        txtA.setText("A");
                        txtbfname.setText("Bird Found In");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

        }

    }}
