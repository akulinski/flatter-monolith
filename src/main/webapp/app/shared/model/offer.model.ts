import { IUser } from 'app/core/user/user.model';
import { IAddress } from 'app/shared/model/address.model';
import { IAlbum } from 'app/shared/model/album.model';
import { IMatch } from 'app/shared/model/match.model';

export interface IOffer {
  id?: number;
  description?: any;
  totalCost?: number;
  roomAmount?: number;
  size?: number;
  type?: string;
  constructionYear?: number;
  pets?: boolean;
  smokingInside?: boolean;
  isFurnished?: boolean;
  user?: IUser;
  address?: IAddress;
  album?: IAlbum;
  match?: IMatch;
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
    public pets?: boolean,
    public smokingInside?: boolean,
    public isFurnished?: boolean,
    public user?: IUser,
    public address?: IAddress,
    public album?: IAlbum,
    public match?: IMatch
  ) {
    this.pets = this.pets || false;
    this.smokingInside = this.smokingInside || false;
    this.isFurnished = this.isFurnished || false;
  }
}
