package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    private String feedbackId;
    private String studentId;
    private String assessmentId;
    private String classId;
    private String lecturerId;

    @Column(length = 2000)
    private String feedbackText;

    private String feedbackDate;

    @Column(length = 2000)
    private String studentComment;

    public Feedback() {}
    public Feedback(String feedbackId, String studentId, String assessmentId, String classId, String lecturerId, String feedbackText, String feedbackDate) {
        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.classId = classId;
        this.lecturerId = lecturerId;
        this.feedbackText = feedbackText;
        this.feedbackDate = feedbackDate;
    }

    public String getFeedbackId() { return feedbackId; }
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getAssessmentId() { return assessmentId; }
    public void setAssessmentId(String assessmentId) { this.assessmentId = assessmentId; }
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }
    public String getFeedbackText() { return feedbackText; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
    public String getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(String feedbackDate) { this.feedbackDate = feedbackDate; }
    public String getStudentComment() { return studentComment; }
    public void setStudentComment(String studentComment) { this.studentComment = studentComment; }
}
