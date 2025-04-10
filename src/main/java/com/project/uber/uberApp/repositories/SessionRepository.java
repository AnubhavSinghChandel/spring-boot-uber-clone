package com.project.uber.uberApp.repositories;

import com.project.uber.uberApp.entities.SessionEntity;
import com.project.uber.uberApp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    Optional<List<SessionEntity>> findByUser(UserEntity user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
