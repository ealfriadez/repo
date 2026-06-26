package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.RegisterRequest;
import com.example.bookstoreai.dto.response.AuthResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.model.Customer;
import com.example.bookstoreai.model.Role;
import com.example.bookstoreai.model.User;
import com.example.bookstoreai.repository.CustomerRepository;
import com.example.bookstoreai.repository.RoleRepository;
import com.example.bookstoreai.repository.UserRepository;
import com.example.bookstoreai.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock CustomerRepository customerRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    void register_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail("carlos@email.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(buildRequest("carlos@email.com", "12345678")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("carlos@email.com");
    }

    @Test
    void register_duplicateDni_throwsException() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.existsByDni("12345678")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(buildRequest("nuevo@email.com", "12345678")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("12345678");
    }

    @Test
    void register_roleNotFound_throwsException() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.existsByDni(any())).thenReturn(false);
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.register(buildRequest("nuevo@email.com", "99887766")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("ROLE_CUSTOMER");
    }

    @Test
    void register_ok() {
        Role role = Role.builder().id(1L).name("ROLE_CUSTOMER").build();
        User user = User.builder().id(1L).email("nuevo@email.com").role(role).build();
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.existsByDni(any())).thenReturn(false);
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(customerRepository.save(any(Customer.class))).thenReturn(mock(Customer.class));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken(userDetails)).thenReturn("jwt-token");

        AuthResponse result = authService.register(buildRequest("nuevo@email.com", "99887766"));

        assertThat(result.token()).isEqualTo("jwt-token");
        verify(userRepository).save(any(User.class));
        verify(customerRepository).save(any(Customer.class));
    }

    private RegisterRequest buildRequest(String email, String dni) {
        return new RegisterRequest(email, "Pass@1234", "Carlos", "Lopez", dni, "999888777");
    }
}
