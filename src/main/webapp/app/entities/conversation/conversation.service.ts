import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConversation } from 'app/shared/model/conversation.model';

type EntityResponseType = HttpResponse<IConversation>;
type EntityArrayResponseType = HttpResponse<IConversation[]>;

@Injectable({ providedIn: 'root' })
export class ConversationService {
    public resourceUrl = SERVER_API_URL + 'api/conversations';

    constructor(protected http: HttpClient) {}

    create(conversation: IConversation): Observable<EntityResponseType> {
        return this.http.post<IConversation>(this.resourceUrl, conversation, { observe: 'response' });
    }

    update(conversation: IConversation): Observable<EntityResponseType> {
        return this.http.put<IConversation>(this.resourceUrl, conversation, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IConversation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IConversation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
