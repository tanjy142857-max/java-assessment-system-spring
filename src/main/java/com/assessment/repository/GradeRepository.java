package com.assessment.repository;

import com.assessment.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, String> {
    List<Grade> findByStudentId(String studentId);
    List<Grade> findByClassId(String classId);
    List<Grade> findByAssessmentId(String assessmentId);
    Optional<Grade> findByStudentIdAndAssessmentIdAndClassId(String studentId, String assessmentId, String classId);
}
