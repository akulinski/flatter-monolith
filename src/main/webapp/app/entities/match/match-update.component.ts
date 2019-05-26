import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IMatch, Match } from 'app/shared/model/match.model';
import { MatchService } from './match.service';
import { IOffer } from 'app/shared/model/offer.model';
import { OfferService } from 'app/entities/offer';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-match-update',
  templateUrl: './match-update.component.html'
})
export class MatchUpdateComponent implements OnInit {
  match: IMatch;
  isSaving: boolean;

  offers: IOffer[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    isApproved: [],
    creationDate: [],
    approvalDate: [],
    offer: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected matchService: MatchService,
    protected offerService: OfferService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ match }) => {
      this.updateForm(match);
      this.match = match;
    });
    this.offerService
      .query({ filter: 'match-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IOffer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IOffer[]>) => response.body)
      )
      .subscribe(
        (res: IOffer[]) => {
          if (!this.match.offer || !this.match.offer.id) {
            this.offers = res;
          } else {
            this.offerService
              .find(this.match.offer.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IOffer>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IOffer>) => subResponse.body)
              )
              .subscribe(
                (subRes: IOffer) => (this.offers = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(match: IMatch) {
    this.editForm.patchValue({
      id: match.id,
      isApproved: match.isApproved,
      creationDate: match.creationDate != null ? match.creationDate.format(DATE_TIME_FORMAT) : null,
      approvalDate: match.approvalDate != null ? match.approvalDate.format(DATE_TIME_FORMAT) : null,
      offer: match.offer,
      user: match.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const match = this.createFromForm();
    if (match.id !== undefined) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  private createFromForm(): IMatch {
    const entity = {
      ...new Match(),
      id: this.editForm.get(['id']).value,
      isApproved: this.editForm.get(['isApproved']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      approvalDate:
        this.editForm.get(['approvalDate']).value != null ? moment(this.editForm.get(['approvalDate']).value, DATE_TIME_FORMAT) : undefined,
      offer: this.editForm.get(['offer']).value,
      user: this.editForm.get(['user']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>) {
    result.subscribe((res: HttpResponse<IMatch>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackOfferById(index: number, item: IOffer) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
