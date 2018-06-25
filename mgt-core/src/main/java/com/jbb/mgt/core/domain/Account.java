package com.jbb.mgt.core.domain;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class Account {

    private int accountId;
    private String username;
    private String nickname;
    private String phoneNumber;
    private Integer jbbUserId;
    private String password;
    private int orgId;
    private int creator;
    private Timestamp creationDate;
    private boolean deleted;
    private boolean freeze;
    private AccountKey key;
    private List<Roles> roles;
    // private int[] roles;
    private int[] permissions;
    
    private Organization organization;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getJbbUserId() {
        return jbbUserId;
    }

    public void setJbbUserId(Integer jbbUserId) {
        this.jbbUserId = jbbUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public AccountKey getKey() {
        return key;
    }

    public void setKey(AccountKey key) {
        this.key = key;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public int[] getPermissions() {
        return permissions;
    }

    public void setPermissions(int[] permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", username=" + username + ", nickname=" + nickname
            + ", phoneNumber=" + phoneNumber + ", jbbUserId=" + jbbUserId + ", password=" + password + ", orgId="
            + orgId + ", creator=" + creator + ", creationDate=" + creationDate + ", deleted=" + deleted + ", freeze="
            + freeze + ", key=" + key + ", roles=" + roles + ", permissions=" + Arrays.toString(permissions) + "]";
    }

}
