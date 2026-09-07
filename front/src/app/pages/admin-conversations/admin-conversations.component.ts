import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientDTO, MessageService } from '../../services/message.service';

@Component({
  selector: 'app-admin-conversations',
  standalone: true,
  templateUrl: './admin-conversations.component.html'
})
export class AdminConversationsComponent implements OnInit {
  clients: ClientDTO[] = [];

  constructor(
    private messageService: MessageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.messageService.getAdminConversations().subscribe((clients) => (this.clients = clients));
  }

  openConversation(clientId: number): void {
    this.router.navigate(['/admin/chat', clientId]);
  }
}
