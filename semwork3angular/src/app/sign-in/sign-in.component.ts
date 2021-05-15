import {Component} from '@angular/core';
import {NgForm} from '@angular/forms';
import {HttpService} from '../_service/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {Location} from '@angular/common';
import {CookieAuthService} from '../_service/cookie-auth.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  error: string;
  info: string;
  remember = false;

  constructor(private route: ActivatedRoute,
              private httpService: HttpService,
              private tokenService: TokenService,
              private router: Router,
              private location: Location,
              private cookieAuthService: CookieAuthService) {
    route.queryParams.subscribe((queryParam: any) => {
      this.info = queryParam.info;
      this.error = queryParam.error;
    });
    if (this.info || this.error) {
      location.replaceState('/signIn');
    }
  }

  submit(form: NgForm): void {
    this.error = null;
    this.info = null;
    this.httpService.sendSignInForm(form).subscribe(data => {
        this.tokenService.token = data.token;
        this.tokenService.user = data.user;
        this.tokenService.done = true;
        this.cookieAuthService.setAuthCookie(data.token, this.remember);
        this.router.navigate(['/im']);
      },
      error => {
        this.error = error.error;
        this.tokenService.done = true;
      });
  }
}
