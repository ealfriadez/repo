export interface UserProfileResponse {
    firstName: string;
    lastName: string;
    phone: string;
    roleName: string;
}

export interface UpdateProfileRequest {
    firstName: string;
    lastName: string;
    phone: string;
}

export interface ChangePasswordRequest {
    currentPassword: string;
    newPassword: string;
}
