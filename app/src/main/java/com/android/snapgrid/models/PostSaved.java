package com.android.snapgrid.models;


import java.util.Date;

public class PostSaved {

    public String postId;

    public Date createdAt;

    public PostSaved() {
    }

    public PostSaved(String postId, Date createdAt, boolean saved) {
        this.postId = postId;
        this.createdAt = createdAt;
        this.saved = saved;
    }

    private boolean saved;

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
