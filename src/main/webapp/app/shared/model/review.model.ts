import { IUser } from 'app/core/user/user.model';

export interface IReview {
  id?: number;
  rate?: number;
  description?: string;
  issuer?: IUser;
  receiver?: IUser;
}

export class Review implements IReview {
  constructor(public id?: number, public rate?: number, public description?: string, public issuer?: IUser, public receiver?: IUser) {}
}
