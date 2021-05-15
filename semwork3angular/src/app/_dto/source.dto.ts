import {InnerMessage} from './inner-message.dto';
import {Page} from './page.dto';

export class SourceDto {
  id: string;
  name: string;
  about: string;
  sourceType: number;
  messages: Page<InnerMessage[]>;
  subsAmount: number;
  avatarImageUrl: string;

  static getTypeName(source: SourceDto): string {
    switch (source.sourceType) {
      case 0:
        return 'Группа';
      case 1:
        return 'Канал';
      case 2:
        return 'Диалог';
    }
  }
}
