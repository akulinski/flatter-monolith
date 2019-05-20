import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProfilePicture } from 'app/shared/model/profile-picture.model';

type EntityResponseType = HttpResponse<IProfilePicture>;
type EntityArrayResponseType = HttpResponse<IProfilePicture[]>;

@Injectable({ providedIn: 'root' })
export class ProfilePictureService {
  public resourceUrl = SERVER_API_URL + 'api/profile-pictures';

  constructor(protected http: HttpClient) {}

  create(profilePicture: IProfilePicture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profilePicture);
    return this.http
      .post<IProfilePicture>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(profilePicture: IProfilePicture): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profilePicture);
    return this.http
      .put<IProfilePicture>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProfilePicture>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProfilePicture[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(profilePicture: IProfilePicture): IProfilePicture {
    const copy: IProfilePicture = Object.assign({}, profilePicture, {
      taken: profilePicture.taken != null && profilePicture.taken.isValid() ? profilePicture.taken.toJSON() : null,
      uploaded: profilePicture.uploaded != null && profilePicture.uploaded.isValid() ? profilePicture.uploaded.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.taken = res.body.taken != null ? moment(res.body.taken) : null;
      res.body.uploaded = res.body.uploaded != null ? moment(res.body.uploaded) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((profilePicture: IProfilePicture) => {
        profilePicture.taken = profilePicture.taken != null ? moment(profilePicture.taken) : null;
        profilePicture.uploaded = profilePicture.uploaded != null ? moment(profilePicture.uploaded) : null;
      });
    }
    return res;
  }
}
