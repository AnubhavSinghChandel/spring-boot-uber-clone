package com.project.uber.uberApp.controllers;

import com.project.uber.uberApp.dto.*;
import com.project.uber.uberApp.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO){

        return new ResponseEntity<>(authService.signup(signupDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response){

        String[] tokens = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Cookie cookie = new Cookie("refreshToken", tokens[1]);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(tokens[0]);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res){

        String refreshToken = Arrays.stream(req.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token absent from cookie"));

        authService.logout(refreshToken);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);

        res.addCookie(cookie);


        return ResponseEntity.ok().build();
    }

    @PostMapping("/onboardNewDriver/{userId}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<DriverDTO> onboardNewDriver(@PathVariable Long userId, @RequestBody OnboardDriverDTO onboardDriverDTO){
        return new ResponseEntity<>(authService.onboardNewDriver(userId, onboardDriverDTO.getVehicleId()), HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest req){

        String refreshToken = Arrays.stream(req.getCookies())
                .filter(cookie-> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie-> cookie.getValue())
                .orElseThrow(()-> new AuthenticationServiceException("Refresh token absent from cookie"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }

}
