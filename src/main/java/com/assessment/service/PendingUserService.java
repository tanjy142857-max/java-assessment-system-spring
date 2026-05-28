package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PendingUserService {

    private final PendingUserRepository pendingUserRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PendingUserService(PendingUserRepository pendingUserRepository, UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.pendingUserRepository = pendingUserRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<PendingUser> getPendingRequests() { return pendingUserRepository.findByStatus("PENDING"); }

    public String createPendingRequest(String username, String password, String fullName, String email, String intakeCode, String gender, String nationality, String dob) {
        if (userRepository.existsByUsername(username) || pendingUserRepository.existsByUsernameAndStatus(username, "PENDING"))
            return null;

        String pendingId = "PEND" + String.format("%03d", pendingUserRepository.count() + 1);
        PendingUser pending = new PendingUser(pendingId, username, passwordEncoder.encode(password), fullName, email, intakeCode, gender, nationality, dob, LocalDate.now().toString(), "PENDING");
        pendingUserRepository.save(pending);
        return pendingId;
    }

    public boolean approveRequest(String pendingId) {
        PendingUser pending = pendingUserRepository.findById(pendingId).orElse(null);
        if (pending == null) return false;

        String studentId = userService.generateUserId("STUDENT");
        Student student = new Student(studentId, pending.getUsername(), pending.getPassword(), pending.getFullName(), pending.getEmail(), pending.getIntakeCode(), pending.getGender(), pending.getNationality(), pending.getDateOfBirth());
        userRepository.save(student);

        pending.setStatus("APPROVED");
        pendingUserRepository.save(pending);
        return true;
    }

    public boolean rejectRequest(String pendingId) {
        PendingUser pending = pendingUserRepository.findById(pendingId).orElse(null);
        if (pending == null) return false;
        pending.setStatus("REJECTED");
        pendingUserRepository.save(pending);
        return true;
    }

    public void deleteRequest(String pendingId) { pendingUserRepository.deleteById(pendingId); }
    public PendingUser getById(String id) { return pendingUserRepository.findById(id).orElse(null); }
}
