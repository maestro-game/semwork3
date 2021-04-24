import {Component, OnInit} from '@angular/core';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {HttpService} from '../_service/http.service';
import {Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {CookieAuthService} from '../_service/cookie-auth.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {
  sources: PreviewSourceDto[];

  constructor(private cookieAuthService: CookieAuthService,
              private httpService: HttpService,
              public tokenService: TokenService,
              private router: Router) {
    this.httpService.getSourceList().subscribe(data => this.sources = data);
  }

  open(id: number): void {
    console.log(id);
  }
}
