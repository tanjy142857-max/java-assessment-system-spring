package com.assessment.repository;

import com.assessment.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByClassId(String classId);
    List<Feedback> findByAssessmentId(String assessmentId);
    List<Feedback> findByStudentId(String studentId);
    Optional<Feedback> findByStudentIdAndAssessmentIdAndClassId(String studentId, String assessmentId, String classId);
}
