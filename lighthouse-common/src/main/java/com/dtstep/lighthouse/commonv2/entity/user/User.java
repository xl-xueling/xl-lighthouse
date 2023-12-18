package com.dtstep.lighthouse.commonv2.entity.user;

import java.util.List;

public class User {

    private int id;

    private String userName;

    private String password;

    private List<Role> roles;

    public User(int id,String userName,String password,List<Role> roles){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
