import {Component} from '@angular/core';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {HttpService} from '../_service/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {CookieAuthService} from '../_service/cookie-auth.service';
import {SocketService} from '../_service/socket.service';
import {NgForm} from '@angular/forms';
import {$} from 'protractor';
import {InnerMessage} from '../_dto/inner-message.dto';
import {SourceDto} from '../_dto/source.dto';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  sources: PreviewSourceDto[];
  currentSource: SourceDto;
  createGroupError: string;

  constructor(private cookieAuthService: CookieAuthService,
              private httpService: HttpService,
              public tokenService: TokenService,
              private activateRoute: ActivatedRoute,
              private socketService: SocketService,
              private router: Router) {
    socketService.isConnected.subscribe((value => {
      if (value) {
        activateRoute.queryParams.subscribe(params => {
          if (!this.currentSource || this.currentSource.id !== params.id) {
            this.socketService.subscribe('/user/main/channels/' + params.id + '/get', (response) => {
              this.socketService.unsubscribe('/user/main/channels/' + params.id + '/get');
              this.currentSource = JSON.parse(response.body);
              // this.currentSource.messages = this.currentSource.messages.reverse();
            });
            this.socketService.send('/main/channels/' + params.id + '/get', null);
          }
        });
        this.socketService.subscribe('/user/main/channels/get', (data) => {
          this.socketService.unsubscribe('/user/main/channels/get');
          this.sources = JSON.parse(data.body);
          // this.sources = this.sources.reverse();
          this.sources.forEach((dto) => this.subscribeForSource(dto));
        });
        this.socketService.send('/main/channels/get', null);
      }
    }));
    this.currentSource = undefined;
  }

  open(id: number): void {
    this.router.navigate(['im'], {queryParams: {id}});
  }

  subscribeForSource(dto: PreviewSourceDto): void {
    this.socketService.subscribe('/main/channels/' + dto.id, (response) => {
      const mes: InnerMessage = JSON.parse(response.body);
      if (dto.id === this.currentSource.id) {
        this.currentSource.messages.push(mes);
      }
      dto.lastMessageShortText = mes.text;
      dto.lastMessageTimestamp = mes.created;
      this.sources = this.sources.sort((a, b) =>
        a.lastMessageTimestamp === b.lastMessageTimestamp ? 0 : a.lastMessageTimestamp < b.lastMessageTimestamp ? 1 : -1);
    });
  }

  createChannel(): void {

  }

  createGroup(form: NgForm): void {
    console.error('submit');
    this.socketService.subscribe('/user/main/channels/create', (data => {
      this.socketService.unsubscribe('/user/main/channels/create');
      this.currentSource = JSON.parse(data.body);
      if (this.currentSource) {
        eval('$("#createGroupModal").modal("hide")');
        form.reset();
        const dto = {
          avatarImageUrl: this.currentSource.avatarImageUrl,
          id: this.currentSource.id,
          lastMessageShortText: this.currentSource.messages[0].text,
          lastMessageTimestamp: this.currentSource.messages[0].created,
          name: this.currentSource.name
        };
        this.sources.push(dto);
        this.subscribeForSource(dto);
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
