package com.assessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grading_system")
public class GradeLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;
    private int minMark;
    private int maxMark;
    private String description;

    public GradeLevel() {}
    public GradeLevel(String grade, int minMark, int maxMark, String description) {
        this.grade = grade;
        this.minMark = minMark;
        this.maxMark = maxMark;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getMinMark() { return minMark; }
    public void setMinMark(int minMark) { this.minMark = minMark; }
    public int getMaxMark() { return maxMark; }
    public void setMaxMark(int maxMark) { this.maxMark = maxMark; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
