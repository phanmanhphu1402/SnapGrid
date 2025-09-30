package com.jackphan.snapgrid.models;

public class Follows {
    private int idFollower;
    private int idFlowing;
    private int dateFollow;

    public Follows() {
    }

    public Follows(int idFollower, int idFlowing, int dateFollow) {
        this.idFollower = idFollower;
        this.idFlowing = idFlowing;
        this.dateFollow = dateFollow;
    }

    public int getIdFollower() {
        return idFollower;
    }

    public void setIdFollower(int idFollower) {
        this.idFollower = idFollower;
    }

    public int getIdFlowing() {
        return idFlowing;
    }

    public void setIdFlowing(int idFlowing) {
        this.idFlowing = idFlowing;
    }

    public int getDateFollow() {
        return dateFollow;
    }

    public void setDateFollow(int dateFollow) {
        this.dateFollow = dateFollow;
    }
}
