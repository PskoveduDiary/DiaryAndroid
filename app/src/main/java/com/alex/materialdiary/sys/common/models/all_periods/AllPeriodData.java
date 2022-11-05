package com.alex.materialdiary.sys.common.models.all_periods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllPeriodData {

@SerializedName("SYS_GUID")
@Expose
private String sysGuid;
@SerializedName("PERIOD_GUID")
@Expose
private String periodGuid;
@SerializedName("NAME")
@Expose
private String name;
@SerializedName("DATE_BEGIN")
@Expose
private String dateBegin;
@SerializedName("DATE_END")
@Expose
private String dateEnd;
@SerializedName("GRADE_TYPE_GUID")
@Expose
private String gradeTypeGuid;

public String getSysGuid() {
return sysGuid;
}

public void setSysGuid(String sysGuid) {
this.sysGuid = sysGuid;
}

public String getPeriodGuid() {
return periodGuid;
}

public void setPeriodGuid(String periodGuid) {
this.periodGuid = periodGuid;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getDateBegin() {
return dateBegin;
}

public void setDateBegin(String dateBegin) {
this.dateBegin = dateBegin;
}

public String getDateEnd() {
return dateEnd;
}

public void setDateEnd(String dateEnd) {
this.dateEnd = dateEnd;
}

public String getGradeTypeGuid() {
return gradeTypeGuid;
}

public void setGradeTypeGuid(String gradeTypeGuid) {
this.gradeTypeGuid = gradeTypeGuid;
}

}