package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final GradeLevelRepository gradeLevelRepository;
    private final AssessmentRepository assessmentRepository;
    private final ClassRepository classRepository;
    private final SubmissionRepository submissionRepository;
    private final ModuleRepository moduleRepository;

    public GradeService(GradeRepository gradeRepository, GradeLevelRepository gradeLevelRepository, AssessmentRepository assessmentRepository, ClassRepository classRepository, SubmissionRepository submissionRepository, ModuleRepository moduleRepository) {
        this.gradeRepository = gradeRepository;
        this.gradeLevelRepository = gradeLevelRepository;
        this.assessmentRepository = assessmentRepository;
        this.classRepository = classRepository;
        this.submissionRepository = submissionRepository;
        this.moduleRepository = moduleRepository;
    }

    public List<Grade> getAllGrades() { return gradeRepository.findAll(); }
    public List<Grade> getGradesByStudent(String studentId) { return gradeRepository.findByStudentId(studentId); }
    public List<Grade> getGradesByClass(String classId) { return gradeRepository.findByClassId(classId); }
    public Grade getGradeByStudentAssessmentClass(String studentId, String assessmentId, String classId) {
        return gradeRepository.findByStudentIdAndAssessmentIdAndClassId(studentId, assessmentId, classId).orElse(null);
    }

    public Grade saveGrade(Grade grade) {
        // 自动算字母等级
        double pct = (grade.getMarksObtained() / getAssessmentTotalMarks(grade.getAssessmentId())) * 100;
        grade.setLetterGrade(calculateLetterGrade(pct));
        Grade saved = gradeRepository.save(grade);
        // 更新对应 submission 状态
        submissionRepository.findByStudentIdAndAssessmentIdAndClassId(grade.getStudentId(), grade.getAssessmentId(), grade.getClassId())
            .ifPresent(sub -> { sub.setStatus("Graded"); submissionRepository.save(sub); });
        return saved;
    }

    // --- 评分等级表管理 ---
    public List<GradeLevel> getGradingSystem() { return gradeLevelRepository.findAll(); }
    public void saveGradingSystem(List<GradeLevel> levels) {
        gradeLevelRepository.deleteAll();
        gradeLevelRepository.saveAll(levels);
    }

    public String calculateLetterGrade(double percentage) {
        int floor = (int) Math.floor(percentage);
        return gradeLevelRepository.findAll().stream()
            .filter(g -> floor >= g.getMinMark() && floor <= g.getMaxMark())
            .findFirst().map(GradeLevel::getGrade).orElse("F");
    }

    // --- GPA 计算（与原版逻辑一致）---
    public double calculateGPA(String studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) return 0.0;

        // 按模块分组
        Map<String, List<Grade>> byModule = new HashMap<>();
        for (Grade g : grades) {
            ClassEntity cls = classRepository.findById(g.getClassId()).orElse(null);
            if (cls == null) continue;
            byModule.computeIfAbsent(cls.getModuleId(), k -> new ArrayList<>()).add(g);
        }

        double totalPoints = 0;
        int countedModules = 0;

        for (Map.Entry<String, List<Grade>> entry : byModule.entrySet()) {
            String moduleId = entry.getKey();
            List<Grade> moduleGrades = entry.getValue();

            // 检查该模块的所有评估是否都有成绩（权重合计 = 100%）
            List<Assessment> assessments = assessmentRepository.findByModuleId(moduleId);
            double totalWeight = assessments.stream().mapToDouble(Assessment::getWeightage).sum();
            if (Math.abs(totalWeight - 100.0) > 0.01) continue; // 不是所有评估都打分了

            double weightedPct = 0;
            for (Grade g : moduleGrades) {
                Assessment ass = assessmentRepository.findById(g.getAssessmentId()).orElse(null);
                if (ass == null) continue;
                double pct = (g.getMarksObtained() / ass.getTotalMarks()) * 100;
                weightedPct += pct * (ass.getWeightage() / 100.0);
            }

            totalPoints += percentageToGpaPoints(weightedPct);
            countedModules++;
        }

        return countedModules == 0 ? 0.0 : totalPoints / countedModules;
    }

    private double percentageToGpaPoints(double pct) {
        if (pct >= 90) return 4.0;
        if (pct >= 80) return 3.7;
        if (pct >= 75) return 3.3;
        if (pct >= 70) return 3.0;
        if (pct >= 65) return 2.7;
        if (pct >= 60) return 2.3;
        if (pct >= 55) return 2.0;
        if (pct >= 50) return 1.7;
        if (pct >= 40) return 1.3;
        return 0.0;
    }

    public List<Grade> getGradesByLecturer(String lecturerId) {
        List<ClassEntity> classes = classRepository.findByLecturerId(lecturerId);
        return classes.stream()
            .flatMap(c -> gradeRepository.findByClassId(c.getClassId()).stream())
            .collect(Collectors.toList());
    }

    private double getAssessmentTotalMarks(String assessmentId) {
        return assessmentRepository.findById(assessmentId)
            .map(Assessment::getTotalMarks).orElse(100.0);
    }
}
