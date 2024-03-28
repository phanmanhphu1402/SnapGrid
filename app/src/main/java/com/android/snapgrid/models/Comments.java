package com.android.snapgrid.models;

public class Comments {
    private int idComment;
    private int idPost;
    private int idUser;
    private String content;
    private String dataComment;

    public Comments() {
    }

    public Comments(int idComment, int idPost, int idUser, String content, String dataComment) {
        this.idComment = idComment;
        this.idPost = idPost;
        this.idUser = idUser;
        this.content = content;
        this.dataComment = dataComment;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
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

    public String getDataComment() {
        return dataComment;
    }

    public void setDataComment(String dataComment) {
        this.dataComment = dataComment;
    }
}
