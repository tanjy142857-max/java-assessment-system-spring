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
@RequestMapping("/lecturer")
public class LecturerController {

    private final ClassService classService;
    private final ModuleService moduleService;
    private final AssessmentService assessmentService;
    private final GradeService gradeService;
    private final UserService userService;
    private final SubmissionService submissionService;
    private final FeedbackService feedbackService;

    public LecturerController(ClassService classService, ModuleService moduleService, AssessmentService assessmentService, GradeService gradeService, UserService userService, SubmissionService submissionService, FeedbackService feedbackService) {
        this.classService = classService;
        this.moduleService = moduleService;
        this.assessmentService = assessmentService;
        this.gradeService = gradeService;
        this.userService = userService;
        this.submissionService = submissionService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        List<ClassEntity> myClasses = classService.getClassesByLecturer(user.getUserId());
        model.addAttribute("myClasses", myClasses.size());
        model.addAttribute("totalGrades", gradeService.getGradesByLecturer(user.getUserId()).size());
        return "lecturer/dashboard";
    }

    @GetMapping("/assessments")
    public String assessments(Authentication auth, @RequestParam(required = false) String classId, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        List<ClassEntity> myClasses = classService.getClassesByLecturer(user.getUserId());
        model.addAttribute("myClasses", myClasses);

        if (classId != null) {
            ClassEntity cls = classService.getClassById(classId);
            if (cls != null) {
                model.addAttribute("selectedClass", cls);
                model.addAttribute("assessments", assessmentService.getAssessmentsByModule(cls.getModuleId()));
                model.addAttribute("module", moduleService.getModuleById(cls.getModuleId()));
                model.addAttribute("totalWeight", assessmentService.getTotalWeightageForModule(cls.getModuleId()));

                // 学生列表
                List<User> students = new ArrayList<>();
                for (User u : userService.getUsersByRole("STUDENT")) {
                    students.add(u);
                }
                model.addAttribute("students", students);
                model.addAttribute("grades", gradeService.getGradesByClass(classId));
                model.addAttribute("today", LocalDate.now().toString());
            }
        }
        return "lecturer/assessments-grading";
    }

    @PostMapping("/assessments/add")
    public String addAssessment(@RequestParam String classId, @RequestParam String assessmentName,
                                @RequestParam String assessmentType, @RequestParam double weightage,
                                @RequestParam double totalMarks, @RequestParam String dueDate,
                                @RequestParam String dueTime, RedirectAttributes ra) {
        ClassEntity cls = classService.getClassById(classId);
        if (cls == null) return "redirect:/lecturer/assessments";
        String id = assessmentService.generateAssessmentId();
        Assessment ass = new Assessment(id, cls.getModuleId(), assessmentName, assessmentType, weightage, totalMarks);
        ass.setDueDate(dueDate);
        ass.setDueTime(dueTime);
        if (assessmentService.createAssessment(ass) == null) {
            ra.addFlashAttribute("error", "Total weightage would exceed 100%.");
        }
        return "redirect:/lecturer/assessments?classId=" + classId;
    }

    @PostMapping("/grades/save")
    public String saveGrade(@RequestParam String classId, @RequestParam String assessmentId,
                            @RequestParam String studentId, @RequestParam double marks,
                            @RequestParam(required = false) String feedbackText, RedirectAttributes ra) {
        String gradeId = "GRD" + String.format("%03d", gradeService.getAllGrades().size() + 1);
        Grade grade = new Grade(gradeId, studentId, assessmentId, classId, marks, "", LocalDate.now().toString());
        gradeService.saveGrade(grade);

        if (feedbackText != null && !feedbackText.isBlank()) {
            User lecturer = userService.getUserByUsername(getCurrentUsername());
            Feedback fb = feedbackService.getFeedback(studentId, assessmentId, classId);
            if (fb == null) {
                String fbId = feedbackService.generateFeedbackId();
                fb = new Feedback(fbId, studentId, assessmentId, classId, lecturer != null ? lecturer.getUserId() : "", feedbackText, LocalDate.now().toString());
            } else {
                fb.setFeedbackText(feedbackText);
                fb.setFeedbackDate(LocalDate.now().toString());
            }
            feedbackService.saveFeedback(fb);
        }
        ra.addFlashAttribute("success", "Grade saved.");
        return "redirect:/lecturer/assessments?classId=" + classId;
    }

    @GetMapping("/feedback")
    public String feedback(Authentication auth, @RequestParam(required = false) String classId, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("myClasses", classService.getClassesByLecturer(user.getUserId()));
        if (classId != null) {
            model.addAttribute("selectedClass", classService.getClassById(classId));
            model.addAttribute("assessments", assessmentService.getAssessmentsByModule(
                classService.getClassById(classId).getModuleId()));
            model.addAttribute("grades", gradeService.getGradesByClass(classId));
            model.addAttribute("feedbacks", feedbackService.getFeedbackByClass(classId));
        }
        return "lecturer/feedback";
    }

    @PostMapping("/feedback/save")
    public String saveFeedback(@RequestParam String classId, @RequestParam String assessmentId,
                               @RequestParam String studentId, @RequestParam String feedbackText) {
        User lecturer = userService.getUserByUsername(getCurrentUsername());
        Feedback fb = feedbackService.getFeedback(studentId, assessmentId, classId);
        if (fb == null) {
            String fbId = feedbackService.generateFeedbackId();
            fb = new Feedback(fbId, studentId, assessmentId, classId, lecturer.getUserId(), feedbackText, LocalDate.now().toString());
        } else {
            fb.setFeedbackText(feedbackText);
        }
        feedbackService.saveFeedback(fb);
        return "redirect:/lecturer/feedback?classId=" + classId;
    }

    private String getCurrentUsername() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
