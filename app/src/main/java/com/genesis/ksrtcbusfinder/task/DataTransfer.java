package com.genesis.ksrtcbusfinder.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.genesis.ksrtcbusfinder.HomeActivity;
import com.genesis.ksrtcbusfinder.activity.MainActivity;
import com.genesis.ksrtcbusfinder.activity.UserSignUpActivity;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ruble on 02-02-2018.
 */

public class DataTransfer extends AsyncTask<String,Void,String> {
    Context context;
    Activity activity;
    String User_Registration="http://"+ IpConfig.IP+"/ksrtc/user_registration.php";
    String NAME;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    public DataTransfer(Context context) {
        this.context=context;
        activity= (Activity) context;
    }

    @Override
    protected void onPreExecute() {
        builder=new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Connecting to server....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String task=strings[0];
        if(task.equals("user_SignUp")){
            try {
                URL url = new URL(User_Registration);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                NAME=strings[2];
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(strings[1],"UTF-8")+"&"
                        + URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(strings[2],"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(strings[3],"UTF-8");
                bufferedwriter.write(data);
                bufferedwriter.flush();
                bufferedwriter.close();
                outputStream.close();
                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                data="";
                while((data=bufferedReader.readLine())!=null){
                    stringBuilder.append(data+"\n");
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
            JSONObject jsonObject=new JSONObject(results);
            JSONArray jsonArray=jsonObject.getJSONArray("server_response");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            String code = jsonObject1.getString("code");
            if (code.equals("user_reg_true")){
                Toast.makeText(activity, "Registration Sucess", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }else if (code.equals("user_reg_false")) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(results);

    }
    public void class s

}
