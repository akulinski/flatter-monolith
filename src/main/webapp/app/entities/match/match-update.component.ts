import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IMatch } from 'app/shared/model/match.model';
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
    creationDate: string;
    approvalDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected matchService: MatchService,
        protected offerService: OfferService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ match }) => {
            this.match = match;
            this.creationDate = this.match.creationDate != null ? this.match.creationDate.format(DATE_TIME_FORMAT) : null;
            this.approvalDate = this.match.approvalDate != null ? this.match.approvalDate.format(DATE_TIME_FORMAT) : null;
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

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.match.creationDate = this.creationDate != null ? moment(this.creationDate, DATE_TIME_FORMAT) : null;
        this.match.approvalDate = this.approvalDate != null ? moment(this.approvalDate, DATE_TIME_FORMAT) : null;
        if (this.match.id !== undefined) {
            this.subscribeToSaveResponse(this.matchService.update(this.match));
        } else {
            this.subscribeToSaveResponse(this.matchService.create(this.match));
        }
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
