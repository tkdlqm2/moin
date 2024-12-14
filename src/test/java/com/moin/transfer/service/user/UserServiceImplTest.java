package com.moin.transfer.service.user;

import com.moin.transfer.dto.request.LoginRequest;
import com.moin.transfer.dto.request.SignUpRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.LoginApiResponse;
import com.moin.transfer.entity.User;
import com.moin.transfer.exception.user.UserException;
import com.moin.transfer.repository.UserRepository;
import com.moin.transfer.service.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccess() {
        // given
        SignUpRequest request = UserFixture.createSignUpRequest();

        doNothing().when(userValidator).validateDuplicateEmail(request.getUserId());
        when(encryptionService.encodePassword(request.getPassword()))
                .thenReturn(UserFixture.ENCODED_PASSWORD);
        when(encryptionService.encryptValue(request.getIdValue()))
                .thenReturn(UserFixture.ENCRYPTED_ID_VALUE);

        // when
        ApiResponse<?> response = userService.signUp(request);

        // then
        assertThat(response.isSuccess()).isTrue();
        verify(userRepository).save(any(User.class));
        verify(userValidator).validateDuplicateEmail(request.getUserId());
        verify(encryptionService).encodePassword(request.getPassword());
        verify(encryptionService).encryptValue(request.getIdValue());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() {
        // given
        LoginRequest request = UserFixture.createLoginRequest();
        User user = UserFixture.createUser();
        Authentication authentication = mock(Authentication.class);

        when(userValidator.validateAndGetUser(request.getUserId())).thenReturn(user);
        when(encryptionService.matchesPassword(request.getPassword(), user.getPassword())).thenReturn(true);
        when(authenticationService.authenticateUser(request.getUserId(), request.getPassword())).thenReturn(authentication);
        when(authenticationService.generateToken(authentication)).thenReturn(UserFixture.AUTH_TOKEN);

        // when
        LoginApiResponse response = userService.login(request);

        // then
        assertThat(response.getToken()).isEqualTo(UserFixture.AUTH_TOKEN);
        verify(userValidator).validateAndGetUser(request.getUserId());
        verify(authenticationService).authenticateUser(request.getUserId(), request.getPassword());
        verify(authenticationService).generateToken(authentication);
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    void loginFailInvalidPassword() {
        // given
        LoginRequest request = new LoginRequest(UserFixture.EMAIL, "wrongPassword");
        User user = UserFixture.createUser();

        when(userValidator.validateAndGetUser(request.getUserId())).thenReturn(user);
        when(encryptionService.matchesPassword(request.getPassword(), user.getPassword())).thenReturn(false);

        // when & then
        assertThrows(UserException.class, () -> userService.login(request));
        verify(userValidator).validateAndGetUser(request.getUserId());
        verify(encryptionService).matchesPassword(request.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("현재 사용자 조회 성공 테스트")
    void getCurrentUserSuccess() {
        // given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        User expectedUser = UserFixture.createUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(UserFixture.EMAIL);
        when(userValidator.validateAndGetUser(UserFixture.EMAIL)).thenReturn(expectedUser);

        // when
        User result = userService.getCurrentUser();

        // then
        assertThat(result).isEqualTo(expectedUser);
        verify(userValidator).validateAndGetUser(UserFixture.EMAIL);
    }

    @Test
    @DisplayName("현재 사용자 조회 실패 테스트 - 인증되지 않은 사용자")
    void getCurrentUserFailNotAuthenticated() {
        // given
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(null);

        // when & then
        assertThrows(UserException.class, () -> userService.getCurrentUser());
    }

    @Test
    @DisplayName("주민등록번호 복호화 테스트")
    void decryptRegNoSuccess() {
        // given
        String encryptedRegNo = UserFixture.ENCRYPTED_ID_VALUE;

        when(encryptionService.decryptValue(encryptedRegNo))
                .thenReturn(UserFixture.REG_NO);

        // when
        String result = userService.decryptRegNo(encryptedRegNo);

        // then
        assertThat(result).isEqualTo(UserFixture.REG_NO);
        verify(encryptionService).decryptValue(encryptedRegNo);
    }
}
