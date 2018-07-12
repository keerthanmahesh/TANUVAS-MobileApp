package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.io.Serializable;

public class UserClass implements Serializable {

    private int u_id;
    private String username;
    private String password;
    private String fullname;
    private int district_id;
    private String phone_number;
    private int isSuperAdmin;

    public UserClass(int u_id, String username, String password, String fullname, int district_id, String phone_number, int isSuperAdmin) {
        this.u_id = u_id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.district_id = district_id;
        this.phone_number = phone_number;
        this.isSuperAdmin = isSuperAdmin;
    }

    public int getU_id(){
        return u_id;
    }

    public String getFullname() {
        return fullname;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(int isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}
