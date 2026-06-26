package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.LoginRequest;
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
import com.example.bookstoreai.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        User user = userRepository.findByEmail(request.email()).orElseThrow();

        return new AuthResponse(token, user.getEmail(), user.getRole().getName());
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("El email '%s' ya está registrado".formatted(request.email()));
        }
        if (customerRepository.existsByDni(request.dni())) {
            throw new BusinessException("El DNI '%s' ya está registrado".formatted(request.dni()));
        }

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new BusinessException("Rol ROLE_CUSTOMER no encontrado. Ejecute data.sql primero"));

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .role(customerRole)
                .build();
        userRepository.save(user);

        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .dni(request.dni())
                .phone(request.phone())
                .user(user)
                .build();
        customerRepository.save(customer);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        return new AuthResponse(token, user.getEmail(), customerRole.getName());
    }
}
