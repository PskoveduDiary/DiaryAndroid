package com.alex.materialdiary.sys.common.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PDBody {

    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("appid")
    @Expose
    private String appid = "Образование Псковской области";
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sysguid")
    @Expose
    private String sysguid;
    @SerializedName("pdakey")
    @Expose
    private String pdakey;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysguid() {
        return sysguid;
    }

    public void setSysguid(String sysguid) {
        this.sysguid = sysguid;
    }

    public String getPdakey() {
        return pdakey;
    }

    public void setPdakey(String pdakey) {
        this.pdakey = pdakey;
    }

}