import {Component, OnInit} from '@angular/core';
import {PreviewSourceDto} from '../_dto/preview-source.dto';
import {HttpService} from '../_service/http.service';
import {Router} from '@angular/router';
import {TokenService} from '../_service/token.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit{
  sources: PreviewSourceDto[];

  constructor(private httpService: HttpService, public tokenService: TokenService, private router: Router) {}

  open(id: number): void {
    console.log(id);
  }

  ngOnInit(): void {
    this.httpService.getSourceList().subscribe(data => this.sources = data);
  }
}
