import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {AppSettings} from '../_config/global.config';
import {NgForm} from '@angular/forms';
import {TokenService} from './token.service';

@Injectable()
export class HttpService {
  private authHeader: HttpHeaders;

  constructor(private http: HttpClient, private tokenService: TokenService) {
  }

  private getAuthHeaderLazy(): HttpHeaders {
    return this.authHeader ? this.authHeader : this.authHeader = new HttpHeaders({Authorization: this.tokenService.token});
  }

  sendSignUpForm(form: NgForm): Observable<string> {
    return this.http.post(AppSettings.API_ENDPOINT + '/signUp', form.form.getRawValue()) as Observable<string>;
  }

  sendSignInForm(form: NgForm): Observable<any> {
    return this.http.post(AppSettings.API_ENDPOINT + '/signIn', form.form.getRawValue());
  }

  getSourceList(): Observable<PreviewSourceDto[]> {
    return this.http.get(AppSettings.API_ENDPOINT + '/channels',
      {headers: this.getAuthHeaderLazy()}) as Observable<Array<PreviewSourceDto>>;
  }
}
