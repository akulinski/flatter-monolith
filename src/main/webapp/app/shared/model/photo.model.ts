import { Moment } from 'moment';
import { IAlbum } from 'app/shared/model/album.model';

export interface IPhoto {
    id?: number;
    title?: string;
    description?: any;
    imageContentType?: string;
    image?: any;
    height?: number;
    width?: number;
    taken?: Moment;
    uploaded?: Moment;
    album?: IAlbum;
}

export class Photo implements IPhoto {
    constructor(
        public id?: number,
        public title?: string,
        public description?: any,
        public imageContentType?: string,
        public image?: any,
        public height?: number,
        public width?: number,
        public taken?: Moment,
        public uploaded?: Moment,
        public album?: IAlbum
    ) {}
}
