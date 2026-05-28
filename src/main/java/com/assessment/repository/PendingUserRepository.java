package com.assessment.repository;

import com.assessment.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PendingUserRepository extends JpaRepository<PendingUser, String> {
    List<PendingUser> findByStatus(String status);
    boolean existsByUsernameAndStatus(String username, String status);
}
