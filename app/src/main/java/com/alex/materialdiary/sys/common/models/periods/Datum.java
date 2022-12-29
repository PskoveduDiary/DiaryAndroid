package com.alex.materialdiary.sys.common.models.periods;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

@SerializedName("SYS_GUID")
@Expose
private String sysGuid;
@SerializedName("NAME")
@Expose
private String name;
@SerializedName("PERIODS")
@Expose
private List<Period> periods = null;

public String getSysGuid() {
return sysGuid;
}

public void setSysGuid(String sysGuid) {
this.sysGuid = sysGuid;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public List<Period> getPeriods() {
return periods;
}

public void setPeriods(List<Period> periods) {
this.periods = periods;
}

}