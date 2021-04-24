import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {TokenService} from '../_service/token.service';

@Component({
  selector: 'app-source',
  templateUrl: './source.component.html',
  styleUrls: ['./source.component.css'],
})
export class SourceComponent {
  id: number;
  private subscription: Subscription;

  constructor(private activateRoute: ActivatedRoute, public tokenService: TokenService, public source: ) {
    this.subscription = activateRoute.params.subscribe(params => this.id = params.id);
    if (this.id) {

    }
  }
}
