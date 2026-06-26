import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SaleRequest, SaleResponse, PageResponse } from '../../models/api.models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SaleService {
  private readonly baseUrl = environment.apiUrl + '/api/sales';

  constructor(private http: HttpClient) {}

  findAll(page = 0, size = 10, filters?: { dateFrom?: string; dateTo?: string }): Observable<PageResponse<SaleResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filters?.dateFrom) params = params.set('dateFrom', filters.dateFrom);
    if (filters?.dateTo) params = params.set('dateTo', filters.dateTo);
    return this.http.get<PageResponse<SaleResponse>>(this.baseUrl, { params });
  }

  findById(id: number): Observable<SaleResponse> {
    return this.http.get<SaleResponse>(`${this.baseUrl}/${id}`);
  }

  create(request: SaleRequest): Observable<SaleResponse[]> {
    return this.http.post<SaleResponse[]>(this.baseUrl, request);
  }
}
