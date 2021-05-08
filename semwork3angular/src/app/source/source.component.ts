import {Component, Input} from '@angular/core';
import {TokenService} from '../_service/token.service';
import {HttpService} from '../_service/http.service';
import {SourceDto} from '../_dto/source.dto';
import {SocketService} from '../_service/socket.service';
import {InnerMessage} from '../_dto/inner-message.dto';

@Component({
  selector: 'app-source',
  templateUrl: './source.component.html',
  styleUrls: ['./source.component.css'],
})
export class SourceComponent {
  private _id: number;
  source: SourceDto;
  getTypeName = SourceDto.getTypeName;

  @Input()
  set id(id: number) {
    if (!id) {
      return;
    }
    this.socketService.isConnected.subscribe(value => {
      if (value) {
        this.socketService.subscribe('/main/channels/' + id, (response) => {
          this.source.messages.push(JSON.parse(response.body));
        });
        this.socketService.subscribe('/user/main/channels/' + id + '/get', (response) => {
          this.source = JSON.parse(response.body);
          this.source.messages = this.source.messages.reverse();
        });
        this.socketService.send('/main/channels/' + id + '/get', null);
      }
    });
    this._id = id;
  }

  get id(): number {
    return this._id;
  }

  messageClass(message: InnerMessage): string {
    if (!message.author) { return 'channel-message'; }
    if (message.author.id === this.tokenService.user.id) { return 'my-message'; }
    return 'user-message';
  }

  send(inputText: HTMLInputElement): void {
    if (inputText.value) {
      this.socketService.send('/main/channels/' + this.source.id + '/send', inputText.value);
    }
  }

  constructor(public tokenService: TokenService, private httpService: HttpService, private socketService: SocketService) {
  }
}
