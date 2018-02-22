package com.genesis.ksrtcbusfinder.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.genesis.ksrtcbusfinder.activity.HomeActivity;
import com.genesis.ksrtcbusfinder.activity.MainActivity;
import com.genesis.ksrtcbusfinder.activity.StaffActivity;
import com.genesis.ksrtcbusfinder.ipconfig.IpConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ruble on 02-02-2018.
 */

public class DataTransfer extends AsyncTask<String,Void,String> {
    Context context;
    Activity activity;
    String User_Registration = "http://" + IpConfig.IP + "/ksrtc/user_reg.php";
    String User_Login = "http://" + IpConfig.IP + "/ksrtc/user_login.php";

    String NAME;
    String staff_id;
    String JSON_STRING="";
    public DataTransfer(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... strings) {
        String task = strings[0];
        if (task.equals("user_login")){
            try {
                staff_id=strings[1];
                URL url = new URL(User_Login);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8");
                bufferedwriter.write(data);
                bufferedwriter.flush();
                bufferedwriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (task.equals("user_SignUp")) {
            try {
                URL url = new URL(User_Registration);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                NAME = strings[2];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8") + "&"
                        + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[3], "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(strings[4], "UTF-8");
                bufferedwriter.write(data);
                bufferedwriter.flush();
                bufferedwriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String results) {
        try {

            JSONObject jsonObject = new JSONObject(results);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            String code = jsonObject1.getString("code");
            String message = jsonObject1.getString("message");
            if (code.equals("user_reg_true")) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            } else if (code.equals("user_reg_false")) {
                Toast.makeText(activity,message,Toast.LENGTH_LONG).show();
            }else if (code.equals("user_login_true")) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.putExtra("user_id",staff_id);
                activity.startActivity(intent);
            } else if (code.equals("staff_login_true")) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, StaffActivity.class);
                intent.putExtra("staff_id",staff_id);
                activity.startActivity(intent);
            } else if (code.equals("user_login_false")) {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(results);

    }
}
