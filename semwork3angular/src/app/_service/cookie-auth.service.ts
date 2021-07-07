import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {HttpService} from './http.service';
import {TokenService} from './token.service';
import {Router} from '@angular/router';

@Injectable()
export class CookieAuthService {
  public readonly AUTH_COOKIE_NAME = 'auth';

  constructor(private cookieService: CookieService,
              private httpService: HttpService,
              private tokenService: TokenService,
              private router: Router) {
    const token = this.cookieService.get(this.AUTH_COOKIE_NAME);
    if (token) {
      this.tokenService.token = token;
      httpService.getUserDto().subscribe(user => {
          router.navigateByUrl((router.url.indexOf('?') > 0 ? '/im?' + router.url.split('?')[1] : '/im'));
          this.tokenService.user = user;
          this.tokenService.done.next(true);
        },
        () => {
          this.tokenService.done.next(true);
        });
    } else {
      this.tokenService.done.next(true);
    }
  }

  setAuthCookie(value: string, remember?: boolean): void {
    if (remember) {
      this.cookieService.set(this.AUTH_COOKIE_NAME, value, {expires: 90, path: '/'});
    } else {
      this.cookieService.set(this.AUTH_COOKIE_NAME, value, {path: '/'});
    }
  }

  deleteAuthCookie(): void {
    this.cookieService.delete(this.AUTH_COOKIE_NAME);
  }
}
