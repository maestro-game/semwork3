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
    return this.http.post(AppSettings.API_ENDPOINT + '/signIn', form.form.getRawValue());
  }
}
