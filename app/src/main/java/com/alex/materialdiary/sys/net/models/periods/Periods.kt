package com.alex.materialdiary.sys.common.models.periods;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Periods {

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
private List<Datum> data = null;

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

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

}
