import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatRequest, ChatResponse } from '../../models/ai.models';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly baseUrl = environment.apiUrl + '/api/ai';

  constructor(private http: HttpClient) {}

  chat(message: string, conversationId: string): Observable<ChatResponse> {
    const request: ChatRequest = { message, conversationId };
    return this.http.post<ChatResponse>(`${this.baseUrl}/chat`, request);
  }

  deleteConversation(conversationId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/conversations/${conversationId}`);
  }
}
