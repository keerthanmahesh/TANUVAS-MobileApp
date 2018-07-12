package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.io.Serializable;

public class Villages implements Serializable {
    private int village_id;
    private int district_id;
    private String en_village_name;
    private int allocated;
    private String u_id;
    private double latitude,longitude;

    public Villages(int village_id, int district_id, String en_village_name, int allocated, String u_id) {
        this.village_id = village_id;
        this.district_id = district_id;
        this.en_village_name = en_village_name;
        this.allocated = allocated;
        this.u_id = u_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public void setAllocated(int allocated) {
        this.allocated = allocated;
    }

    public void setEn_village_name(String en_village_name) {
        this.en_village_name = en_village_name;
    }

    public void setVillage_id(int village_id) {
        this.village_id = village_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public int getAllocated() {
        return allocated;
    }

    public String getU_id() {
        return u_id;
    }

    public int getVillage_id() {
        return village_id;
    }

    public String getEn_village_name() {
        return en_village_name;
    }

}
