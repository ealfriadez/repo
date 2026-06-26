package com.example.bookstoreai.security;

import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.model.Customer;
import com.example.bookstoreai.model.User;
import com.example.bookstoreai.repository.CustomerRepository;
import com.example.bookstoreai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User getAuthenticatedUser() {
        String email = getAuthenticatedEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public Customer getAuthenticatedCustomer() {
        User user = getAuthenticatedUser();
        return customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user: " + user.getEmail()));
    }

    public boolean isOwner() {
        return hasRole("ROLE_OWNER");
    }

    public boolean isCustomer() {
        return hasRole("ROLE_CUSTOMER");
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
