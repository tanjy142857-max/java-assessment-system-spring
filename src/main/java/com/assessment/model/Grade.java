package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    private String gradeId;
    private String studentId;
    private String assessmentId;
    private String classId;
    private double marksObtained;
    private String letterGrade;
    private String submissionDate;

    public Grade() {}
    public Grade(String gradeId, String studentId, String assessmentId, String classId, double marksObtained, String letterGrade, String submissionDate) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.classId = classId;
        this.marksObtained = marksObtained;
        this.letterGrade = letterGrade;
        this.submissionDate = submissionDate;
    }

    public String getGradeId() { return gradeId; }
    public void setGradeId(String gradeId) { this.gradeId = gradeId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(double marksObtained) { this.marksObtained = marksObtained; }
    public String getLetterGrade() { return letterGrade; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }
    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
}
