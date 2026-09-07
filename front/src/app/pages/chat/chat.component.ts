import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MessageDTO, MessageService } from '../../services/message.service';
import { WebSocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './chat.component.html'
})
export class ChatComponent implements OnInit, OnDestroy {
  messages: MessageDTO[] = [];
  newMessage = '';
  clientId!: number;
  isAdmin = false;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private messageService: MessageService,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getRole() === 'admin';
    const paramId = this.route.snapshot.paramMap.get('clientId');
    this.clientId = paramId ? Number(paramId) : this.authService.getUserId()!;

    const conversation$ = this.isAdmin
      ? this.messageService.getConversationWithClient(this.clientId)
      : this.messageService.getConversation();

    conversation$.subscribe((messages) => (this.messages = messages));

    this.webSocketService.subscribeToConversation(this.clientId, (message) => {
      this.messages.push(message as MessageDTO);
    });
  }

  ngOnDestroy(): void {
    this.webSocketService.unsubscribeFromConversation(this.clientId);
  }

  onSubmit(): void {
    if (!this.newMessage.trim()) {
      return;
    }
    const destinataire = this.isAdmin ? this.clientId : undefined;
    this.messageService.sendMessage(this.newMessage, destinataire).subscribe(() => {
      this.newMessage = '';
    });
  }
}
