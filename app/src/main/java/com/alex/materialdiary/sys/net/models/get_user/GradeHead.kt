package com.alex.materialdiary.sys.common.models.get_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GradeHead {

@SerializedName("SYS_GUID")
@Expose
private String sysGuid;
@SerializedName("SURNAME")
@Expose
private String surname;
@SerializedName("NAME")
@Expose
private String name;
@SerializedName("SECONDNAME")
@Expose
private String secondname;
@SerializedName("SEX")
@Expose
private String sex;
@SerializedName("SCHOOL")
@Expose
private SchoolInfo school;

public String getSysGuid() {
return sysGuid;
}

public void setSysGuid(String sysGuid) {
this.sysGuid = sysGuid;
}

public String getSurname() {
return surname;
}

public void setSurname(String surname) {
this.surname = surname;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getSecondname() {
return secondname;
}

public void setSecondname(String secondname) {
this.secondname = secondname;
}

public String getSex() {
return sex;
}

public void setSex(String sex) {
this.sex = sex;
}

public SchoolInfo getSchool() {
return school;
}

public void setSchool(SchoolInfo school) {
this.school = school;
}

}