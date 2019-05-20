import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMatch } from 'app/shared/model/match.model';

type EntityResponseType = HttpResponse<IMatch>;
type EntityArrayResponseType = HttpResponse<IMatch[]>;

@Injectable({ providedIn: 'root' })
export class MatchService {
    public resourceUrl = SERVER_API_URL + 'api/matches';

    constructor(protected http: HttpClient) {}

    create(match: IMatch): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(match);
        return this.http
            .post<IMatch>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(match: IMatch): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(match);
        return this.http
            .put<IMatch>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMatch[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(match: IMatch): IMatch {
        const copy: IMatch = Object.assign({}, match, {
            creationDate: match.creationDate != null && match.creationDate.isValid() ? match.creationDate.toJSON() : null,
            approvalDate: match.approvalDate != null && match.approvalDate.isValid() ? match.approvalDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
            res.body.approvalDate = res.body.approvalDate != null ? moment(res.body.approvalDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((match: IMatch) => {
                match.creationDate = match.creationDate != null ? moment(match.creationDate) : null;
                match.approvalDate = match.approvalDate != null ? moment(match.approvalDate) : null;
            });
        }
        return res;
    }
}
