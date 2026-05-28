package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ClassRepository classRepository;
    private final AssessmentRepository assessmentRepository;
    private final GradeRepository gradeRepository;
    private final SubmissionRepository submissionRepository;
    private final FeedbackRepository feedbackRepository;

    public ModuleService(ModuleRepository moduleRepository, ClassRepository classRepository, AssessmentRepository assessmentRepository, GradeRepository gradeRepository, SubmissionRepository submissionRepository, FeedbackRepository feedbackRepository) {
        this.moduleRepository = moduleRepository;
        this.classRepository = classRepository;
        this.assessmentRepository = assessmentRepository;
        this.gradeRepository = gradeRepository;
        this.submissionRepository = submissionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public List<CourseModule> getAllModules() { return moduleRepository.findAll(); }
    public List<CourseModule> getModulesByAcademicLeader(String alId) { return moduleRepository.findByAcademicLeaderId(alId); }
    public CourseModule getModuleById(String id) { return moduleRepository.findById(id).orElse(null); }

    public CourseModule createModule(CourseModule module) {
        if (moduleRepository.existsByModuleCode(module.getModuleCode())) return null;
        return moduleRepository.save(module);
    }

    public CourseModule updateModule(CourseModule module) { return moduleRepository.save(module); }

    @Transactional
    public void deleteModule(String moduleId) {
        // 级联删除：classes → grades/submissions/feedback → assessments
        List<ClassEntity> classes = classRepository.findByModuleId(moduleId);
        for (ClassEntity cls : classes) {
            gradeRepository.findByClassId(cls.getClassId()).forEach(g -> gradeRepository.delete(g));
            submissionRepository.findByClassId(cls.getClassId()).forEach(s -> submissionRepository.delete(s));
            feedbackRepository.findByClassId(cls.getClassId()).forEach(f -> feedbackRepository.delete(f));
            classRepository.delete(cls);
        }
        List<Assessment> assessments = assessmentRepository.findByModuleId(moduleId);
        for (Assessment a : assessments) {
            gradeRepository.findByAssessmentId(a.getAssessmentId()).forEach(g -> gradeRepository.delete(g));
            submissionRepository.findByAssessmentId(a.getAssessmentId()).forEach(s -> submissionRepository.delete(s));
            feedbackRepository.findByAssessmentId(a.getAssessmentId()).forEach(f -> feedbackRepository.delete(f));
            assessmentRepository.delete(a);
        }
        moduleRepository.deleteById(moduleId);
    }

    public String generateModuleId() {
        long count = moduleRepository.count();
        return "MOD" + String.format("%03d", count + 1);
    }
}
