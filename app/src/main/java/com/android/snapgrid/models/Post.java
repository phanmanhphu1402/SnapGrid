package com.android.snapgrid.models;

public class Post {
    private int idPost;
    private int idUser;
    private String content;
    private String datePost;
    private int numberLike;
    private int numberShare;

    public Post() {
    }

    public Post(int idPost, int idUser, String content, String datePost, int numberLike, int numberShare) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.content = content;
        this.datePost = datePost;
        this.numberLike = numberLike;
        this.numberShare = numberShare;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public int getNumberLike() {
        return numberLike;
    }

    public void setNumberLike(int numberLike) {
        this.numberLike = numberLike;
    }

    public int getNumberShare() {
        return numberShare;
    }

    public void setNumberShare(int numberShare) {
        this.numberShare = numberShare;
    }
}
