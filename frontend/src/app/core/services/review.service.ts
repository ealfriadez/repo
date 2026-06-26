import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReviewRequest, ReviewResponse, PageResponse } from '../../models/api.models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReviewService {
  private readonly baseUrl = environment.apiUrl + '/api/reviews';

  constructor(private http: HttpClient) {}

  findById(id: number): Observable<ReviewResponse> {
    return this.http.get<ReviewResponse>(`${this.baseUrl}/${id}`);
  }

  getByBook(bookId: number): Observable<ReviewResponse[]> {
    return this.http.get<ReviewResponse[]>(`${this.baseUrl}/book/${bookId}`);
  }

  getByBookPaginated(bookId: number, page: number, size: number): Observable<PageResponse<ReviewResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<ReviewResponse>>(`${this.baseUrl}/book/${bookId}`, { params });
  }

  create(request: ReviewRequest): Observable<ReviewResponse> {
    return this.http.post<ReviewResponse>(this.baseUrl, request);
  }

  update(id: number, request: ReviewRequest): Observable<ReviewResponse> {
    return this.http.put<ReviewResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
