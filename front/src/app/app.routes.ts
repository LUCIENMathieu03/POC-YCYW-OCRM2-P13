import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ChatComponent } from './pages/chat/chat.component';
import { AdminConversationsComponent } from './pages/admin-conversations/admin-conversations.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'chat', component: ChatComponent },
  { path: 'admin/conversations', component: AdminConversationsComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
