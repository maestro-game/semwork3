import {Component} from '@angular/core';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {HttpService} from '../_service/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {SocketService} from '../_service/socket.service';
import {FormControl, FormGroup, NgForm} from '@angular/forms';
import {$} from 'protractor';
import {InnerMessage} from '../_dto/inner-message.dto';
import {SourceDto} from '../_dto/source.dto';
import {ResponseDto} from '../_dto/response.dto';
import {TitleSource} from '../_dto/title-source.dto';
import {TempSourceDto} from '../_dto/temp-source.dto';
import {AppSettings} from '../_config/global.config';
import {CookieAuthService} from '../_service/cookie-auth.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  sources: PreviewSourceDto[];
  foundSources: TitleSource[];
  currentSource: SourceDto;
  selectedMessage: InnerMessage;
  imageFormGroup: FormGroup = new FormGroup({
    file: new FormControl()
  });
  API_ENDPOINT = AppSettings.API_ENDPOINT;
  createGroupError: string;
  change: boolean;
  tempSub: any;
  readonly BASE64: string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_';

  constructor(private httpService: HttpService,
              public tokenService: TokenService,
              private activateRoute: ActivatedRoute,
              private socketService: SocketService,
              private router: Router,
              private cookieAuthService: CookieAuthService) {
    tokenService.done.subscribe((done) => {
      if (!done) {
        return;
      }
      if (!tokenService.user.id) {
        this.router.navigate(['signIn']);
        return;
      }
      socketService.start();
      socketService.isConnected.subscribe((value => {
        if (value) {
          this.socketService.subscribe('/user/main/channels/current/get', (response) => {
            const curSource: SourceDto = JSON.parse(response.body);
            if (!this.sources.find((val) => val.id === curSource.id)) {
              if (curSource.sourceType === 0) {
                this.openFound(curSource);
              } else {
                this.changeCurrentSource(new TempSourceDto(curSource));
                this.temporarySubscribeForSource(curSource.id);
              }
            } else {
              this.changeCurrentSource(curSource);
            }
          });
          this.socketService.subscribe('/user/main/channels/leave', (response) => {
            const respId = JSON.parse(response.body);
            this.sources = this.sources.filter(obj => {
              if (obj.id === respId) {
                if (this.currentSource.id === respId) {
                  this.router.navigate(['im']);
                  this.changeCurrentSource(null);
                }
                obj.subscription.unsubscribe();
                return false;
              }
              return true;
            });
          });
          this.socketService.subscribe('/user/main/channels/join', (response) => {
            const preview: PreviewSourceDto = JSON.parse(response.body);
            if (!preview.name) {
              preview.name = this.calculateName(preview.id);
            }
            this.subscribeForSource(preview);
            this.sources.push(preview);
            this.sources = this.sources.sort((a, b) =>
              a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
          });
          const getAllSub = this.socketService.subscribe('/user/main/channels/get', (data) => {
            getAllSub.unsubscribe();
            this.sources = JSON.parse(data.body);
            this.sources.forEach((dto) => {
              this.subscribeForSource(dto);
              if (!dto.name) {
                dto.name = this.calculateName(dto.id);
              }
            });
          });
          this.socketService.send('/main/channels/get', null);
          this.socketService.subscribe('/user/main/channels/search', (data => {
            this.foundSources = JSON.parse(data.body);
            this.foundSources.forEach((source) => {
              if (!source.name) {
                source.name = this.calculateName(source.id);
              }
            });
          }));
          activateRoute.queryParams.subscribe(params => {
            if (params.id && (!this.currentSource || this.currentSource.id !== params.id)) {
              this.socketService.send('/main/channels/current/get', params.id);
            }
          });
        }
      }));
      this.changeCurrentSource(undefined);
      eval('$(document).ready(function () {\n' +
        '  $(\'#searchModal\').on(\'shown.bs.modal\', function () {\n' +
        '    $(\'#searchInput\').focus();\n' +
        '  });\n' +
        '});');
    });
  }

  calculateName(id: string): string {
    const lenIndex = id.search('_*$') - 1;
    const len = this.BASE64.indexOf(id.charAt(lenIndex)) + 1;
    const id1 = id.substr(0, len);
    return '@' + (id1 === this.tokenService.user.id ? id.substr(len, lenIndex - len) : id1);
  }

  changeCurrentSource(newSource: SourceDto): void {
    if (this.tempSub) {
      this.tempSub.unsubscribe();
      this.tempSub = null;
    }
    if (newSource && !newSource.name) {
      newSource.name = this.calculateName(newSource.id);
    }
    this.currentSource = newSource;
  }

  setSelectedMessage(message: InnerMessage): void {
    this.selectedMessage = message;
  }

  open(id: string): void {
    if (this.selectedMessage) {
      this.socketService.send('/main/messages/' + id + '/repost/' + this.selectedMessage.id, null);
      this.selectedMessage = null;
      this.change = !this.change;
    }
    this.router.navigate(['im'], {queryParams: {id}});
  }

  temporarySubscribeForSource(id: string): void {
    this.tempSub = this.socketService.subscribe('/main/channels/' + id, (response) => {
      const resp: ResponseDto<any> = JSON.parse(response.body);
      if (resp.type === 1) {
        if (id === this.currentSource.id) {
          this.currentSource.messages.content.push(resp.payload);
        }
      } else if (resp.type === 2) {
        if (id === this.currentSource.id) {
          this.currentSource.messages.content = this.currentSource.messages.content.filter(mes => mes.id !== resp.payload.id);
        }
      } else if (resp.type === 3) {
        if (this.currentSource.id === resp.payload) {
          this.router.navigate(['im']);
          this.changeCurrentSource(null);
        }
      } else if (resp.type === 4) {
        if (id === this.currentSource.id) {
          this.currentSource.avatarImageUrl = resp.payload;
        }
      }
    });
  }

  subscribeForSource(dto: PreviewSourceDto): void {
    if (this.tempSub && this.currentSource.id === dto.id) {
      this.tempSub.unsubscribe();
      this.tempSub = null;
    }
    dto.subscription = this.socketService.subscribe('/main/channels/' + dto.id, (response) => {
      const resp: ResponseDto<any> = JSON.parse(response.body);
      if (resp.type === 1) {
        if (dto.id === this.currentSource.id) {
          this.currentSource.messages.content.push(resp.payload);
        }
        dto.lastMessageShortText = resp.payload.text;
        dto.lastMessageTimestamp = resp.payload.created;
        this.sources = this.sources.sort((a, b) =>
          a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
      } else if (resp.type === 2) {
        if (dto.id === this.currentSource.id) {
          this.currentSource.messages.content = this.currentSource.messages.content.filter(mes => mes.id !== resp.payload.id);
        }
        dto.lastMessageTimestamp = resp.payload.lastMessageTimestamp;
        dto.lastMessageShortText = resp.payload.lastMessageShortText;
        this.sources = this.sources.sort((a, b) =>
          a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
      } else if (resp.type === 3) {
        this.sources = this.sources.filter(obj => {
          if (obj.id === resp.payload) {
            if (this.currentSource.id === resp.payload) {
              this.router.navigate(['im']);
              this.changeCurrentSource(null);
            }
            obj.subscription.unsubscribe();
            return false;
          }
          return true;
        });
      } else if (resp.type === 4) {
        if (dto.id === this.currentSource.id) {
          this.currentSource.avatarImageUrl = resp.payload;
        }
        dto.avatarImageUrl = resp.payload;
      }
    });
  }

  search(value: string): void {
    if (value) {
      this.socketService.send('/main/channels/search', value);
    }
  }

  openFound(foundSource: TitleSource): void {
    if (foundSource.sourceType === 0) {
      if (!confirm('Присоединиться к группе ' + foundSource.id + '?')) {
        return;
      }
      const sub = this.socketService.subscribe('/user/main/channels/join', () => {
        sub.unsubscribe();
        this.socketService.send('/main/channels/current/get', foundSource.id);
        eval('$("#searchModal").modal("hide")');
      });
      this.socketService.send('/main/channels/' + foundSource.id + '/join', 'null');
    } else {
      this.socketService.send('/main/channels/current/get', foundSource.id);
      eval('$("#searchModal").modal("hide")');
    }
  }

  createGroup(form: NgForm, isChannel: boolean): void {
    const createSub = this.socketService.subscribe('/user/main/channels/create', (data => {
      createSub.unsubscribe();
      this.changeCurrentSource(JSON.parse(data.body));
      if (this.currentSource) {
        eval(isChannel ? '$("#createChannelModal").modal("hide")' : '$("#createGroupModal").modal("hide")');
        form.reset();
        const dto = {
          avatarImageUrl: this.currentSource.avatarImageUrl,
          id: this.currentSource.id,
          lastMessageShortText: this.currentSource.messages.content[0].text,
          lastMessageTimestamp: this.currentSource.messages.content[0].created,
          name: this.currentSource.name,
          subscription: null
        };
        this.sources.push(dto);
        this.subscribeForSource(dto);
        this.sources = this.sources.sort((a, b) =>
          a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
        this.router.navigate(['im'], {queryParams: {id: this.currentSource.id}});
      } else {
        this.createGroupError = `Не удалось создать ${isChannel ? 'канал' : 'группу'}`;
      }
    }));
    const body = form.form.getRawValue();
    body.sourceType = +isChannel;
    this.socketService.send('/main/channels/create', JSON.stringify(body as string));
  }

  upload(event): void {
    const file = (event.target as HTMLInputElement).files[0];
    this.imageFormGroup.patchValue({file});
    this.imageFormGroup.get('file').updateValueAndValidity();
  }

  sendImage(): void {
    const formData = new FormData();
    formData.append('file', this.imageFormGroup.get('file').value);

    this.httpService.sendImageForm(formData).subscribe(data => {
        this.tokenService.user.photoUrl = data;
        this.imageFormGroup.reset();
      },
      () => window.alert('ошибка при загрузке изображения'));
  }

  logout(): void {
    this.httpService.sendLogout().subscribe(() => {
        eval('$("#profileModal").modal("hide")');
        this.cookieAuthService.deleteAuthCookie();
        this.router.navigate(['signIn']);
      },
      (error) => alert(error));
  }
}
