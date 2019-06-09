import {IOffer} from "app/shared/model/offer.model";
import {IAddress} from "app/shared/model/address.model";
import {IAlbum} from "app/shared/model/album.model";

export interface IFulllOfferModel {
  offer: IOffer;
  address: IAddress;
  album: IAlbum
}
