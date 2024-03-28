package com.android.snapgrid.models;

public class Event {
    private int idEvent;
    private String nameEvent;
    private String descripton;
    private String location;
    private String timeStart;
    private String timeEnd;
    private int idCreator;

    public Event() {
    }

    public Event(int idEvent, String nameEvent, String descripton, String location, String timeStart, String timeEnd, int idCreator) {
        this.idEvent = idEvent;
        this.nameEvent = nameEvent;
        this.descripton = descripton;
        this.location = location;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.idCreator = idCreator;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }
}
