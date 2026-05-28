package com.assessment.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN_STAFF")
public class AdminStaff extends User {
    public AdminStaff() {}
    public AdminStaff(String userId, String username, String password, String fullName, String email, String gender, String nationality, String dob) {
        super(userId, username, password, fullName, email, gender, nationality, dob);
    }
}
