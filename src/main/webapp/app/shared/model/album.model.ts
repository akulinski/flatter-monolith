import { Moment } from 'moment';
import { IOffer } from 'app/shared/model/offer.model';
import { IUser } from 'app/core/user/user.model';
import { IPhoto } from 'app/shared/model/photo.model';

export interface IAlbum {
    id?: number;
    title?: string;
    description?: any;
    created?: Moment;
    offer?: IOffer;
    user?: IUser;
    onetomanies?: IPhoto[];
}

export class Album implements IAlbum {
    constructor(
        public id?: number,
        public title?: string,
        public description?: any,
        public created?: Moment,
        public offer?: IOffer,
        public user?: IUser,
        public onetomanies?: IPhoto[]
    ) {}
}
