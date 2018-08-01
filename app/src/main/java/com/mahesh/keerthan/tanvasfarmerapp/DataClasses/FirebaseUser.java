package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.io.Serializable;

public class FirebaseUser implements Serializable {

    private String fullname;
    private int district_id;
    private long phone_number;
    private int isSuperAdmin;

    public FirebaseUser( String fullname, int district_id, long phone_number, int isSuperAdmin) {
        this.fullname = fullname;
        this.district_id = district_id;
        this.phone_number = phone_number;
        this.isSuperAdmin = isSuperAdmin;
    }
    public FirebaseUser(){

    }


    public String getFullname() {
        return fullname;
    }

    public int getDistrict_id() {
        return district_id;
    }


    public long getPhone_number() {
        return phone_number;
    }


    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public int getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(int isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }
}
