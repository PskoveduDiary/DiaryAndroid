package com.alex.materialdiary.sys.common.models.period_marks;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mark {

@SerializedName("SYS_GUID")
@Expose
private String sysGuid;
@SerializedName("DATE")
@Expose
@Nullable
private String date;
@SerializedName("LONG_NAME")
@Expose
private String longName;
@SerializedName("SHORT_NAME")
@Expose
private String shortName;
@SerializedName("VALUE")
@Expose
private Integer value;

public String getSysGuid() {
return sysGuid;
}

public void setSysGuid(String sysGuid) {
this.sysGuid = sysGuid;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getLongName() {
return longName;
}

public void setLongName(String longName) {
this.longName = longName;
}

public String getShortName() {
return shortName;
}

public void setShortName(String shortName) {
this.shortName = shortName;
}

public Integer getValue() {
return value;
}

public void setValue(Integer value) {
this.value = value;
}

}