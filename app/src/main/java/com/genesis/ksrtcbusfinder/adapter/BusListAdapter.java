package com.genesis.ksrtcbusfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.genesis.ksrtcbusfinder.R;
import com.genesis.ksrtcbusfinder.model.BusListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruble on 09-02-2018.
 */

public class BusListAdapter extends ArrayAdapter {
    List list=new ArrayList();
    int i=1;
    public BusListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(BusListModel busListModel) {
        super.add(busListModel);
        list.add(busListModel);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row=convertView;
        BusHolder busHolder;
        if (row==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.row_layout,parent,false);
            busHolder=new BusHolder();
            busHolder.bus_id=row.findViewById(R.id.busIdTextView);
            busHolder.time=row.findViewById(R.id.timeTextView);
            busHolder.location=row.findViewById(R.id.locationTextView);
            busHolder.sl=row.findViewById(R.id.slTextView);
            row.setTag(busHolder);

        }else{
            busHolder= (BusHolder) row.getTag();
        }
        BusListModel busListModel= (BusListModel) this.getItem(position);
        busHolder.bus_id.setText(busListModel.getBus_id());
        busHolder.time.setText(busListModel.getTime());
        busHolder.location.setText(busListModel.getLocation());
        busHolder.sl.setText(i+"");
        return row;
    }
    static class BusHolder{
        TextView bus_id,time,location,sl;
    }

}
