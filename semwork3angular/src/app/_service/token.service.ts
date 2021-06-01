import {Injectable} from '@angular/core';
import {UserDto} from '../_dto/user.dto';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable()
export class TokenService {
  public user: UserDto = new UserDto();
  public token = '';
  public done = new BehaviorSubject<boolean>(false);
}
