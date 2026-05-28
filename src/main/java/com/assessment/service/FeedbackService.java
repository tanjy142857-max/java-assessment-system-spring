package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getFeedbackByStudent(String studentId) { return feedbackRepository.findByStudentId(studentId); }
    public List<Feedback> getFeedbackByClass(String classId) { return feedbackRepository.findByClassId(classId); }
    public Feedback getFeedback(String studentId, String assessmentId, String classId) {
        return feedbackRepository.findByStudentIdAndAssessmentIdAndClassId(studentId, assessmentId, classId).orElse(null);
    }

    public Feedback saveFeedback(Feedback feedback) { return feedbackRepository.save(feedback); }

    public String generateFeedbackId() {
        return "FDB" + String.format("%03d", feedbackRepository.count() + 1);
    }
}
