package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final GradeRepository gradeRepository;
    private final SubmissionRepository submissionRepository;
    private final FeedbackRepository feedbackRepository;

    public ClassService(ClassRepository classRepository, GradeRepository gradeRepository, SubmissionRepository submissionRepository, FeedbackRepository feedbackRepository) {
        this.classRepository = classRepository;
        this.gradeRepository = gradeRepository;
        this.submissionRepository = submissionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public List<ClassEntity> getAllClasses() { return classRepository.findAll(); }
    public List<ClassEntity> getClassesByLecturer(String lecturerId) { return classRepository.findByLecturerId(lecturerId); }
    public List<ClassEntity> getClassesByModule(String moduleId) { return classRepository.findByModuleId(moduleId); }
    public ClassEntity getClassById(String id) { return classRepository.findById(id).orElse(null); }

    public ClassEntity createClass(ClassEntity cls) { return classRepository.save(cls); }
    public ClassEntity updateClass(ClassEntity cls) { return classRepository.save(cls); }

    @Transactional
    public void deleteClass(String classId) {
        gradeRepository.findByClassId(classId).forEach(g -> gradeRepository.delete(g));
        submissionRepository.findByClassId(classId).forEach(s -> submissionRepository.delete(s));
        feedbackRepository.findByClassId(classId).forEach(f -> feedbackRepository.delete(f));
        classRepository.deleteById(classId);
    }

    public String generateClassId() {
        return "CLS" + String.format("%03d", classRepository.count() + 1);
    }
}
