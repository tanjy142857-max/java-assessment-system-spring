package com.assessment.repository;

import com.assessment.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, String> {
    List<Assessment> findByModuleId(String moduleId);
}
