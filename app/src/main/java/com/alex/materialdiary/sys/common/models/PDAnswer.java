package com.alex.materialdiary.sys.common.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PDAnswer {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pdakey")
    @Expose
    private String pdakey;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPdakey() {
        return pdakey;
    }

    public void setPdakey(String pdakey) {
        this.pdakey = pdakey;
    }


}