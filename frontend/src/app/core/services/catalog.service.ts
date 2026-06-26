import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookResponse, PageResponse } from '../../models/api.models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly baseUrl = environment.apiUrl + '/api/catalog';

  constructor(private http: HttpClient) {}

  searchBooks(search: string, page = 0, size = 12): Observable<PageResponse<BookResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<PageResponse<BookResponse>>(`${this.baseUrl}/books`, { params });
  }

  findByGenre(genre: string, page = 0, size = 12): Observable<PageResponse<BookResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<BookResponse>>(`${this.baseUrl}/books/genre/${genre}`, { params });
  }
}
