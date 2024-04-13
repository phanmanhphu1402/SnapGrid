package com.android.snapgrid.models;

public class ChatUserModel {
    private String id, idUser, name, imageURL;
    public ChatUserModel(){

    }
    public ChatUserModel(String id, String idUser, String name, String imageURL){
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
