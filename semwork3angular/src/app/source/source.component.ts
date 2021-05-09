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
  _source: SourceDto;
  getTypeName = SourceDto.getTypeName;

  @Input()
  set source(source: SourceDto) {
    if (source) {
      this._source = source;
    }
  }

  messageClass(message: InnerMessage): string {
    if (!message.author) {
      return 'channel-message';
    }
    if (message.author.id === this.tokenService.user.id) {
      return 'my-message';
    }
    return 'user-message';
  }

  send(inputText: HTMLInputElement): void {
    if (inputText.value) {
      this.socketService.send('/main/channels/' + this._source.id + '/send', inputText.value);
      inputText.value = null;
    }
  }

  constructor(public tokenService: TokenService, private httpService: HttpService, private socketService: SocketService) {
  }
}
