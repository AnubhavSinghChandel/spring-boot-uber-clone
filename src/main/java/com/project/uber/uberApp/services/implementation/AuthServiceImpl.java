package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.DriverDTO;
import com.project.uber.uberApp.dto.LoginResponseDTO;
import com.project.uber.uberApp.dto.SignupDTO;
import com.project.uber.uberApp.dto.UserDTO;
import com.project.uber.uberApp.entities.DriverEntity;
import com.project.uber.uberApp.entities.UserEntity;
import com.project.uber.uberApp.enums.Roles;
import com.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.project.uber.uberApp.repositories.UserRepository;
import com.project.uber.uberApp.security.JWTService;
import com.project.uber.uberApp.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.Cookie;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.project.uber.uberApp.enums.Roles.DRIVER;
import static com.project.uber.uberApp.enums.Roles.RIDER;

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

        UserEntity user = (UserEntity) auth.getPrincipal();

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
            Optional<UserEntity> user = userRepo.findByEmail(signupDto.getEmail());
            if(user.isPresent()){

                log.warn("Signup failed: User with email {} already exists.", signupDto.getEmail());

                throw new BadCredentialsException("User with email "+signupDto.getEmail()+" already exists!");
            }

            log.info("User with email {} not found. Proceeding with signup.", signupDto.getEmail());

            UserEntity userEntity = modelMapper.map(signupDto, UserEntity.class);
            log.debug("Mapped SignupDTO to UserEntity: {}", userEntity);

            userEntity.setRoles(Set.of(Roles.RIDER));
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            log.info("Default roles assigned and password encoded for user: {}", userEntity.getUsername());

            UserEntity savedUser = userRepo.save(userEntity);
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
        UserEntity user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with id "+userId));
        if(user.getRoles().contains(DRIVER)){
            throw new RuntimeConflictException("User with id "+userId+" is already a driver");
        }
        DriverEntity newDriver = DriverEntity.builder()
                .rating(0.0)
                .available(true)
                .vehicleId(vehicleId)
                .user(user)
                .build();

        user.getRoles().add(DRIVER);
        user.getRoles().remove(RIDER);
        userRepo.save(user);

        DriverEntity savedDriver = driverService.createNewDriver(newDriver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        UserEntity user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with id "+userId+" does not exist!"));
        return jwtService.generateRefreshToken(user);
    }
}
