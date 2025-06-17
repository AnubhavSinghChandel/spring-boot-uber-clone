package com.project.taxiGo.taxiGoApp.services.implementation;

import com.project.taxiGo.taxiGoApp.dto.DriverDTO;
import com.project.taxiGo.taxiGoApp.dto.SignupDTO;
import com.project.taxiGo.taxiGoApp.dto.UserDTO;
import com.project.taxiGo.taxiGoApp.entities.Driver;
import com.project.taxiGo.taxiGoApp.entities.User;
import com.project.taxiGo.taxiGoApp.enums.Roles;
import com.project.taxiGo.taxiGoApp.exceptions.ResourceNotFoundException;
import com.project.taxiGo.taxiGoApp.exceptions.RuntimeConflictException;
import com.project.taxiGo.taxiGoApp.repositories.UserRepository;
import com.project.taxiGo.taxiGoApp.security.JWTService;
import com.project.taxiGo.taxiGoApp.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.project.taxiGo.taxiGoApp.enums.Roles.DRIVER;
import static com.project.taxiGo.taxiGoApp.enums.Roles.RIDER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final SessionService sessionService;

    @Override
    public String[] login(String email, String password) {

        log.info("Login attempt for email: {}", email);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        log.info("Authentication successful for email: {}", email);

        User user = (User) auth.getPrincipal();

        log.debug("Authenticated user: {}", user.getUsername());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.createNewSession(user, refreshToken);

        log.info("Tokens generated and session created for user: {}", user.getUsername());

        return new String[] {accessToken, refreshToken};
    }

    @Override
    public void logout(String refreshToken) {
        sessionService.deleteSession(refreshToken);
    }

    @Override
    // When making changes to more than one entity in a service
    // always use @Transactional. This make the service atomic and
    // will always take care to not make any inconsistency in the database
    // If one of the writes fail it will roll back all the changes
    @Transactional
    public UserDTO signup(SignupDTO signupDto) {

        log.info("Signup attempt for email: {}", signupDto.getEmail());

        // Everything related to a user is created here, such as rider profile, wallet etc.
        // By default, everyone that signs up to the system will be considered as a rider,
        // to become a driver admins gve the user permissions handles in onboardDriver
        try{
            Optional<User> user = userRepo.findByEmail(signupDto.getEmail());
            if(user.isPresent()){

                log.warn("Signup failed: User with email {} already exists.", signupDto.getEmail());

                throw new BadCredentialsException("User with email "+signupDto.getEmail()+" already exists!");
            }

            log.info("User with email {} not found. Proceeding with signup.", signupDto.getEmail());

            User userEntity = modelMapper.map(signupDto, User.class);
            log.debug("Mapped SignupDTO to UserEntity: {}", userEntity);

            userEntity.setRoles(Set.of(Roles.RIDER));
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            log.info("Default roles assigned and password encoded for user: {}", userEntity.getUsername());

            User savedUser = userRepo.save(userEntity);
            log.info("User {} successfully saved to repository.", savedUser.getUsername());

            // create Rider Profile
            riderService.createNewRider(savedUser);
            log.info("Rider profile created for user: {}", savedUser.getUsername());

            walletService.createNewWallet(savedUser);
            log.info("Wallet created for user: {}", savedUser.getUsername());

            UserDTO signedUpUser = modelMapper.map(savedUser, UserDTO.class);
            log.info("Signup completed successfully for user: {}", signedUpUser.getEmail());
            return signedUpUser;
        }catch (Exception e){
            log.error("Signup failed for email: {}. Error: {}", signupDto.getEmail(), e.getMessage(), e);
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    public DriverDTO onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with id "+userId));
        if(user.getRoles().contains(DRIVER)){
            throw new RuntimeConflictException("User with id "+userId+" is already a driver");
        }
        Driver newDriver = Driver.builder()
                .rating(0.0)
                .available(true)
                .vehicleId(vehicleId)
                .user(user)
                .build();

        user.getRoles().add(DRIVER);
        user.getRoles().remove(RIDER);
        userRepo.save(user);

        Driver savedDriver = driverService.createNewDriver(newDriver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with id "+userId+" does not exist!"));
        return jwtService.generateRefreshToken(user);
    }
}
