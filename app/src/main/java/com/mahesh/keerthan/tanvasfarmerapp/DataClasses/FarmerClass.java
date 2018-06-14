package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.io.Serializable;

public class FarmerClass implements Serializable {

    private String u_id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String aadhar_number;
    private String address_1;
    private String address_2;
    private String gender;
    private String dob;
    private int village_id;
    private int district_id;


    public FarmerClass(String u_id, String first_name, String last_name, String phone_number, String aadhar_number, String address_1, String address_2, String gender, String dob, int village_id, int district_id) {
        this.u_id = u_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.aadhar_number = aadhar_number;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.gender = gender;
        this.dob = dob;
        this.village_id = village_id;
        this.district_id = district_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public void setVillage_id(int village_id) {
        this.village_id = village_id;
    }

    public int getVillage_id() {
        return village_id;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getU_id() {
        return u_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
