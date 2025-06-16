package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.entities.Session;
import com.project.uber.uberApp.entities.User;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.repositories.SessionRepository;
import com.project.uber.uberApp.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepo;
    private final int SESSION_LIMIT = 1;

    @Override
    public void createNewSession(User user, String refreshToken) {

        List<Session> sessions = sessionRepo.findByUser(user).orElseThrow(()->new ResourceNotFoundException("User "+user.getId()+" has no active session!"));
        if(sessions.size()==SESSION_LIMIT){
            sessions.sort(Comparator.comparing(session -> session.getLastUsedAt()));

            Session lastUsedSession = sessions.getFirst();
            sessionRepo.delete(lastUsedSession);
        }

        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        sessionRepo.save(newSession);
    }

    @Override
    public void validateSession(String refreshToken){
        Session session = sessionRepo.findByRefreshToken(refreshToken)
                .orElseThrow(()->new SessionAuthenticationException("Session associated with refreshToken does not exist!"));

        session.setLastUsedAt(LocalDateTime.now());
        sessionRepo.save(session);
    }

    @Override
    public void deleteSession(String refreshToken) {
        Session session = sessionRepo.findByRefreshToken(refreshToken)
                .orElseThrow(()->new SessionAuthenticationException("Session associated with refreshToken does not exist!"));

        sessionRepo.delete(session);
    }
}
