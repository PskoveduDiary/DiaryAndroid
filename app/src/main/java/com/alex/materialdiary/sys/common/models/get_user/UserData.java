package com.alex.materialdiary.sys.common.models.get_user;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

@SerializedName("LOGIN")
@Expose
private String login;
@SerializedName("SURNAME")
@Expose
private String surname;
@SerializedName("NAME")
@Expose
private String name;
@SerializedName("SECONDNAME")
@Expose
private String secondname;
@SerializedName("EMAIL")
@Expose
private String email;
@SerializedName("CONFIRMATION")
@Expose
private String confirmation;
@SerializedName("CONFIRM_EXPIRATION")
@Expose
private Object confirmExpiration;
@SerializedName("SESSION_ID")
@Expose
private Object sessionId;
@SerializedName("SCHOOLS")
@Expose
private List<Schools> schools = null;

public String getLogin() {
return login;
}

public void setLogin(String login) {
this.login = login;
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

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getConfirmation() {
return confirmation;
}

public void setConfirmation(String confirmation) {
this.confirmation = confirmation;
}

public Object getConfirmExpiration() {
return confirmExpiration;
}

public void setConfirmExpiration(Object confirmExpiration) {
this.confirmExpiration = confirmExpiration;
}

public Object getSessionId() {
return sessionId;
}

public void setSessionId(Object sessionId) {
this.sessionId = sessionId;
}

public List<Schools> getSchools() {
return schools;
}

public void setSchools(List<Schools> schools) {
this.schools = schools;
}

}