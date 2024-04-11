package com.android.snapgrid.models;

public class Post {
    private int idPost;
    private String idUser;
    private String content;
    private String datePost;
    private int numberLike;
    private int numberShare;
    private String imageUrl;
    private String title;

    public Post(int idPost, String idUser, String content, String datePost, int numberLike, int numberShare, String imageUrl, String title) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.content = content;
        this.datePost = datePost;
        this.numberLike = 0;
        this.numberShare = 0;
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Post() {
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
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
