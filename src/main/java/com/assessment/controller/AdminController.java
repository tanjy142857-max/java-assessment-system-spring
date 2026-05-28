package com.assessment.controller;

import com.assessment.model.*;
import com.assessment.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ClassService classService;
    private final ModuleService moduleService;
    private final PendingUserService pendingUserService;
    private final GradeService gradeService;

    public AdminController(UserService userService, ClassService classService, ModuleService moduleService, PendingUserService pendingUserService, GradeService gradeService) {
        this.userService = userService;
        this.classService = classService;
        this.moduleService = moduleService;
        this.pendingUserService = pendingUserService;
        this.gradeService = gradeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalClasses", classService.getAllClasses().size());
        model.addAttribute("pendingCount", pendingUserService.getPendingRequests().size());
        return "admin/dashboard";
    }

    // --- Manage Users ---
    @GetMapping("/users")
    public String manageUsers(@RequestParam(required = false) String role, Model model) {
        List<User> users = (role != null && !role.isEmpty()) ? userService.getUsersByRole(role) : userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("selectedRole", role);
        return "admin/manage-users";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam String username, @RequestParam String password,
                          @RequestParam String fullName, @RequestParam String email,
                          @RequestParam String role, @RequestParam String extra,
                          @RequestParam String gender, @RequestParam String nationality,
                          @RequestParam String dob, RedirectAttributes ra) {
        String userId = userService.generateUserId(role);
        User user = switch (role) {
            case "ADMIN_STAFF" -> new AdminStaff(userId, username, password, fullName, email, gender, nationality, dob);
            case "ACADEMIC_LEADER" -> new AcademicLeader(userId, username, password, fullName, email, extra, gender, nationality, dob);
            case "LECTURER" -> new Lecturer(userId, username, password, fullName, email, extra, gender, nationality, dob);
            case "STUDENT" -> new Student(userId, username, password, fullName, email, extra, gender, nationality, dob);
            default -> null;
        };
        if (user == null || userService.createUser(user) == null) {
            ra.addFlashAttribute("error", "Username already exists or invalid role.");
        } else {
            ra.addFlashAttribute("success", "User created: " + userId);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // --- Pending Requests ---
    @GetMapping("/pending")
    public String pendingRequests(Model model) {
        model.addAttribute("requests", pendingUserService.getPendingRequests());
        return "admin/pending-requests";
    }

    @PostMapping("/pending/approve/{id}")
    public String approve(@PathVariable String id, RedirectAttributes ra) {
        if (pendingUserService.approveRequest(id))
            ra.addFlashAttribute("success", "Request approved.");
        return "redirect:/admin/pending";
    }

    @PostMapping("/pending/reject/{id}")
    public String reject(@PathVariable String id, RedirectAttributes ra) {
        pendingUserService.rejectRequest(id);
        ra.addFlashAttribute("success", "Request rejected.");
        return "redirect:/admin/pending";
    }

    // --- Manage Classes ---
    @GetMapping("/classes")
    public String manageClasses(Model model) {
        model.addAttribute("classes", classService.getAllClasses());
        model.addAttribute("modules", moduleService.getAllModules());
        model.addAttribute("lecturers", userService.getUsersByRole("LECTURER"));
        return "admin/manage-classes";
    }

    @PostMapping("/classes/add")
    public String addClass(@RequestParam String className, @RequestParam String moduleId,
                           @RequestParam String lecturerId, @RequestParam String semester) {
        String id = classService.generateClassId();
        classService.createClass(new ClassEntity(id, className, moduleId, lecturerId, semester));
        return "redirect:/admin/classes";
    }

    @PostMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return "redirect:/admin/classes";
    }

    // --- Grading System ---
    @GetMapping("/grading")
    public String gradingSystem(Model model) {
        model.addAttribute("gradeLevels", gradeService.getGradingSystem());
        return "admin/grading-system";
    }

    @PostMapping("/grading/save")
    public String saveGrading(@RequestParam List<String> grade, @RequestParam List<Integer> minMark,
                              @RequestParam List<Integer> maxMark, @RequestParam List<String> description) {
        List<GradeLevel> levels = new java.util.ArrayList<>();
        for (int i = 0; i < grade.size(); i++) {
            levels.add(new GradeLevel(grade.get(i), minMark.get(i), maxMark.get(i), description.get(i)));
        }
        gradeService.saveGradingSystem(levels);
        return "redirect:/admin/grading";
    }
}
