package com.alex.materialdiary.sys.common.models.period_marks;

import java.text.DecimalFormat;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeriodMarksData {

@SerializedName("JOURNAL_SYS_GUID")
@Expose
private String journalSysGuid;
@SerializedName("JOURNAL_NAME")
@Expose
private String journalName;
@SerializedName("SUBJECT_SYS_GUID")
@Expose
private String subjectSysGuid;
@SerializedName("SUBJECT_NAME")
@Expose
private String subjectName;
@SerializedName("TEACHER_SYS_GUID")
@Expose
private String teacherSysGuid;
@SerializedName("TEACHER_NAME")
@Expose
private String teacherName;
@SerializedName("TEACHER_NAME_SHORT")
@Expose
private String teacherNameShort;
@SerializedName("HIDE_IN_REPORTS")
@Expose
private Boolean hideInReports;
@SerializedName("HIDE_IN_SCHEDULE")
@Expose
private Boolean hideInSchedule;
@SerializedName("MARKS")
@Expose
private List<Mark> marks = null;

public String getJournalSysGuid() {
return journalSysGuid;
}

public void setJournalSysGuid(String journalSysGuid) {
this.journalSysGuid = journalSysGuid;
}

public String getJournalName() {
return journalName;
}

public void setJournalName(String journalName) {
this.journalName = journalName;
}

public String getSubjectSysGuid() {
return subjectSysGuid;
}

public void setSubjectSysGuid(String subjectSysGuid) {
this.subjectSysGuid = subjectSysGuid;
}

public String getSubjectName() {
return subjectName;
}

public void setSubjectName(String subjectName) {
this.subjectName = subjectName;
}

public String getTeacherSysGuid() {
return teacherSysGuid;
}

public void setTeacherSysGuid(String teacherSysGuid) {
this.teacherSysGuid = teacherSysGuid;
}

public String getTeacherName() {
return teacherName;
}

public void setTeacherName(String teacherName) {
this.teacherName = teacherName;
}

public String getTeacherNameShort() {
return teacherNameShort;
}

public void setTeacherNameShort(String teacherNameShort) {
this.teacherNameShort = teacherNameShort;
}

public Boolean getHideInReports() {
return hideInReports;
}

public void setHideInReports(Boolean hideInReports) {
this.hideInReports = hideInReports;
}

public Boolean getHideInSchedule() {
return hideInSchedule;
}

public void setHideInSchedule(Boolean hideInSchedule) {
this.hideInSchedule = hideInSchedule;
}

public List<Mark> getMarks() {
return marks;
}

public String getAverage() {
        if (marks.size() == 0) return "Нет оценок";
        int com = 0;
        for (int i = 0; i < marks.size(); i++ ){
            com += marks.get(i).getValue();
        }
        return "Средний бал: " + new DecimalFormat("#0.00").format((float) com / marks.size());
    }
public String get_to_five(){
    if (marks.size() == 0) return "3";
    int sum = 0;
    int added = 0;
    for (int i = 0; i < marks.size(); i++ ){
        sum += marks.get(i).getValue();
    }
    float average = (float) sum / marks.size();
    if (average >= 4.59) return "Уже 5";
    while (average < 4.6){
        added += 1;
        sum += 5;
        average = (float) sum / (marks.size() + added);
        if (added >= 40){
            return "Много";
        }
    }
    return String.valueOf(added);
}

public String get_four_to_four(){
    if (marks.size() == 0) return "3";
    int sum = 0;
    int added = 0;
    for (int i = 0; i < marks.size(); i++ ){
        sum += marks.get(i).getValue();
    }
    float average = (float) sum / marks.size();
    if (average >= 4.59) return "Уже 5";
    if (average >= 3.59) return "Уже 4";
    while (average < 3.6){
        added += 1;
        sum += 4;
        average = (float) sum / (marks.size() + added);
        if (added >= 40){
            return "Много";
        }
    }
    return String.valueOf(added);
}
public String get_five_to_four(){
    if (marks.size() == 0) return "3";
    int sum = 0;
    int added = 0;
    for (int i = 0; i < marks.size(); i++ ){
        sum += marks.get(i).getValue();
    }
    float average = (float) sum / marks.size();
    if (average >= 4.59) return "Уже 5";
    if (average >= 3.59) return "Уже 4";
    while (average < 3.6){
        added += 1;
        sum += 5;
        average = (float) sum / (marks.size() + added);
        if (added >= 40){
            return "Много";
        }
    }
    return String.valueOf(added);
}
public void setMarks(List<Mark> marks) {
this.marks = marks;
}

}