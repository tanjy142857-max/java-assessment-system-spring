package com.assessment.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LECTURER")
public class Lecturer extends User {
    private String specialization;
    private String academicLeaderId;

    public Lecturer() {}
    public Lecturer(String userId, String username, String password, String fullName, String email, String specialization, String gender, String nationality, String dob) {
        super(userId, username, password, fullName, email, gender, nationality, dob);
        this.specialization = specialization;
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getAcademicLeaderId() { return academicLeaderId; }
    public void setAcademicLeaderId(String academicLeaderId) { this.academicLeaderId = academicLeaderId; }
}
