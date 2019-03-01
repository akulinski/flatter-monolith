import { IOffer } from 'app/shared/model/offer.model';

export interface IAddress {
    id?: number;
    city?: string;
    zipCode?: string;
    street?: string;
    flatNumber?: string;
    offer?: IOffer;
}

export class Address implements IAddress {
    constructor(
        public id?: number,
        public city?: string,
        public zipCode?: string,
        public street?: string,
        public flatNumber?: string,
        public offer?: IOffer
    ) {}
}
