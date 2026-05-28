package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    private String submissionId;
    private String studentId;
    private String assessmentId;
    private String classId;
    private String status = "Pending";
    private String submissionDate;
    private String notes;

    public Submission() {}
    public Submission(String submissionId, String studentId, String assessmentId, String classId, String status, String submissionDate, String notes) {
        this.submissionId = submissionId;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.classId = classId;
        this.status = status;
        this.submissionDate = submissionDate;
        this.notes = notes;
    }

    public String getSubmissionId() { return submissionId; }
    public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
