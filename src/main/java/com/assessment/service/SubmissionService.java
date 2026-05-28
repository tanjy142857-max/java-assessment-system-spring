package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public List<Submission> getSubmissionsByStudent(String studentId) { return submissionRepository.findByStudentId(studentId); }
    public List<Submission> getSubmissionsByClass(String classId) { return submissionRepository.findByClassId(classId); }

    public Submission createSubmission(Submission submission) { return submissionRepository.save(submission); }
    public Submission updateSubmission(Submission submission) { return submissionRepository.save(submission); }

    public boolean hasSubmission(String studentId, String assessmentId) {
        return submissionRepository.existsByStudentIdAndAssessmentId(studentId, assessmentId);
    }

    public String generateSubmissionId() {
        return "SUB" + String.format("%03d", submissionRepository.count() + 1);
    }
}
