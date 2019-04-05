import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IProfilePicture {
    id?: number;
    imageContentType?: string;
    image?: any;
    height?: number;
    width?: number;
    taken?: Moment;
    uploaded?: Moment;
    user?: IUser;
}

export class ProfilePicture implements IProfilePicture {
    constructor(
        public id?: number,
        public imageContentType?: string,
        public image?: any,
        public height?: number,
        public width?: number,
        public taken?: Moment,
        public uploaded?: Moment,
        public user?: IUser
    ) {}
}
