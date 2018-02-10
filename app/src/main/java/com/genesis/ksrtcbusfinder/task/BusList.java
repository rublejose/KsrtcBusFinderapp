package com.genesis.ksrtcbusfinder.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.genesis.ksrtcbusfinder.activity.BusListActivity;
import com.genesis.ksrtcbusfinder.activity.HomeActivity;
import com.genesis.ksrtcbusfinder.ipconfig.IpConfig;

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
 * Created by ruble on 08-02-2018.
 */

public class BusList extends AsyncTask<String,Void,String> {
    Activity activity;
    Context context;
    String Fetch_result = "http://" + IpConfig.IP + "/ksrtc/fetch_result.php";
    public BusList(Context context) {
        this.context=context;
        activity= (Activity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String task = strings[0];
        if (task.equals("bus_list")){
            try {
                URL url = new URL(Fetch_result);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("source", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8") + "&"
                        + URLEncoder.encode("destination", "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8") + "&"
                        + URLEncoder.encode("bus_type", "UTF-8") + "=" + URLEncoder.encode(strings[3], "UTF-8");
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

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(activity, BusListActivity.class);
        intent.putExtra("value",s);
        activity.startActivity(intent);
        super.onPostExecute(s);
    }
}
