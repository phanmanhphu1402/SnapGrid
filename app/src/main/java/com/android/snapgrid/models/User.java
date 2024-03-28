package com.android.snapgrid.models;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String dateOfBirth;
    private int gender;
    private String address;
    private String avatar;
    private String dataJoin;
    private int status;

    public User() {

    }

    public User(int id, String name, String email, String password, String dateOfBirth, int gender, String address, String avatar, String dataJoin, int status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.avatar = avatar;
        this.dataJoin = dataJoin;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
