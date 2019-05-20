import { Moment } from 'moment';
import { IConversation } from 'app/shared/model/conversation.model';

export interface IMessage {
    id?: number;
    creationDate?: Moment;
    content?: any;
    isSeen?: boolean;
    conversation?: IConversation;
}

export class Message implements IMessage {
    constructor(
        public id?: number,
        public creationDate?: Moment,
        public content?: any,
        public isSeen?: boolean,
        public conversation?: IConversation
    ) {
        this.isSeen = this.isSeen || false;
    }
}
