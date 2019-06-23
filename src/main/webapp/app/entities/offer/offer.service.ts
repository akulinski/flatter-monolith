import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IOffer} from 'app/shared/model/offer.model';
import {IFulllOfferModel} from "app/entities/offer/fulll-offer-model";

type EntityResponseType = HttpResponse<IOffer>;
type EntityArrayResponseType = HttpResponse<IOffer[]>;

@Injectable({providedIn: 'root'})
export class OfferService {
  public resourceUrl = SERVER_API_URL + 'api/offers';

  public  topOffers = SERVER_API_URL + 'api/offers/get-top-3';

  public myMatchesUrl = isDevMode() ? SERVER_API_URL + 'api/mock-my-offers' : SERVER_API_URL + 'api/my-offers';

  constructor(protected http: HttpClient) {
  }

  create(offer: IOffer): Observable<EntityResponseType> {
    return this.http.post<IOffer>(this.resourceUrl, offer, {observe: 'response'});
  }


  createFullOffer(fullOfferModel: IFulllOfferModel): Observable<EntityResponseType> {
    console.log("Posting new http to full offer");
    return this.http.post<IFulllOfferModel>(this.resourceUrl+'/create-full', fullOfferModel, {observe: 'response'});
  }

  update(offer: IOffer): Observable<EntityResponseType> {
    return this.http.put<IOffer>(this.resourceUrl, offer, {observe: 'response'});
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOffer>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOffer[]>(this.myMatchesUrl, {params: options, observe: 'response'});
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  getTopOffers(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOffer[]>(this.topOffers, {params: options, observe: 'response'});
  }


}
