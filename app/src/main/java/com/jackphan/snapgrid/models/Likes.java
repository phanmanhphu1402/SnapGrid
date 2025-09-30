package com.jackphan.snapgrid.models;

public class Likes {
    private int idUser;
    private int idTarget;
    private String targetType;
    private String dataLike;

    public Likes() {
    }

    public Likes(int idUser, int idTarget, String targetType, String dataLike) {
        this.idUser = idUser;
        this.idTarget = idTarget;
        this.targetType = targetType;
        this.dataLike = dataLike;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getDataLike() {
        return dataLike;
    }

    public void setDataLike(String dataLike) {
        this.dataLike = dataLike;
    }
}
