package com.assessment.repository;

import com.assessment.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, String> {
    List<ClassEntity> findByLecturerId(String lecturerId);
    List<ClassEntity> findByModuleId(String moduleId);
}
