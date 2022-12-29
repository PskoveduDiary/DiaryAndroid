package com.alex.materialdiary.sys.common.models.diary_day;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeworkPrevious {

    @SerializedName("DATE")
    @Expose
    private String date;
    @SerializedName("HOMEWORK")
    @Expose
    private String homework;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

}