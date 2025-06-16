package com.project.uber.uberApp.services;

import com.project.uber.uberApp.entities.User;

public interface SessionService {

    void createNewSession(User user, String refreshToken);

    void validateSession(String refreshToken);

    void deleteSession(String refreshToken);
}
