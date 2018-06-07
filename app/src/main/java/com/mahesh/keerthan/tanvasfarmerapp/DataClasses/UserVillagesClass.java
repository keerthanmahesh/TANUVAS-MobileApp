package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

public class UserVillagesClass {
    private int s_no;
    private int u_id;
    private int village_id;
    private int district_id;


    public UserVillagesClass(int s_no, int u_id, int village_id, int district_id) {
        this.s_no = s_no;
        this.u_id = u_id;
        this.village_id = village_id;
        this.district_id = district_id;
    }

    public int getVillage_id() {
        return village_id;
    }

    public int getU_id() {
        return u_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public int getS_no() {
        return s_no;
    }

    public void setVillage_id(int village_id) {
        this.village_id = village_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public void setS_no(int s_no) {
        this.s_no = s_no;
    }
}
