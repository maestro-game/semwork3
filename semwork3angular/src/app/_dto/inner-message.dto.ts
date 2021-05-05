import {TitleSource} from './title-source.dto';

export class InnerMessage {
  id: number;
  created: Date;
  author: TitleSource;
  from: TitleSource;
  text: string;
  views: number;
}
