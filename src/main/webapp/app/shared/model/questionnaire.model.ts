import { IUser } from 'app/core/user/user.model';

export interface IQuestionnaire {
  id?: number;
  pets?: boolean;
  smokingInside?: boolean;
  isFurnished?: boolean;
  roomAmountMin?: number;
  roomAmountMax?: number;
  sizeMin?: number;
  sizeMax?: number;
  constructionYearMin?: number;
  constructionYearMax?: number;
  type?: string;
  totalCostMin?: number;
  totalCostMax?: number;
  user?: IUser;
}

export class Questionnaire implements IQuestionnaire {
  constructor(
    public id?: number,
    public pets?: boolean,
    public smokingInside?: boolean,
    public isFurnished?: boolean,
    public roomAmountMin?: number,
    public roomAmountMax?: number,
    public sizeMin?: number,
    public sizeMax?: number,
    public constructionYearMin?: number,
    public constructionYearMax?: number,
    public type?: string,
    public totalCostMin?: number,
    public totalCostMax?: number,
    public user?: IUser
  ) {
    this.pets = this.pets || false;
    this.smokingInside = this.smokingInside || false;
    this.isFurnished = this.isFurnished || false;
  }
}
