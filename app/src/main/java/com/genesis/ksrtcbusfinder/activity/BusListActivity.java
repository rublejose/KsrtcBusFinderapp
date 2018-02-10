package com.genesis.ksrtcbusfinder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.genesis.ksrtcbusfinder.R;
import com.genesis.ksrtcbusfinder.adapter.BusListAdapter;
import com.genesis.ksrtcbusfinder.model.BusListModel;
import com.genesis.ksrtcbusfinder.task.BusList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusListActivity extends AppCompatActivity {
    String json_string;
    ListView busListView=null;
    JSONObject jsonObject;
    JSONArray jsonArray;
    BusListAdapter busListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        busListView=findViewById(R.id.busListView);
        busListAdapter=new BusListAdapter(this,R.layout.row_layout);
        busListView.setAdapter(busListAdapter);
        json_string=getIntent().getStringExtra("value");
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count=0;
            while (count<jsonArray.length()){
                JSONObject jsonObject1=jsonArray.getJSONObject(count);
                BusListModel busListModel=new BusListModel();
                busListModel.setBus_id(jsonObject1.getString("bus_id"));
                busListModel.setTime(jsonObject1.getString("time"));
                busListModel.setLocation(jsonObject1.getString("position"));
                busListAdapter.add(busListModel);
                count++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
