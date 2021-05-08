import {Component} from '@angular/core';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {HttpService} from '../_service/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {CookieAuthService} from '../_service/cookie-auth.service';
import {SocketService} from '../_service/socket.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  sources: PreviewSourceDto[];
  id: number;

  constructor(private cookieAuthService: CookieAuthService,
              private httpService: HttpService,
              public tokenService: TokenService,
              private activateRoute: ActivatedRoute,
              private socketService: SocketService,
              private router: Router) {
    activateRoute.queryParams.subscribe(params => this.id = params.id);
    socketService.isConnected.subscribe((value => {
      if (value) {
        this.socketService.subscribe('/user/main/channels/get', (data) => {
          this.sources = JSON.parse(data.body);
        });
        this.socketService.send('/main/channels/get', null);
      }
    }));
  }

  open(id: number): void {
    this.router.navigate(['im'], {queryParams: {id}});
  }
}
