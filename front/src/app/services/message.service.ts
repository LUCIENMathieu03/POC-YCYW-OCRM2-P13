import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../api-config';

export interface MessageDTO {
  id: number;
  content: string;
  dateEnvoi: string;
  senderId: number;
  senderName: string;
  senderEmail: string;
  destinataire: number | null;
}

export interface ClientDTO {
  id: number;
  email: string;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class MessageService {
  constructor(private http: HttpClient) {}

  getConversation(): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>(`${API_BASE_URL}/messages/conversation`);
  }

  getConversationWithClient(clientId: number): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>(`${API_BASE_URL}/messages/conversation/${clientId}`);
  }

  sendMessage(content: string, destinataire?: number): Observable<MessageDTO> {
    return this.http.post<MessageDTO>(`${API_BASE_URL}/messages`, { content, destinataire });
  }

  getAdminConversations(): Observable<ClientDTO[]> {
    return this.http.get<ClientDTO[]>(`${API_BASE_URL}/admin/conversations`);
  }
}
