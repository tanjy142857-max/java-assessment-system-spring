package com.assessment.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACADEMIC_LEADER")
public class AcademicLeader extends User {
    private String department;

    public AcademicLeader() {}
    public AcademicLeader(String userId, String username, String password, String fullName, String email, String department, String gender, String nationality, String dob) {
        super(userId, username, password, fullName, email, gender, nationality, dob);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
