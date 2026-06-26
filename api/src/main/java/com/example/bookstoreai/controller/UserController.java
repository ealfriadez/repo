package com.example.bookstoreai.controller;

import com.example.bookstoreai.dto.request.ChangePasswordRequest;
import com.example.bookstoreai.dto.request.UpdateProfileRequest;
import com.example.bookstoreai.dto.response.UserProfileResponse;
import com.example.bookstoreai.security.SecurityUtil;
import com.example.bookstoreai.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final SecurityUtil securityUtil;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> getProfile() {
        String email = securityUtil.getAuthenticatedEmail();
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        String email = securityUtil.getAuthenticatedEmail();
        return ResponseEntity.ok(userService.updateProfile(email, request));
    }

    @PutMapping("/profile/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        String email = securityUtil.getAuthenticatedEmail();
        userService.changePassword(email, request);
        return ResponseEntity.noContent().build();
    }
}
