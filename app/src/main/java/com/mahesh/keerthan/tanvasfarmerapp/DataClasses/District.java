package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.io.Serializable;

public class District implements Serializable {

    private int district_id;
    private String en_district_name;

    public District(int district_id, String en_district_name) {
        this.district_id = district_id;
        this.en_district_name = en_district_name;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public String getEn_district_name() {
        return en_district_name;
    }

    public void setEn_district_name(String en_district_name) {
        this.en_district_name = en_district_name;
    }
}
