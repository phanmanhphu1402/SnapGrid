package com.jackphan.snapgrid.models;

public class Group {
    private int idGroup;
    private String groupName;
    private String description;
    private int idCreator;
    private String Privacy;

    public Group() {
    }

    public Group(int idGroup, String groupName, String description, int idCreator, String privacy) {
        this.idGroup = idGroup;
        this.groupName = groupName;
        this.description = description;
        this.idCreator = idCreator;
        Privacy = privacy;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public String getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(String privacy) {
        Privacy = privacy;
    }
}
