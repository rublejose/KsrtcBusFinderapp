package com.genesis.ksrtcbusfinder.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.genesis.ksrtcbusfinder.R;
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

public class StaffActivity extends AppCompatActivity {
    TextView staffNameTextView=null;
    TextView busIdTextView=null;
    TextView depTimeTextView=null;
    TextView currentStopTextView=null;
    Button updateButton=null;
    TextView nextTimeTextView;
    String bus_id;
    String time;
    String stop_name;
    String stop_id;
    String staff_name;
    String staff_id;
    String trip_id;
    String stop_no;
    int i=1;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        staff_id=getIntent().getStringExtra("staff_id");
        staffNameTextView=findViewById(R.id.staffNameTextView);
        nextTimeTextView=findViewById(R.id.nextTimeTextView);
        busIdTextView=findViewById(R.id.busIdTextView);
        depTimeTextView=findViewById(R.id.depTimeTextView);
        updateButton=findViewById(R.id.updateButton);
        currentStopTextView=findViewById(R.id.currentStopTextView);
        sharedPreferences=getSharedPreferences("onResume",MODE_PRIVATE);
        stop_no=sharedPreferences.getString("stop_number","1");
        String check=sharedPreferences.getString("next_stop","no");
        Toast.makeText(this, check, Toast.LENGTH_SHORT).show();
        if(check.equals("completed")){
            stop_no=1+"";
        }
        SetDetails setDetails=new SetDetails();
        setDetails.execute("fetch_initial",staff_id,stop_no);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDetails setDetails1=new SetDetails();
                setDetails1.execute("next",stop_id,trip_id,stop_no);
            }
        });
    }

    public  class SetDetails extends AsyncTask<String,Void,String>{
        String Staff_Details = "http://" + IpConfig.IP + "/ksrtc/staff_details.php";
        String Location_Update = "http://" + IpConfig.IP + "/ksrtc/location_update.php";


        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            if (strings[0].equals("fetch_initial")){
                try {
                    url = new URL(Staff_Details);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("staff_id", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8")+ "&"
                            + URLEncoder.encode("stop_no", "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8");
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
            }else  if (strings[0].equals("next")){
                try {
                    url = new URL(Location_Update);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("stop_id", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8")+"&"
                            + URLEncoder.encode("trip_id", "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8")+"&"
                            + URLEncoder.encode("stop_number", "UTF-8") + "=" + URLEncoder.encode(strings[3], "UTF-8");
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
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String code= jsonObject1.getString("code");
                if (code.equals("fetch_initial")) {
                    bus_id = jsonObject1.getString("bus_id");
                    time = jsonObject1.getString("time");
                    stop_name = jsonObject1.getString("stop_name");
                    stop_id = jsonObject1.getString("stop_id");
                    staff_name = jsonObject1.getString("staff_name");
                    trip_id = jsonObject1.getString("trip_id");
                    staffNameTextView.setText(staff_name);
                    busIdTextView.setText(bus_id);
                    depTimeTextView.setText(time);
                    if (Integer.parseInt(stop_no) > 1) {
                        updateButton.setText(sharedPreferences.getString("next_stop", "null"));
                        nextTimeTextView.setText(sharedPreferences.getString("next_time","null"));
                        currentStopTextView.setText(sharedPreferences.getString("current_stop","null"));
                        i=Integer.parseInt(stop_no);
                    } else{
                        updateButton.setText("Start");
                        currentStopTextView.setText(stop_name);
                    }
                }else if (code.equals("next")){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    currentStopTextView.setText(stop_name);
                    editor.putString("current_stop",stop_name);
                    time = jsonObject1.getString("time");
                    stop_name = jsonObject1.getString("stop_name");
                    stop_id = jsonObject1.getString("stop_id");
                    nextTimeTextView.setText(time);
                    updateButton.setText(stop_name);
                    if(staff_name.equals("completed")){
                        editor.putString("stop_number",1+"");
                    }else {
                        editor.putString("stop_number",i+"");
                    }
                    editor.putString("next_stop",stop_name);
                    editor.putString("next_time",time);
                    stop_no=i+"";
                    editor.commit();
                    i++;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
