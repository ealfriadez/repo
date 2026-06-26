package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.ChangePasswordRequest;
import com.example.bookstoreai.dto.request.UpdateProfileRequest;
import com.example.bookstoreai.dto.response.UserProfileResponse;

public interface IUserService {
    UserProfileResponse getProfile(String email);
    UserProfileResponse updateProfile(String email, UpdateProfileRequest request);
    void changePassword(String email, ChangePasswordRequest request);
}
