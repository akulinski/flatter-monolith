import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';

type EntityResponseType = HttpResponse<IQuestionnaire>;
type EntityArrayResponseType = HttpResponse<IQuestionnaire[]>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireService {
  public resourceUrl = SERVER_API_URL + 'api/questionnaires';

  constructor(protected http: HttpClient) {}

  create(questionnaire: IQuestionnaire): Observable<EntityResponseType> {
    return this.http.post<IQuestionnaire>(this.resourceUrl, questionnaire, { observe: 'response' });
  }

  update(questionnaire: IQuestionnaire): Observable<EntityResponseType> {
    return this.http.put<IQuestionnaire>(this.resourceUrl, questionnaire, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionnaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionnaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
