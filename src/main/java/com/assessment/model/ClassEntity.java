package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    private String classId;
    private String className;
    private String moduleId;
    private String lecturerId;
    private String semester;

    public ClassEntity() {}
    public ClassEntity(String classId, String className, String moduleId, String lecturerId, String semester) {
        this.classId = classId;
        this.className = className;
        this.moduleId = moduleId;
        this.lecturerId = lecturerId;
        this.semester = semester;
    }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
