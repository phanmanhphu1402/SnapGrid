package com.android.snapgrid.models;

public class GroupMembership {
    private int idGroup;
    private int idUser;
    private String dataJoin;
    private String role;

    public GroupMembership() {
    }

    public GroupMembership(int idGroup, int idUser, String dataJoin, String role) {
        this.idGroup = idGroup;
        this.idUser = idUser;
        this.dataJoin = dataJoin;
        this.role = role;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDataJoin() {
        return dataJoin;
    }

    public void setDataJoin(String dataJoin) {
        this.dataJoin = dataJoin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
