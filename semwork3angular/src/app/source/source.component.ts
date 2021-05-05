import {Component, Input} from '@angular/core';
import {TokenService} from '../_service/token.service';
import {HttpService} from '../_service/http.service';
import {SourceDto} from '../_dto/source.dto';
import {SocketService} from '../_service/socket.service';

@Component({
  selector: 'app-source',
  templateUrl: './source.component.html',
  styleUrls: ['./source.component.css'],
})
export class SourceComponent {
  private _id: number;
  source: SourceDto;

  @Input()
  set id(id: number) {
    if (!id) { return; }
    this.socketService.isConnected.subscribe(value => {
      if (value) {
        this.socketService.isConnected.unsubscribe();
        this.socketService.subscribe('/user/main/channels/' + id + '/get', (response) => {
          this.source = JSON.parse(response.body);
        });
        this.socketService.send('/main/channels/' + id + '/get', null);
      }
    });
    this._id = id;
  }

  get id(): number {
    return this._id;
  }

  constructor(private tokenService: TokenService, private httpService: HttpService, private socketService: SocketService) {
  }
}
