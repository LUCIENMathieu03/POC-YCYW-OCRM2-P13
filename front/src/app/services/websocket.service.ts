import { Injectable } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { WS_URL } from '../api-config';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private client: Client;
  private subscriptions = new Map<string, StompSubscription>();

  constructor() {
    this.client = new Client({
      brokerURL: WS_URL,
      reconnectDelay: 5000
    });
    this.client.activate();
  }

  subscribeToConversation(clientId: number, callback: (message: unknown) => void): void {
    const topic = `/topic/conversation/${clientId}`;

    const doSubscribe = () => {
      this.unsubscribeFromConversation(clientId);
      const subscription = this.client.subscribe(topic, (message: IMessage) => {
        callback(JSON.parse(message.body));
      });
      this.subscriptions.set(topic, subscription);
    };

    if (this.client.connected) {
      doSubscribe();
    } else {
      this.client.onConnect = doSubscribe;
    }
  }

  unsubscribeFromConversation(clientId: number): void {
    const topic = `/topic/conversation/${clientId}`;
    this.subscriptions.get(topic)?.unsubscribe();
    this.subscriptions.delete(topic);
  }
}
