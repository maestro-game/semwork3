import {SourceDto} from './source.dto';
import {Page} from './page.dto';
import {InnerMessage} from './inner-message.dto';

export class TempSourceDto {
  id: string;
  name: string;
  about: string;
  sourceType: number;
  messages: Page<InnerMessage[]>;
  subsAmount: number;
  avatarImageUrl: string;
  admin: boolean;
  isTemp: boolean;

  constructor(source: SourceDto) {
    this.id = source.id;
    this.name = source.name;
    this.about = source.about;
    this.sourceType = source.sourceType;
    this.messages = source.messages;
    this.subsAmount = source.subsAmount;
    this.avatarImageUrl = source.avatarImageUrl;
    this.admin = source.admin;
    this.isTemp = true;
  }
}
