package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final GradeRepository gradeRepository;
    private final SubmissionRepository submissionRepository;
    private final FeedbackRepository feedbackRepository;

    public AssessmentService(AssessmentRepository assessmentRepository, GradeRepository gradeRepository, SubmissionRepository submissionRepository, FeedbackRepository feedbackRepository) {
        this.assessmentRepository = assessmentRepository;
        this.gradeRepository = gradeRepository;
        this.submissionRepository = submissionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public List<Assessment> getAllAssessments() { return assessmentRepository.findAll(); }
    public List<Assessment> getAssessmentsByModule(String moduleId) { return assessmentRepository.findByModuleId(moduleId); }
    public Assessment getAssessmentById(String id) { return assessmentRepository.findById(id).orElse(null); }

    public Assessment createAssessment(Assessment assessment) {
        // 同一模块的所有评估权重总和不能超过 100
        double totalWeight = getTotalWeightageForModule(assessment.getModuleId());
        if (totalWeight + assessment.getWeightage() > 100) return null;
        return assessmentRepository.save(assessment);
    }

    public Assessment updateAssessment(Assessment assessment) { return assessmentRepository.save(assessment); }

    @Transactional
    public void deleteAssessment(String assessmentId) {
        gradeRepository.findByAssessmentId(assessmentId).forEach(g -> gradeRepository.delete(g));
        submissionRepository.findByAssessmentId(assessmentId).forEach(s -> submissionRepository.delete(s));
        feedbackRepository.findByAssessmentId(assessmentId).forEach(f -> feedbackRepository.delete(f));
        assessmentRepository.deleteById(assessmentId);
    }

    public double getTotalWeightageForModule(String moduleId) {
        return assessmentRepository.findByModuleId(moduleId).stream()
            .mapToDouble(Assessment::getWeightage).sum();
    }

    public String generateAssessmentId() {
        return "ASS" + String.format("%03d", assessmentRepository.count() + 1);
    }
}
