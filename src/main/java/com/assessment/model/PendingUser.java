package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pending_users")
public class PendingUser {

    @Id
    private String pendingId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String intakeCode;
    private String gender;
    private String nationality;
    private String dateOfBirth;
    private String requestDate;
    private String status = "PENDING";

    public PendingUser() {}
    public PendingUser(String pendingId, String username, String password, String fullName, String email, String intakeCode, String gender, String nationality, String dateOfBirth, String requestDate, String status) {
        this.pendingId = pendingId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.intakeCode = intakeCode;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.requestDate = requestDate;
        this.status = status;
    }

    public String getPendingId() { return pendingId; }
    public void setPendingId(String pendingId) { this.pendingId = pendingId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIntakeCode() { return intakeCode; }
    public void setIntakeCode(String intakeCode) { this.intakeCode = intakeCode; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
