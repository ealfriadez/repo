package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.ChangePasswordRequest;
import com.example.bookstoreai.dto.request.UpdateProfileRequest;
import com.example.bookstoreai.dto.response.UserProfileResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.model.Customer;
import com.example.bookstoreai.model.User;
import com.example.bookstoreai.repository.CustomerRepository;
import com.example.bookstoreai.repository.UserRepository;
import com.example.bookstoreai.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String email) {
        User user = findUserByEmail(email);
        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user: " + email));

        return new UserProfileResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                user.getRole().getName()
        );
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = findUserByEmail(email);
        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user: " + email));

        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }

        customerRepository.save(customer);

        return new UserProfileResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                user.getRole().getName()
        );
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findUserByEmail(email);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
