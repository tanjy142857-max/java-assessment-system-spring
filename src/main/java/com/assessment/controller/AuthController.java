package com.assessment.controller;

import com.assessment.model.Student;
import com.assessment.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final PendingUserService pendingUserService;

    public AuthController(PendingUserService pendingUserService) {
        this.pendingUserService = pendingUserService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String fullName, @RequestParam String email,
                           @RequestParam String intakeCode, @RequestParam String gender,
                           @RequestParam String nationality, @RequestParam String dateOfBirth,
                           RedirectAttributes redirect) {
        String id = pendingUserService.createPendingRequest(username, password, fullName, email, intakeCode, gender, nationality, dateOfBirth);
        if (id == null) {
            redirect.addFlashAttribute("error", "Username already exists.");
            return "redirect:/register";
        }
        redirect.addFlashAttribute("success", "Registration submitted! Wait for admin approval.");
        return "redirect:/login";
    }

    // 登录后根据角色跳转到对应 dashboard
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority();
        return switch (role) {
            case "ADMIN_STAFF" -> "redirect:/admin/dashboard";
            case "ACADEMIC_LEADER" -> "redirect:/academic-leader/dashboard";
            case "LECTURER" -> "redirect:/lecturer/dashboard";
            case "STUDENT" -> "redirect:/student/dashboard";
            default -> "redirect:/login";
        };
    }
}
