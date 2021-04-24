import {Component} from '@angular/core';
import {NgForm} from '@angular/forms';
import {HttpService} from '../_service/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenService} from '../_service/token.service';
import {Location} from '@angular/common';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  error: string;
  info: string;

  constructor(private route: ActivatedRoute,
              private httpService: HttpService,
              private tokenService: TokenService,
              private router: Router,
              private location: Location) {
    route.queryParams.subscribe((queryParam: any) => this.info = queryParam.info);
    if (this.info) {
      location.replaceState('/signIn');
    }
  }

  submit(form: NgForm): void {
    this.error = null;
    this.info = null;
    this.httpService.sendSignInForm(form).subscribe(data => {
        this.tokenService.token = data.token;
        this.tokenService.user = data.user;
        this.router.navigate(['/im']);
      },
      error => {
        this.error = error.error;
      });
  }
}
