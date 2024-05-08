package com.android.snapgrid.models;

import com.android.snapgrid.adapters.CommentAdapter;

import java.util.ArrayList;

public class Post {
    private String idPost;
    private String idUser;
    private String content;
    private String datePost;
    private int numberLike;
    private int numberComment;

    private ArrayList<Comments> commentsArrayList;
    private String imageUrl;
    private String title;
    private String tag;

    public Post(String idPost, String idUser, String content, String datePost, int numberLike, int numberComment, ArrayList<Comments> commentsArrayList, String imageUrl, String title, String tag) {
        this.idPost = idPost;
        this.idUser = idUser;
        this.content = content;
        this.datePost = datePost;
        this.numberLike = numberLike;
        this.numberComment = commentsArrayList.size();
        this.commentsArrayList = commentsArrayList;
        this.imageUrl = imageUrl;
        this.title = title;
        this.tag = tag;
    }

    public Post() {
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
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

    public int getNumberComment() {
        return numberComment;
    }

    public void setNumberComment(int numberComment) {
        this.numberComment = numberComment;
    }

    public ArrayList<Comments> getCommentsArrayList() {
        return commentsArrayList;
    }

    public void setCommentsArrayList(ArrayList<Comments> commentsArrayList) {
        this.commentsArrayList = commentsArrayList;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
