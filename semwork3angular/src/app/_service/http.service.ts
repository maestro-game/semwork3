import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AppSettings} from '../_config/global.config';
import {NgForm} from '@angular/forms';
import {TokenService} from './token.service';
import {UserDto} from '../_dto/user.dto';

@Injectable()
export class HttpService {
  constructor(private http: HttpClient, private tokenService: TokenService) {
  }

  private getAuthHeader(): HttpHeaders {
    return new HttpHeaders({Authorization: this.tokenService.token});
  }

  getUserDto(): Observable<UserDto> {
    this.getAuthHeader();
    return this.http.get(AppSettings.API_ENDPOINT + '/profile',
      {headers: this.getAuthHeader()}) as Observable<UserDto>;
  }

  sendSignUpForm(form: NgForm): Observable<string> {
    return this.http.post(AppSettings.API_ENDPOINT + '/signUp', form.form.getRawValue()) as Observable<string>;
  }

  sendSignInForm(form: NgForm): Observable<any> {
    const data = form.form.getRawValue();
    if (data.remember !== 'true') {
      data.remember = 'false';
    }
    return this.http.post(AppSettings.API_ENDPOINT + '/signIn', data);
  }

  sendImageForm(form: FormData): Observable<string> {
    return this.http.post(AppSettings.API_ENDPOINT + '/profile', form,
      {headers: {Authorization: this.tokenService.token}}) as Observable<string>;
  }

  sendSourceAvatarForm(form: FormData): Observable<string> {
    return this.http.post(AppSettings.API_ENDPOINT + '/channels', form,
      {headers: {Authorization: this.tokenService.token}}) as Observable<string>;
  }

  sendLogout(): Observable<string> {
    return this.http.get(AppSettings.API_ENDPOINT + '/logout', {headers: {Authorization: this.tokenService.token}}) as Observable<string>;
  }
}
