package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.entities.User;

public interface SessionService {

    void createNewSession(User user, String refreshToken);

    void validateSession(String refreshToken);

    void deleteSession(String refreshToken);
}
