import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfileResponse, UpdateProfileRequest, ChangePasswordRequest } from '../../models/user.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private baseUrl = environment.apiUrl + '/api/users';

    constructor(private http: HttpClient) {}

    getProfile(): Observable<UserProfileResponse> {
        return this.http.get<UserProfileResponse>(`${this.baseUrl}/profile`);
    }

    updateProfile(request: UpdateProfileRequest): Observable<UserProfileResponse> {
        return this.http.put<UserProfileResponse>(`${this.baseUrl}/profile`, request);
    }

    changePassword(request: ChangePasswordRequest): Observable<void> {
        return this.http.put<void>(`${this.baseUrl}/profile/password`, request);
    }
}
