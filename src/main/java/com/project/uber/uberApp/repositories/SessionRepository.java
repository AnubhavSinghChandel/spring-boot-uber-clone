package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.Session;
import com.project.uber.uberApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<List<Session>> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
