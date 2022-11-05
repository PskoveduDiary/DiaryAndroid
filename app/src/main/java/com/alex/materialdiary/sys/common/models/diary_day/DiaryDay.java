package com.alex.materialdiary.sys.common.models.diary_day;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiaryDay {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("system")
    @Expose
    private Boolean system;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<DatumDay> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DatumDay> getData() {
        return data;
    }

    public void setData(List<DatumDay> data) {
        this.data = data;
    }

}
