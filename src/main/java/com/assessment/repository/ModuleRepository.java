package com.assessment.repository;

import com.assessment.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModuleRepository extends JpaRepository<CourseModule, String> {
    List<CourseModule> findByAcademicLeaderId(String academicLeaderId);
    boolean existsByModuleCode(String moduleCode);
}
