import {Injectable} from '@angular/core';
import {UserDto} from '../_dto/user.dto';

@Injectable()
export class TokenService {
  public user: UserDto = new UserDto();
  public token = '';
}
