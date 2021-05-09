import {InnerMessage} from './inner-message.dto';
import {PreviewSourceDto} from './preview-source.dto';

export class SourceDto {
  id: number;
  string_id: string;
  name: string;
  about: string;
  sourceType: number;
  messages: InnerMessage[];
  subs_amount: number;
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
