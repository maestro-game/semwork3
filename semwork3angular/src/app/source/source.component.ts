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

  rightClickMessage: InnerMessage;
  showRightClickMenu = false;
  rightClickTop = 0;
  rightClickLeft = 0;

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

  rightClick(event: MouseEvent, message: InnerMessage, type: number): boolean {
    this.rightClickMessage = message;
    this.rightClickTop = event.clientY;
    this.rightClickLeft = event.clientX;
    this.showRightClickMenu = true;
    document.body.addEventListener('click', () => {
      this.showRightClickMenu = false;
    });
    return false;
  }

  repost(): void {

  }

  delete(): void {
    this._source.messages.content.
  }

  copy(): void {
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', this.rightClickMessage.text);
      e.preventDefault();
      document.removeEventListener('copy', null, null);
    });
    document.execCommand('copy');
  }

  constructor(public tokenService: TokenService, private httpService: HttpService, private socketService: SocketService) {
  }
}
