package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.UserEntity;
import org.springframework.stereotype.Service;

public interface SessionService {

    void createNewSession(UserEntity user, String refreshToken);

    void validateSession(String refreshToken);

    void deleteSession(String refreshToken);
}
