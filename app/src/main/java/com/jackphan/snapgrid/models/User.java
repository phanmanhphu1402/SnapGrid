package com.jackphan.snapgrid.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    @SerializedName("ID")
    private String id;
    @SerializedName("FullName")
    private String name;
    @SerializedName("Email")
    private String email;
    @SerializedName("Password")
    private String password;
    @SerializedName("Avatar")
    private String avatar;
    @SerializedName("DataJoin")
    private String dataJoin;
    @SerializedName("Followers")
    private ArrayList followers;
    @SerializedName("Followings")
    private ArrayList followings;
    @SerializedName("Decription")
    private  String description;

    @SerializedName("fcmToken")
    private  String fcmToken;

    public User() {

    }

    public User(String id, String name, String email,
                String password, String avatar, String dataJoin,
                ArrayList followers, ArrayList followings, String description, String fcmToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.dataJoin = dataJoin;
        this.followers = followers;
        this.followings = followings;
        this.description = description;
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public ArrayList getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList followers) {
        this.followers = followers;
    }

    public ArrayList getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList followings) {
        this.followings = followings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDataJoin() {
        return dataJoin;
    }

    public void setDataJoin(String dataJoin) {
        this.dataJoin = dataJoin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
