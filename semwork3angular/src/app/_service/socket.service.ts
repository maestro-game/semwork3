import {Injectable, OnDestroy} from '@angular/core';
import {TokenService} from './token.service';
import {BehaviorSubject} from 'rxjs';

declare var SockJS;
declare var Stomp;

@Injectable()
export class SocketService implements OnDestroy {
  private stompClient;
  public isConnected = new BehaviorSubject<boolean>(false);

  constructor() {
    const ws = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({user: 'user'}, () => {
      this.isConnected.next(true);
    }, () => {
      this.isConnected.next(false);
    });
  }

  subscribe(dest: string, callback: (data) => any): any {
    return this.stompClient.subscribe(dest, callback);
  }

  send(dest: string, body: string): void {
    this.stompClient.send(dest, {}, body);
  }

  ngOnDestroy(): void {
    this.stompClient.disconnect();
  }
}
