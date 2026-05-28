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
@RequestMapping("/academic-leader")
public class AcademicLeaderController {

    private final ModuleService moduleService;
    private final ClassService classService;
    private final UserService userService;
    private final ReportService reportService;

    public AcademicLeaderController(ModuleService moduleService, ClassService classService, UserService userService, ReportService reportService) {
        this.moduleService = moduleService;
        this.classService = classService;
        this.userService = userService;
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("myModules", moduleService.getModulesByAcademicLeader(user.getUserId()).size());
        model.addAttribute("myLecturers", userService.getUsersByRole("LECTURER").stream()
            .filter(u -> u instanceof Lecturer && user.getUserId().equals(((Lecturer) u).getAcademicLeaderId())).count());
        return "academic-leader/dashboard";
    }

    @GetMapping("/modules")
    public String manageModules(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        List<CourseModule> modules = moduleService.getModulesByAcademicLeader(user.getUserId());
        model.addAttribute("modules", modules);
        model.addAttribute("lecturers", userService.getUsersByRole("LECTURER"));
        return "academic-leader/manage-modules";
    }

    @PostMapping("/modules/add")
    public String addModule(@RequestParam String moduleCode, @RequestParam String moduleName,
                            @RequestParam String description, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName());
        String id = moduleService.generateModuleId();
        moduleService.createModule(new CourseModule(id, moduleCode, moduleName, description, user.getUserId()));
        return "redirect:/academic-leader/modules";
    }

    @PostMapping("/modules/delete/{id}")
    public String deleteModule(@PathVariable String id) {
        moduleService.deleteModule(id);
        return "redirect:/academic-leader/modules";
    }

    @PostMapping("/modules/create-class")
    public String createClass(@RequestParam String moduleId, @RequestParam String lecturerId,
                              @RequestParam String semester, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName());
        CourseModule module = moduleService.getModuleById(moduleId);
        String id = classService.generateClassId();
        classService.createClass(new ClassEntity(id, module.getModuleName() + " - " + semester, moduleId, lecturerId, semester));
        return "redirect:/academic-leader/modules";
    }

    @GetMapping("/reports")
    public String reports(Authentication auth, Model model) {
        User user = userService.getUserByUsername(auth.getName());
        model.addAttribute("modules", moduleService.getModulesByAcademicLeader(user.getUserId()));
        return "academic-leader/view-reports";
    }

    @GetMapping("/reports/class/{classId}")
    public String classReport(@PathVariable String classId, Model model) {
        model.addAttribute("report", reportService.generateClassReport(classId));
        return "academic-leader/view-reports";
    }
}
