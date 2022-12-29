package com.alex.materialdiary.sys.common.models.diary_day;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DatumDay {

    @SerializedName("JOURNAL_SYS_GUID")
    @Expose
    private String journalSysGuid;
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
    @SerializedName("CABINET_SYS_GUID")
    @Expose
    private Object cabinetSysGuid;
    @SerializedName("CABINET_NAME")
    @Expose
    private Object cabinetName;
    @SerializedName("LESSON_SYS_GUID")
    @Expose
    private String lessonSysGuid;
    @SerializedName("LESSON_DATE")
    @Expose
    private String lessonDate;
    @SerializedName("LESSON_NUMBER")
    @Expose
    private Integer lessonNumber;
    @SerializedName("LESSON_TIME_SYS_GUID")
    @Expose
    private String lessonTimeSysGuid;
    @SerializedName("LESSON_TIME_BEGIN")
    @Expose
    private String lessonTimeBegin;
    @SerializedName("LESSON_TIME_END")
    @Expose
    private String lessonTimeEnd;
    @SerializedName("GRADE_TYPE_SYS_GUID")
    @Expose
    private Object gradeTypeSysGuid;
    @SerializedName("GRADE_TYPE_NAME")
    @Expose
    private Object gradeTypeName;
    @SerializedName("GRADE_TYPE_PERIOD")
    @Expose
    private Boolean gradeTypePeriod;
    @SerializedName("GRADE_TYPE_WEIGHT")
    @Expose
    private Integer gradeTypeWeight;
    @SerializedName("GRADE_HEAD_COMMENT")
    @Expose
    private Boolean gradeHeadComment;
    @SerializedName("TOPIC")
    @Expose
    private String topic;
    @SerializedName("HOMEWORK")
    @Expose
    private String homework;
    @SerializedName("HOMEWORK_PREVIOUS")
    @Expose
    private HomeworkPrevious homeworkPrevious;
    @SerializedName("MARKS")
    @Expose
    private List<Mark> marks = null;
    @SerializedName("ABSENCE")
    @Expose
    private List<Object> absence = null;
    @SerializedName("NOTES")
    @Expose
    private List<Object> notes = null;

    public String getJournalSysGuid() {
        return journalSysGuid;
    }

    public void setJournalSysGuid(String journalSysGuid) {
        this.journalSysGuid = journalSysGuid;
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

    public Object getCabinetSysGuid() {
        return cabinetSysGuid;
    }

    public void setCabinetSysGuid(Object cabinetSysGuid) {
        this.cabinetSysGuid = cabinetSysGuid;
    }

    public Object getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(Object cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getLessonSysGuid() {
        return lessonSysGuid;
    }

    public void setLessonSysGuid(String lessonSysGuid) {
        this.lessonSysGuid = lessonSysGuid;
    }

    public String getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(String lessonDate) {
        this.lessonDate = lessonDate;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonTimeSysGuid() {
        return lessonTimeSysGuid;
    }

    public void setLessonTimeSysGuid(String lessonTimeSysGuid) {
        this.lessonTimeSysGuid = lessonTimeSysGuid;
    }

    public String getLessonTimeBegin() {
        return lessonTimeBegin;
    }

    public void setLessonTimeBegin(String lessonTimeBegin) {
        this.lessonTimeBegin = lessonTimeBegin;
    }

    public String getLessonTimeEnd() {
        return lessonTimeEnd;
    }

    public void setLessonTimeEnd(String lessonTimeEnd) {
        this.lessonTimeEnd = lessonTimeEnd;
    }

    public Object getGradeTypeSysGuid() {
        return gradeTypeSysGuid;
    }

    public void setGradeTypeSysGuid(Object gradeTypeSysGuid) {
        this.gradeTypeSysGuid = gradeTypeSysGuid;
    }

    public Object getGradeTypeName() {
        return gradeTypeName;
    }

    public void setGradeTypeName(Object gradeTypeName) {
        this.gradeTypeName = gradeTypeName;
    }

    public Boolean getGradeTypePeriod() {
        return gradeTypePeriod;
    }

    public void setGradeTypePeriod(Boolean gradeTypePeriod) {
        this.gradeTypePeriod = gradeTypePeriod;
    }

    public Integer getGradeTypeWeight() {
        return gradeTypeWeight;
    }

    public void setGradeTypeWeight(Integer gradeTypeWeight) {
        this.gradeTypeWeight = gradeTypeWeight;
    }

    public Boolean getGradeHeadComment() {
        return gradeHeadComment;
    }

    public void setGradeHeadComment(Boolean gradeHeadComment) {
        this.gradeHeadComment = gradeHeadComment;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public HomeworkPrevious getHomeworkPrevious() {
        return homeworkPrevious;
    }

    public void setHomeworkPrevious(HomeworkPrevious homeworkPrevious) {
        this.homeworkPrevious = homeworkPrevious;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public List<Object> getAbsence() {
        return absence;
    }

    public void setAbsence(List<Object> absence) {
        this.absence = absence;
    }

    public List<Object> getNotes() {
        return notes;
    }

    public void setNotes(List<Object> notes) {
        this.notes = notes;
    }

}