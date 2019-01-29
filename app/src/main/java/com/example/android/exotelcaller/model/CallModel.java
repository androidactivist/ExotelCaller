package com.example.android.exotelcaller.model;

public class CallModel {

    private String number, name;
    long date;
    int callType, duration;

    public CallModel(String name, int duration, long date, String number, int callType) {

        this.number = number;
        this.duration = duration;
        this.date = date;
        this.name = name;
        this.callType = callType;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }








}
