import { Moment } from 'moment';
import { IOffer } from 'app/shared/model/offer.model';
import { IUser } from 'app/core/user/user.model';

export interface IMatch {
    id?: number;
    isApproved?: boolean;
    creationDate?: Moment;
    approvalDate?: Moment;
    offer?: IOffer;
    user?: IUser;
}

export class Match implements IMatch {
    constructor(
        public id?: number,
        public isApproved?: boolean,
        public creationDate?: Moment,
        public approvalDate?: Moment,
        public offer?: IOffer,
        public user?: IUser
    ) {
        this.isApproved = this.isApproved || false;
    }
}
