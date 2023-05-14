package com.alex.materialdiary.sys.common.models.get_user;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schools {

@SerializedName("ROLES")
@Expose
private List<String> roles = null;
@SerializedName("SCHOOL")
@Expose
private SchoolInfo school;
@SerializedName("GOVERNMENT")
@Expose
private Object government;
@SerializedName("TEACHER")
@Expose
private Object teacher;
@SerializedName("PARENT")
@Expose
private Parent parent;
@SerializedName("PARTICIPANT")
@Expose
private Participant participant;
@SerializedName("USER_GRADES")
@Expose
private List<Object> userGrades = null;
@SerializedName("USER_PARTICIPANTS")
@Expose
private List<Participant> userParticipants = null;
@SerializedName("USER_PARENTS")
@Expose
private List<Parent> userParents = null;

public List<String> getRoles() {
return roles;
}

public void setRoles(List<String> roles) {
this.roles = roles;
}

public SchoolInfo getSchool() {
return school;
}

public void setSchool(SchoolInfo school) {
this.school = school;
}

public Object getGovernment() {
return government;
}

public void setGovernment(Object government) {
this.government = government;
}

public Object getTeacher() {
return teacher;
}

public void setTeacher(Object teacher) {
this.teacher = teacher;
}

public Parent getParent() {
return parent;
}

public void setParent(Parent parent) {
this.parent = parent;
}

public Participant getParticipant() {
return participant;
}

public void setParticipant(Participant participant) {
this.participant = participant;
}

public List<Object> getUserGrades() {
return userGrades;
}

public void setUserGrades(List<Object> userGrades) {
this.userGrades = userGrades;
}

public List<Participant> getUserParticipants() {
return userParticipants;
}

public void setUserParticipants(List<Participant> userParticipants) {
this.userParticipants = userParticipants;
}

public List<Parent> getUserParents() {
return userParents;
}

public void setUserParents(List<Parent> userParents) {
this.userParents = userParents;
}

}