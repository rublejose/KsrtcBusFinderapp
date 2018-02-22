package com.genesis.ksrtcbusfinder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.genesis.ksrtcbusfinder.R;
import com.genesis.ksrtcbusfinder.ipconfig.IpConfig;
import com.genesis.ksrtcbusfinder.model.StopModel;
import com.genesis.ksrtcbusfinder.task.BusList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Spinner sourceSpinner=null;
    Spinner destinationSpinner=null;
    Spinner busTypeSpinner=null;
    Button searchButton=null;
    String username;
    SharedPreferences sharedPreferences;
    ArrayList<StopModel> stopList=new ArrayList<StopModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sourceSpinner=findViewById(R.id.sourceSpinner);
        destinationSpinner=findViewById(R.id.destinationSpinner);
        busTypeSpinner=findViewById(R.id.busTypeSpinner);
        searchButton=findViewById(R.id.searchButton);
        username=getIntent().getStringExtra("user_id");
        sharedPreferences=getSharedPreferences("account_info",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.commit();
        DataFetch dataFetch=new DataFetch(HomeActivity.this);
        dataFetch.execute("fetch_stops");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String source=stopList.get(sourceSpinner.getSelectedItemPosition()).getStop_id();
               String destination=stopList.get(destinationSpinner.getSelectedItemPosition()).getStop_id();
               String type=busTypeSpinner.getSelectedItem().toString();
                BusList busList =new BusList(HomeActivity.this);
                Toast.makeText(HomeActivity.this, source+""+destination+""+type,Toast.LENGTH_SHORT).show();
                busList.execute("bus_list",source,destination,type);
            }
        });
    }
    private  void populateSpinner(){
        List<String>stopName=new ArrayList<String>();
        int i=0;
        while(i<stopList.size()){
            stopName.add(stopList.get(i).getStop_name());
            i++;
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stopName);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(spinnerAdapter);
        destinationSpinner.setAdapter(spinnerAdapter);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.bus_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adapter);
    }
    public class DataFetch extends AsyncTask<String,Void,String> {
        String Fetch_Stops = "http://" + IpConfig.IP + "/ksrtc/fetch_stops.php";
        String JSON_STRING="";
        Activity activity;
        Context context;
        public DataFetch(Context context) {
            this.context=context;
            activity= (Activity) context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings[0].equals("fetch_stops")){
                try {
                    URL url = new URL(Fetch_Stops);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING=bufferedReader.readLine())!=null){
                        stringBuilder.append(JSON_STRING+"\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
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
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray= jsonObject.getJSONArray("server_response");
                int count=0;
                String stop_id,stop_name;
                while(count<jsonArray.length()) {
                    JSONObject jsonObject1=jsonArray.getJSONObject(count);
                    stop_id=jsonObject1.getString("stop_id");
                    stop_name=jsonObject1.getString("stop_name");
                    StopModel stopModel=new StopModel();
                    stopModel.setStop_id(stop_id);
                    stopModel.setStop_name(stop_name);
                    stopList.add(stopModel);
                    count++;
                }
                populateSpinner();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
