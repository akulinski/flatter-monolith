import {Injectable} from '@angular/core';
import {SERVER_API_URL} from "app/app.constants";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {IUserCheckDTO} from "app/home/UserCheckDTO";

type EntityResponseType = HttpResponse<IUserCheckDTO>;

@Injectable({
  providedIn: 'root'
})
export class UserCheckService {
  public resourceUrl = SERVER_API_URL + 'api/users';

  constructor(protected http: HttpClient) {
  }

  check(): Observable<EntityResponseType> {
    return this.http.get<IUserCheckDTO>(`${this.resourceUrl}/check`, {observe: 'response'});
  }

}
