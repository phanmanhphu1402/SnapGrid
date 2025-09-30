package com.jackphan.snapgrid.models;

public class Friendship {
    private int idUser1;
    private int idUser2;
    private String status;
    private String datFriendship;

    public Friendship() {
    }

    public Friendship(int idUser1, int idUser2, String status, String datFriendship) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.status = status;
        this.datFriendship = datFriendship;
    }

    public int getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(int idUser1) {
        this.idUser1 = idUser1;
    }

    public int getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(int idUser2) {
        this.idUser2 = idUser2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatFriendship() {
        return datFriendship;
    }

    public void setDatFriendship(String datFriendship) {
        this.datFriendship = datFriendship;
    }
}
