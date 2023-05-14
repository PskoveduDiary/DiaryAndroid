package com.alex.materialdiary.sys.common.models.get_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Grade {

@SerializedName("SYS_GUID")
@Expose
private String sysGuid;
@SerializedName("NAME")
@Expose
private String name;
@SerializedName("SCHOOL")
@Expose
private SchoolInfo school;
@SerializedName("GRADE_HEAD")
@Expose
private GradeHead gradeHead;

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

public SchoolInfo getSchool() {
return school;
}

public void setSchool(SchoolInfo school) {
this.school = school;
}

public GradeHead getGradeHead() {
return gradeHead;
}

public void setGradeHead(GradeHead gradeHead) {
this.gradeHead = gradeHead;
}

}