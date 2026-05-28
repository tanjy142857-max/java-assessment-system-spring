package com.assessment.service;

import com.assessment.model.*;
import com.assessment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }
    public List<User> getUsersByRole(String role) { return userRepository.findByRole(role); }
    public User getUserById(String id) { return userRepository.findById(id).orElse(null); }
    public User getUserByUsername(String username) { return userRepository.findByUsername(username).orElse(null); }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) return null;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User existing = userRepository.findById(user.getUserId()).orElse(null);
        if (existing == null) return null;
        existing.setFullName(user.getFullName());
        existing.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existing);
    }

    public void deleteUser(String id) { userRepository.deleteById(id); }

    public String generateUserId(String role) {
        String prefix = switch (role) {
            case "ADMIN_STAFF" -> "ADM";
            case "ACADEMIC_LEADER" -> "ACL";
            case "LECTURER" -> "LEC";
            case "STUDENT" -> "STU";
            default -> "USR";
        };
        long count = userRepository.countByRole(role);
        return prefix + String.format("%03d", count + 1);
    }

    public long countByRole(String role) { return userRepository.countByRole(role); }
}
