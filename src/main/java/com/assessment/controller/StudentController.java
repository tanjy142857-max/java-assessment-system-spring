package com.assessment.controller;

import com.assessment.model.*;
import com.assessment.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;
    private final ClassService classService;
    private final ModuleService moduleService;
    private final AssessmentService assessmentService;
    private final GradeService gradeService;
    private final SubmissionService submissionService;
    private final FeedbackService feedbackService;
    private final ReportService reportService;

    public StudentController(UserService userService, ClassService classService, ModuleService moduleService, AssessmentService assessmentService, GradeService gradeService, SubmissionService submissionService, FeedbackService feedbackService, ReportService reportService) {
        this.userService = userService;
        this.classService = classService;
        this.moduleService = moduleService;
        this.assessmentService = assessmentService;
        this.gradeService = gradeService;
        this.submissionService = submissionService;
        this.feedbackService = feedbackService;
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User student = userService.getUserByUsername(auth.getName());
        model.addAttribute("gpa", String.format("%.1f", gradeService.calculateGPA(student.getUserId())));
        model.addAttribute("gradesCount", gradeService.getGradesByStudent(student.getUserId()).size());
        return "student/dashboard";
    }

    @GetMapping("/classes")
    public String classRegistration(Model model) {
        model.addAttribute("allClasses", classService.getAllClasses());
        model.addAttribute("modules", moduleService.getAllModules());
        return "student/class-registration";
    }

    @GetMapping("/submissions")
    public String submitAssignments(Authentication auth, @RequestParam(required = false) String classId, Model model) {
        User student = userService.getUserByUsername(auth.getName());
        if (classId != null) {
            ClassEntity cls = classService.getClassById(classId);
            model.addAttribute("selectedClass", cls);
            model.addAttribute("assessments", assessmentService.getAssessmentsByModule(
                cls != null ? cls.getModuleId() : ""));
            model.addAttribute("submissions", submissionService.getSubmissionsByStudent(student.getUserId()));
        }
        return "student/submit-assignments";
    }

    @PostMapping("/submissions/submit")
    public String submit(@RequestParam String classId, @RequestParam String assessmentId,
                         @RequestParam String notes, Authentication auth, RedirectAttributes ra) {
        User student = userService.getUserByUsername(auth.getName());
        if (submissionService.hasSubmission(student.getUserId(), assessmentId)) {
            ra.addFlashAttribute("error", "Already submitted this assessment.");
            return "redirect:/student/submissions?classId=" + classId;
        }
        String id = submissionService.generateSubmissionId();
        Submission sub = new Submission(id, student.getUserId(), assessmentId, classId, "Submitted", LocalDate.now().toString(), notes);
        submissionService.createSubmission(sub);
        ra.addFlashAttribute("success", "Submitted!");
        return "redirect:/student/submissions?classId=" + classId;
    }

    @GetMapping("/results")
    public String results(Authentication auth, @RequestParam(required = false) String classId, Model model) {
        User student = userService.getUserByUsername(auth.getName());
        List<Grade> grades = classId != null
            ? gradeService.getGradesByStudent(student.getUserId()).stream().filter(g -> g.getClassId().equals(classId)).toList()
            : gradeService.getGradesByStudent(student.getUserId());
        model.addAttribute("grades", grades);
        model.addAttribute("classes", classService.getAllClasses());
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("gpa", gradeService.calculateGPA(student.getUserId()));
        model.addAttribute("report", reportService.generateStudentReport(student.getUserId()));
        return "student/results-feedback";
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam String gradeId, @RequestParam String comment,
                             @RequestParam String classId, Authentication auth) {
        Grade grade = gradeService.getGradesByStudent(
            userService.getUserByUsername(auth.getName()).getUserId()).stream()
            .filter(g -> g.getGradeId().equals(gradeId)).findFirst().orElse(null);
        if (grade != null) {
            Feedback fb = feedbackService.getFeedback(grade.getStudentId(), grade.getAssessmentId(), grade.getClassId());
            if (fb == null) {
                String fbId = feedbackService.generateFeedbackId();
                fb = new Feedback(fbId, grade.getStudentId(), grade.getAssessmentId(), grade.getClassId(), "", "", LocalDate.now().toString());
            }
            fb.setStudentComment(comment);
            feedbackService.saveFeedback(fb);
        }
        return "redirect:/student/results?classId=" + classId;
    }
}
