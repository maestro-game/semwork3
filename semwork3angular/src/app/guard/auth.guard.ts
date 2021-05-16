import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
import {CookieAuthService} from '../_service/cookie-auth.service';
import {TokenService} from '../_service/token.service';
import {Injectable} from '@angular/core';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private cookieAuthService: CookieAuthService, private tokenService: TokenService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return !!this.tokenService.token;
  }
}
