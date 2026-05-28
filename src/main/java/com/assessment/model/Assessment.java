package com.assessment.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    private String assessmentId;
    private String moduleId;
    private String assessmentName;
    private String assessmentType;
    private double weightage;
    private double totalMarks;
    private String dueDate;
    private String dueTime;

    public Assessment() {}
    public Assessment(String assessmentId, String moduleId, String assessmentName, String assessmentType, double weightage, double totalMarks) {
        this.assessmentId = assessmentId;
        this.moduleId = moduleId;
        this.assessmentName = assessmentName;
        this.assessmentType = assessmentType;
        this.weightage = weightage;
        this.totalMarks = totalMarks;
    }

    public boolean isOverdue() {
        if (dueDate == null) return false;
        try { return LocalDate.parse(dueDate).isBefore(LocalDate.now()); }
        catch (Exception e) { return false; }
    }

    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
    public String getAssessmentName() { return assessmentName; }
    public void setAssessmentName(String assessmentName) { this.assessmentName = assessmentName; }
    public String getAssessmentType() { return assessmentType; }
    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }
    public double getWeightage() { return weightage; }
    public void setWeightage(double weightage) { this.weightage = weightage; }
    public double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(double totalMarks) { this.totalMarks = totalMarks; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getDueTime() { return dueTime; }
    public void setDueTime(String dueTime) { this.dueTime = dueTime; }
}
