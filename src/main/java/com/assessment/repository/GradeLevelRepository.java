package com.assessment.repository;

import com.assessment.model.GradeLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeLevelRepository extends JpaRepository<GradeLevel, Long> {
}
