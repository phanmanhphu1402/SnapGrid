package com.jackphan.snapgrid.models;

public class Notify {
    private String idUser;
    private String content;
    private String date;
    private String idPost;

    public Notify(String idUser, String content, String date, String idPost) {
        this.idUser = idUser;
        this.content = content;
        this.date = date;
        this.idPost = idPost;
    }

    public Notify() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }
}
