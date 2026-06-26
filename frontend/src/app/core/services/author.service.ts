import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthorRequest, AuthorResponse, PageResponse } from '../../models/api.models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthorService {
  private readonly baseUrl = environment.apiUrl + '/api/authors';

  constructor(private http: HttpClient) {}

  findAll(page = 0, size = 10, search?: string): Observable<PageResponse<AuthorResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<PageResponse<AuthorResponse>>(this.baseUrl, { params });
  }

  findById(id: number): Observable<AuthorResponse> {
    return this.http.get<AuthorResponse>(`${this.baseUrl}/${id}`);
  }

  create(request: AuthorRequest): Observable<AuthorResponse> {
    return this.http.post<AuthorResponse>(this.baseUrl, request);
  }

  update(id: number, request: AuthorRequest): Observable<AuthorResponse> {
    return this.http.put<AuthorResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
