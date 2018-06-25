package com.jbb.mgt.core.domain;

import java.util.Arrays;

public class Roles {
    private Integer roleId;
    private String description;
    private int[] upAccounts;
    private int[] downAccounts;
    
    public int[] getUpAccounts() {
        return upAccounts;
    }

    public void setUpAccounts(int[] upAccounts) {
        this.upAccounts = upAccounts;
    }

    public int[] getDownAccounts() {
        return downAccounts;
    }

    public void setDownAccounts(int[] downAccounts) {
        this.downAccounts = downAccounts;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        return "Roles [roleId=" + roleId + ", description=" + description + "]";
    }

}
