import { IUser } from 'app/core/user/user.model';
import { IAddress } from 'app/shared/model/address.model';
import { IAlbum } from 'app/shared/model/album.model';

export interface IOffer {
    id?: number;
    description?: any;
    totalCost?: number;
    roomAmount?: number;
    size?: number;
    type?: string;
    constructionYear?: number;
    isFurnished?: boolean;
    user?: IUser;
    address?: IAddress;
    album?: IAlbum;
}

export class Offer implements IOffer {
    constructor(
        public id?: number,
        public description?: any,
        public totalCost?: number,
        public roomAmount?: number,
        public size?: number,
        public type?: string,
        public constructionYear?: number,
        public isFurnished?: boolean,
        public user?: IUser,
        public address?: IAddress,
        public album?: IAlbum
    ) {
        this.isFurnished = this.isFurnished || false;
    }
}
