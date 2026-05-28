package com.assessment.config;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.assessment.repository.GradeLevelRepository gradeLevelRepository;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, com.assessment.repository.GradeLevelRepository gradeLevelRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gradeLevelRepository = gradeLevelRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            AdminStaff admin = new AdminStaff("ADM001", "admin", passwordEncoder.encode("admin123"), "Default Admin", "admin@apu.edu.my", "MALE", "MALAYSIAN", "1980-01-01");
            userRepository.save(admin);
        }

        if (gradeLevelRepository.count() == 0) {
            gradeLevelRepository.save(new GradeLevel("A+", 90, 100, "Distinction"));
            gradeLevelRepository.save(new GradeLevel("A", 80, 89, "Distinction"));
            gradeLevelRepository.save(new GradeLevel("A-", 75, 79, "Distinction"));
            gradeLevelRepository.save(new GradeLevel("B+", 70, 74, "Credit"));
            gradeLevelRepository.save(new GradeLevel("B", 65, 69, "Credit"));
            gradeLevelRepository.save(new GradeLevel("B-", 60, 64, "Credit"));
            gradeLevelRepository.save(new GradeLevel("C+", 55, 59, "Pass"));
            gradeLevelRepository.save(new GradeLevel("C", 50, 54, "Pass"));
            gradeLevelRepository.save(new GradeLevel("D", 40, 49, "Marginal Fail"));
            gradeLevelRepository.save(new GradeLevel("F", 0, 39, "Fail"));
        }
    }
}
