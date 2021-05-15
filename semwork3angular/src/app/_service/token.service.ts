import {Injectable} from '@angular/core';
import {UserDto} from '../_dto/user.dto';
import {BehaviorSubject} from 'rxjs';

@Injectable()
export class TokenService {
  public user: UserDto = new UserDto();
  public token = '';
  public done = false;
}
