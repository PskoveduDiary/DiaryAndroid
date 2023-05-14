package com.alex.materialdiary.sys.common.models.get_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolInfo {

@SerializedName("SYS_GUID")
@Expose
private String sysGuid;
@SerializedName("ID")
@Expose
private Integer id;
@SerializedName("NAME")
@Expose
private String name;
@SerializedName("SHORT_NAME")
@Expose
private String shortName;

public String getSysGuid() {
return sysGuid;
}

public void setSysGuid(String sysGuid) {
this.sysGuid = sysGuid;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getShortName() {
return shortName;
}

public void setShortName(String shortName) {
this.shortName = shortName;
}

}