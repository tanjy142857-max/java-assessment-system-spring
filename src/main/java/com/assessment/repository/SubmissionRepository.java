package com.assessment.repository;

import com.assessment.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findByStudentId(String studentId);
    List<Submission> findByClassId(String classId);
    List<Submission> findByAssessmentId(String assessmentId);
    Optional<Submission> findByStudentIdAndAssessmentIdAndClassId(String studentId, String assessmentId, String classId);
    boolean existsByStudentIdAndAssessmentId(String studentId, String assessmentId);
}
