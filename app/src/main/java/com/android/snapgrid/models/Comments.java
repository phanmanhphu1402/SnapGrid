package com.android.snapgrid.models;

public class Comments {
    private String idComment;
    private String idUser;
    private String content;
    private String dateComment;
    private String imageUser;
    private String nameUser;

    public Comments() {
    }

    public Comments(String idComment, String idUser, String content, String dateComment, String imageUser, String nameUser) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.content = content;
        this.dateComment = dateComment;
        this.imageUser = imageUser;
        this.nameUser = nameUser;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
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

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}
