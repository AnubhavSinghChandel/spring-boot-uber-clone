package com.project.taxiGo.taxiGoApp.repositories;

import com.project.taxiGo.taxiGoApp.entities.Session;
import com.project.taxiGo.taxiGoApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<List<Session>> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
