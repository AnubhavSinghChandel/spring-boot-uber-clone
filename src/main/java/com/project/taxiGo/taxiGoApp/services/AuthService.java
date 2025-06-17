package com.project.taxiGo.taxiGoApp.services;

import com.project.taxiGo.taxiGoApp.dto.DriverDTO;
import com.project.taxiGo.taxiGoApp.dto.SignupDTO;
import com.project.taxiGo.taxiGoApp.dto.UserDTO;

public interface AuthService {

    String[] login(String email, String password);

    UserDTO signup(SignupDTO signupDto);

    DriverDTO onboardNewDriver(Long userID, String vehicleId);

    String refreshToken(String refreshToken);

    void logout(String refreshToken);
}
