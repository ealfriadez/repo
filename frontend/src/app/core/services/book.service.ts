import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookRequest, BookResponse, PageResponse } from '../../models/api.models';
import { BulkUploadResponse } from '../../models/book.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class BookService {
  private readonly baseUrl = environment.apiUrl + '/api/books';

  constructor(private http: HttpClient) {}

  findAll(page = 0, size = 10, search?: string): Observable<PageResponse<BookResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<PageResponse<BookResponse>>(this.baseUrl, { params });
  }

  findById(id: number): Observable<BookResponse> {
    return this.http.get<BookResponse>(`${this.baseUrl}/${id}`);
  }

  create(request: BookRequest, image?: File, bookFile?: File): Observable<BookResponse> {
    const formData = new FormData();
    formData.append('book', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    if (bookFile) {
      formData.append('file', bookFile);
    }
    return this.http.post<BookResponse>(this.baseUrl, formData);
  }

  update(id: number, request: BookRequest, image?: File, bookFile?: File): Observable<BookResponse> {
    const formData = new FormData();
    formData.append('book', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    if (bookFile) {
      formData.append('file', bookFile);
    }
    return this.http.put<BookResponse>(`${this.baseUrl}/${id}`, formData);
  }

  getMyBooks(page = 0, search?: string): Observable<PageResponse<BookResponse>> {
    let params = new HttpParams().set('page', page).set('size', 12);
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<PageResponse<BookResponse>>(`${this.baseUrl}/my-books`, { params });
  }

  downloadBook(bookId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${bookId}/download`, { responseType: 'blob' });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  findBySlug(slug: string): Observable<BookResponse> {
    return this.http.get<BookResponse>(`${this.baseUrl}/slug/${slug}`);
  }

  bulkUpload(file: File): Observable<BulkUploadResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<BulkUploadResponse>(`${this.baseUrl}/bulk-upload`, formData);
  }
}
