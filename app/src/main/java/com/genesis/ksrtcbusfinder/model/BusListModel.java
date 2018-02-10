package com.genesis.ksrtcbusfinder.model;

/**
 * Created by ruble on 09-02-2018.
 */

public class BusListModel {
    String bus_id;
    String time;
    String location;

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
