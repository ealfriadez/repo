import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IngestionResponse } from '../../models/api.models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class IngestionService {
  private readonly baseUrl = environment.apiUrl + '/api/ingestion';

  constructor(private http: HttpClient) {}

  ingestBooks(): Observable<IngestionResponse> {
    return this.http.post<IngestionResponse>(`${this.baseUrl}/books`, null);
  }
}
