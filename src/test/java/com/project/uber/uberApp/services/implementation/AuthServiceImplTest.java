package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.SignupDTO;
import com.project.uber.uberApp.dto.UserDTO;
import com.project.uber.uberApp.entities.User;
import com.project.uber.uberApp.enums.Roles;
import com.project.uber.uberApp.repositories.UserRepository;
import com.project.uber.uberApp.security.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RiderServiceImpl riderService;

    @Mock
    private WalletServiceImpl walletService;

    @Mock
    private DriverServiceImpl driverService;

    @Mock
    private JWTService jwtService;

    @Mock
    private SessionServiceImpl sessionService;

    @Mock
    private AuthenticationManager authManager;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp(){

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRoles(Set.of(Roles.RIDER));
        user.setPassword("password");

    }

    @Test
    void testLogin_WhenSuccess(){

//        arrange

        Authentication auth = mock(Authentication.class);
        when(authManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

//        act

        String[] tokens = authService.login(user.getEmail(), user.getPassword());

//        assert

        assertThat(tokens).hasSize(2);
        assertThat(tokens[0]).isEqualTo("accessToken");
        assertThat(tokens[1]).isEqualTo("refreshToken");
        verify(authManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        verify(jwtService, times(1)).generateAccessToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
        verify(sessionService, times(1)).createNewSession(user, "refreshToken");

    }

    @Test
    void testLogin_WhenAuthenticationFailure(){

//        arrange

        Authentication auth = mock(Authentication.class);
        when(authManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid Email or password!"));

//        act and assert

        assertThatThrownBy(()-> authManager.authenticate(auth))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid Email or password!");

        verify(authManager, times(1)).authenticate(auth);

    }

    @Test
    void testSignUp_WhenSuccess(){

//        arrange

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenReturn(user);

//        act
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setEmail("test@example.com");
        signupDTO.setName("test");
        signupDTO.setPhone("+911234567890");
        signupDTO.setPassword("password");
        UserDTO signedUpUser = authService.signup(signupDTO);

//        assert

        assertThat(signedUpUser).isNotNull();
        assertThat(signedUpUser.getEmail()).isEqualTo(signupDTO.getEmail());
        verify(riderService, times(1)).createNewRider(user);
        verify(walletService, times(1)).createNewWallet(user);

    }


}
