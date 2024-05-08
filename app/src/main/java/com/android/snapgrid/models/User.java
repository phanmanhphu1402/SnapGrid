package com.android.snapgrid.models;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String avatar;
    private String dataJoin;
    private ArrayList followers;
    private ArrayList followings;
    private  String description;

    public User() {

    }

    public User(String id, String name, String email, String password, String avatar, String dataJoin, ArrayList followers, ArrayList followings, String description) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.dataJoin = dataJoin;
        this.followers = followers;
        this.followings = followings;
        this.description = description;
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
