 package com.jbb.server.core.domain;

 public class Role {
     
     /** can perform granted operations on any user object */
     public static final int PERMISSION_ACCESS_ALL = 0;
     /** grant roles */
     public static final int PERMISSION_GRANT_ROLE = 1;
     /** can use get recommand users */
     public static final int PERMISSION_GET_RECOMMAND_USERS = 2;
     /** can use statictic users */
     public static final int PERMISSION_STATISTIC_USERS = 3;

     private int roleId;
     private String name;
     private String description;
    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
     
     
     
}
