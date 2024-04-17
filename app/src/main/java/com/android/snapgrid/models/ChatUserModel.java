package com.android.snapgrid.models;

import android.view.View;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class ChatUserModel {
//    private String id, uid, name, imageURL;
    private String id, lassMessage;
    private List<String> uid;
    @ServerTimestamp
    private Date time;

    public ChatUserModel(){

    }

    public ChatUserModel(String id, String lassMessage, List<String> uid, Date time) {
        this.id = id;
        this.lassMessage = lassMessage;
        this.uid = uid;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLassMessage() {
        return lassMessage;
    }

    public void setLassMessage(String lassMessage) {
        this.lassMessage = lassMessage;
    }

    public List<String> getUid() {
        return uid;
    }

    public void setUid(List<String> uid) {
        this.uid = uid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    //    public ChatUserModel(String id, String uid, String name, String imageURL){
//        this.id = id;
//        this.uid = uid;
//        this.name = name;
//        this.imageURL = imageURL;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getImageURL() {
//        return imageURL;
//    }
//
//    public void setImageURL(String imageURL) {
//        this.imageURL = imageURL;
//    }
}
