package com.alex.materialdiary.sys.common.models.periods;

import com.alex.materialdiary.sys.common.models.period_marks.Mark;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period {

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
private Integer gradeTypeGuid;
@SerializedName("MARK")
@Expose
private Mark mark;
@SerializedName("AVERAGE")
@Expose
private Double average;
@SerializedName("COUNT")
@Expose
private Integer count;

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

public Integer getGradeTypeGuid() {
return gradeTypeGuid;
}

public void setGradeTypeGuid(Integer gradeTypeGuid) {
this.gradeTypeGuid = gradeTypeGuid;
}

public Mark getMark() {
return mark;
}

public void setMark(Mark mark) {
this.mark = mark;
}

public Double getAverage() {
return average;
}

public void setAverage(Double average) {
this.average = average;
}

public Integer getCount() {
return count;
}

public void setCount(Integer count) {
this.count = count;
}

}