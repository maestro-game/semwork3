import {Component, Input, Output, EventEmitter} from '@angular/core';
import {TokenService} from '../_service/token.service';
import {HttpService} from '../_service/http.service';
import {SourceDto} from '../_dto/source.dto';
import {SocketService} from '../_service/socket.service';
import {InnerMessage} from '../_dto/inner-message.dto';
import {TempSourceDto} from '../_dto/temp-source.dto';
import {Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-source',
  templateUrl: './source.component.html',
  styleUrls: ['./source.component.css'],
})
export class SourceComponent {
  _source: SourceDto;
  getTypeName = SourceDto.getTypeName;
  sourceAvatarSendGroup: FormGroup = new FormGroup({
    file: new FormControl()
  });

  rightClickMessage: InnerMessage;
  isReposting = false;
  showRightClickMenu = false;
  rightClickTop = 0;
  rightClickLeft = 0;

  constructor(public tokenService: TokenService,
              private httpService: HttpService,
              private socketService: SocketService,
              private router: Router) {
  }

  @Input()
  set source(source: SourceDto) {
    if (source !== undefined) {
      this._source = source;
    }
  }

  @Input()
  set reposting(isReposting: boolean) {
    this.isReposting = false;
  }

  @Output() selectedMessage = new EventEmitter();

  messageClass(message: InnerMessage): number {
    if (!message.author) {
      return 0;
    }
    if (message.author.id === this.tokenService.user.id) {
      return 2;
    }
    return 1;
  }

  messageClassName(message: InnerMessage): string {
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
      if (this._source instanceof TempSourceDto) {
        const sub = this.socketService.subscribe('/user/main/channels/join', (response) => {
          sub.unsubscribe();
          this.router.navigate(['im'], {queryParams: {id: JSON.parse(response.body).id}});
        });
        this.socketService.send('/main/channels/' + this._source.id + '/join', inputText.value);
      } else {
        this.socketService.send('/main/messages/' + this._source.id + '/send', inputText.value);
      }
      inputText.value = null;
    }
  }

  upload(event): void {
    const file = (event.target as HTMLInputElement).files[0];
    this.sourceAvatarSendGroup.patchValue({file});
    this.sourceAvatarSendGroup.get('file').updateValueAndValidity();
  }

  sendSourceAvatar(id: string): void {
    const formData = new FormData();
    formData.append('file', this.sourceAvatarSendGroup.get('file').value);
    formData.append('id', id);

    this.httpService.sendSourceAvatarForm(formData).subscribe(() => {
        this.sourceAvatarSendGroup.reset();
      },
      () => window.alert('ошибка при загрузке изображения'));
  }

  leave(id: string): void {
    eval('$("#sourceModal").modal("hide")');
    this.socketService.send('/main/channels/' + id + '/leave', null);
  }

  deleteSource(id: string): void {
    eval('$("#sourceModal").modal("hide")');
    this.socketService.send('/main/channels/' + id + '/delete', null);
  }

  subscribe(): void {
    const sub = this.socketService.subscribe('/user/main/channels/join', (response) => {
      sub.unsubscribe();
      this.router.navigate(['im'], {queryParams: {id: JSON.parse(response.body).id}});
      if (this._source instanceof TempSourceDto) {
        this._source.isTemp = false;
      }
    });
    this.socketService.send('/main/channels/' + this._source.id + '/join', 'ignore');
  }

  rightClick(event: MouseEvent, message: InnerMessage, type: number): boolean {
    if (type === 0 || type === 5) {
      return true;
    }
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
    this.isReposting = true;
    this.selectedMessage.emit(this.rightClickMessage);
  }

  cancelRepost(): void {
    this.isReposting = false;
    this.selectedMessage.emit(null);
  }

  delete(): void {
    this.socketService.send('/main/messages/' + this._source.id + '/' + this.rightClickMessage.id + '/delete', null);
  }

  copy(): void {
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', this.rightClickMessage.text);
      e.preventDefault();
      document.removeEventListener('copy', null, null);
    });
    document.execCommand('copy');
  }
}
