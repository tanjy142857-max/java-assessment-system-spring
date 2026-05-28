package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modules")
public class CourseModule {

    @Id
    private String moduleId;

    @Column(unique = true)
    private String moduleCode;

    private String moduleName;
    private String description;
    private String academicLeaderId;

    public CourseModule() {}
    public CourseModule(String moduleId, String moduleCode, String moduleName, String description, String academicLeaderId) {
        this.moduleId = moduleId;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.description = description;
        this.academicLeaderId = academicLeaderId;
    }

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAcademicLeaderId() { return academicLeaderId; }
    public void setAcademicLeaderId(String academicLeaderId) { this.academicLeaderId = academicLeaderId; }
}
