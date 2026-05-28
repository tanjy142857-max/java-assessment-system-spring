package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ClassRepository classRepository;
    private final ModuleRepository moduleRepository;
    private final AssessmentRepository assessmentRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public ReportService(ClassRepository classRepository, ModuleRepository moduleRepository, AssessmentRepository assessmentRepository, GradeRepository gradeRepository, UserRepository userRepository) {
        this.classRepository = classRepository;
        this.moduleRepository = moduleRepository;
        this.assessmentRepository = assessmentRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
    }

    public String generateClassReport(String classId) {
        ClassEntity cls = classRepository.findById(classId).orElse(null);
        if (cls == null) return "Class not found.";

        CourseModule module = moduleRepository.findById(cls.getModuleId()).orElse(null);
        User lecturer = userRepository.findById(cls.getLecturerId()).orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Class Report ===\n");
        sb.append("Class: ").append(cls.getClassName()).append(" (").append(cls.getClassId()).append(")\n");
        sb.append("Module: ").append(module != null ? module.getModuleName() : "N/A").append("\n");
        sb.append("Lecturer: ").append(lecturer != null ? lecturer.getFullName() : "N/A").append("\n");
        sb.append("Semester: ").append(cls.getSemester()).append("\n\n");

        List<Assessment> assessments = assessmentRepository.findByModuleId(cls.getModuleId());
        for (Assessment ass : assessments) {
            List<Grade> grades = gradeRepository.findByAssessmentId(ass.getAssessmentId()).stream()
                .filter(g -> g.getClassId().equals(classId))
                .collect(Collectors.toList());
            double avg = grades.stream().mapToDouble(Grade::getMarksObtained).average().orElse(0);
            double max = grades.stream().mapToDouble(Grade::getMarksObtained).max().orElse(0);
            double min = grades.stream().mapToDouble(Grade::getMarksObtained).min().orElse(0);

            sb.append("Assessment: ").append(ass.getAssessmentName()).append(" (").append(ass.getAssessmentType()).append(")\n");
            sb.append("  Average: ").append(String.format("%.1f", avg)).append("/").append(ass.getTotalMarks()).append("\n");
            sb.append("  Highest: ").append(max).append(" | Lowest: ").append(min).append("\n\n");
        }
        return sb.toString();
    }

    public String generateStudentReport(String studentId) {
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null) return "Student not found.";

        StringBuilder sb = new StringBuilder();
        sb.append("=== Student Report ===\n");
        sb.append("Name: ").append(student.getFullName()).append(" (").append(studentId).append(")\n");
        sb.append("Email: ").append(student.getEmail()).append("\n\n");

        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        sb.append("Grades:\n");
        for (Grade g : grades) {
            Assessment ass = assessmentRepository.findById(g.getAssessmentId()).orElse(null);
            sb.append("  ").append(ass != null ? ass.getAssessmentName() : "N/A")
              .append(" — ").append(g.getMarksObtained())
              .append(" (").append(g.getLetterGrade()).append(")\n");
        }
        return sb.toString();
    }
}
