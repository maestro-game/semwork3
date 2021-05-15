import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {HttpService} from './http.service';
import {TokenService} from './token.service';

@Injectable()
export class CookieAuthService {
  public readonly AUTH_COOKIE_NAME = 'auth';

  constructor(private cookieService: CookieService, private httpService: HttpService, private tokenService: TokenService) {
    const token = this.cookieService.get(this.AUTH_COOKIE_NAME);
    if (token) {
      this.tokenService.token = token;
      httpService.getUserDto().subscribe(user => {
          this.tokenService.user = user;
          this.tokenService.done = true;
        },
        () => this.tokenService.done = true);
    } else {
      this.tokenService.done = true;
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
