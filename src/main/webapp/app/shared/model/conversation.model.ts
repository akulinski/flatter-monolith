import { IUser } from 'app/core/user/user.model';
import { IMessage } from 'app/shared/model/message.model';

export interface IConversation {
  id?: number;
  sender?: IUser;
  reciver?: IUser;
  messages?: IMessage[];
}

export class Conversation implements IConversation {
  constructor(public id?: number, public sender?: IUser, public reciver?: IUser, public messages?: IMessage[]) {}
}
