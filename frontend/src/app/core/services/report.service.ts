import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MonthlySalesReport, TopBookReport } from '../../models/report.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly baseUrl = environment.apiUrl + '/api/reports';

  constructor(private http: HttpClient) {}

  getMonthlySales(dateFrom: string, dateTo: string): Observable<MonthlySalesReport[]> {
    return this.http.get<MonthlySalesReport[]>(`${this.baseUrl}/monthly-sales`, {
      params: { dateFrom, dateTo }
    });
  }

  getTopBooks(dateFrom: string, dateTo: string, limit = 5): Observable<TopBookReport[]> {
    return this.http.get<TopBookReport[]>(`${this.baseUrl}/top-books`, {
      params: { dateFrom, dateTo, limit: limit.toString() }
    });
  }
}
