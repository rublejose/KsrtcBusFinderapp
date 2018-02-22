package com.genesis.ksrtcbusfinder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.genesis.ksrtcbusfinder.BookingActivity;
import com.genesis.ksrtcbusfinder.R;
import com.genesis.ksrtcbusfinder.adapter.BusListAdapter;
import com.genesis.ksrtcbusfinder.model.BusListModel;
import com.genesis.ksrtcbusfinder.task.BusList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusListActivity extends AppCompatActivity {
    String json_string;
    ListView busListView=null;
    JSONObject jsonObject;
    JSONArray jsonArray;
    BusListAdapter busListAdapter;
    ArrayList<BusListModel> list=new ArrayList<BusListModel>();
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
                list.add(busListModel);
                busListAdapter.add(busListModel);
                count++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BusListModel listModel=list.get(i);
                String bus_id=listModel.getBus_id();
                Intent intent=new Intent(BusListActivity.this, BookingActivity.class);
                intent.putExtra("bus_id",bus_id);
                startActivity(intent);
            }
        });
    }


}
