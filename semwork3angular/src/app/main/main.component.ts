import {Component} from '@angular/core';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {HttpService} from '../_service/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {SocketService} from '../_service/socket.service';
import {NgForm} from '@angular/forms';
import {$} from 'protractor';
import {InnerMessage} from '../_dto/inner-message.dto';
import {SourceDto} from '../_dto/source.dto';
import {ResponseDto} from '../_dto/response.dto';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  sources: PreviewSourceDto[];
  foundSources: PreviewSourceDto[];
  currentSource: SourceDto;
  selectedMessage: InnerMessage;
  createGroupError: string;
  change: boolean;

  constructor(private httpService: HttpService,
              public tokenService: TokenService,
              private activateRoute: ActivatedRoute,
              private socketService: SocketService,
              private router: Router) {
    socketService.isConnected.subscribe((value => {
      if (value) {
        this.socketService.subscribe('/user/main/channels/current/get', (response) => {
          this.currentSource = JSON.parse(response.body);
          if (!this.sources.find((val) => val.id === this.currentSource.id)) {
            const preview = {
              avatarImageUrl: this.currentSource.avatarImageUrl,
              id: this.currentSource.id,
              lastMessageShortText: this.currentSource.messages.content[this.currentSource.messages.content.length - 1].text,
              lastMessageTimestamp: this.currentSource.messages.content[this.currentSource.messages.content.length - 1].created,
              name: this.currentSource.name
            };
            this.subscribeForSource(preview);
            this.sources.push(preview);
            this.sources = this.sources.sort((a, b) =>
              a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
          }
        });
        this.socketService.subscribe('/user/main/channels/join', (response) => {
          eval('$("#searchModal").modal("hide")');
          const joinId = response.body;
          this.router.navigate(['im'], {queryParams: {id: joinId}});
        });
        const getAllSub = this.socketService.subscribe('/user/main/channels/get', (data) => {
          getAllSub.unsubscribe();
          this.sources = JSON.parse(data.body);
          this.sources.forEach((dto) => this.subscribeForSource(dto));
        });
        this.socketService.send('/main/channels/get', null);
        this.socketService.subscribe('/user/main/channels/search', (data => {
          this.foundSources = JSON.parse(data.body);
        }));
        activateRoute.queryParams.subscribe(params => {
          if (params.id && (!this.currentSource || this.currentSource.id !== params.id)) {
            this.socketService.send('/main/channels/current/get', params.id);
          }
        });
      }
    }));
    this.currentSource = undefined;
    eval('$(document).ready(function () {\n' +
      '  $(\'#searchModal\').on(\'shown.bs.modal\', function () {\n' +
      '    $(\'#searchInput\').focus();\n' +
      '  });\n' +
      '});');
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

  subscribeForSource(dto: PreviewSourceDto): void {
    this.socketService.subscribe('/main/channels/' + dto.id, (response) => {
      const resp: ResponseDto<any> = JSON.parse(response.body);
      if (resp.type === 1) {
        if (dto.id === this.currentSource.id) {
          this.currentSource.messages.content.push(resp.payload);
        }
        dto.lastMessageShortText = resp.payload.text;
        dto.lastMessageTimestamp = resp.payload.created;
      } else {
        if (dto.id === this.currentSource.id) {
          this.currentSource.messages.content = this.currentSource.messages.content.filter(mes => mes.id !== resp.payload.id);
        }
        dto.lastMessageTimestamp = resp.payload.lastMessageTimestamp;
        dto.lastMessageShortText = resp.payload.lastMessageShortText;
      }
      this.sources = this.sources.sort((a, b) =>
        a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
    });
  }

  search(value: string): void {
    if (value) {
      this.socketService.send('/main/channels/search', value);
    }
  }

  join(id: string): void {
    this.socketService.send('/main/channels/' + id + '/join', null);
  }

  createChannel(): void {

  }

  createGroup(form: NgForm): void {
    const createSub = this.socketService.subscribe('/user/main/channels/create', (data => {
      createSub.unsubscribe();
      this.currentSource = JSON.parse(data.body);
      if (this.currentSource) {
        eval('$("#createGroupModal").modal("hide")');
        form.reset();
        const dto = {
          avatarImageUrl: this.currentSource.avatarImageUrl,
          id: this.currentSource.id,
          lastMessageShortText: this.currentSource.messages.content[0].text,
          lastMessageTimestamp: this.currentSource.messages.content[0].created,
          name: this.currentSource.name
        };
        this.sources.push(dto);
        this.subscribeForSource(dto);
        this.sources = this.sources.sort((a, b) =>
          a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
        this.router.navigate(['im'], {queryParams: {id: this.currentSource.id}});
      } else {
        this.createGroupError = 'Не удалось создать группу';
      }
    }));
    const body = form.form.getRawValue();
    body.sourceType = 0;
    this.socketService.send('/main/channels/create', JSON.stringify(body as string));
  }
}
