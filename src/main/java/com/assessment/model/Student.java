package com.assessment.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    private String intakeCode;

    public Student() {}
    public Student(String userId, String username, String password, String fullName, String email, String intakeCode, String gender, String nationality, String dob) {
        super(userId, username, password, fullName, email, gender, nationality, dob);
        this.intakeCode = intakeCode;
    }

    public String getIntakeCode() { return intakeCode; }
    public void setIntakeCode(String intakeCode) { this.intakeCode = intakeCode; }
}
