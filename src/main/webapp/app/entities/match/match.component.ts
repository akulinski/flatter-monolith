import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMatch } from 'app/shared/model/match.model';
import { AccountService } from 'app/core';
import { MatchService } from './match.service';

@Component({
    selector: 'jhi-match',
    templateUrl: './match.component.html'
})
export class MatchComponent implements OnInit, OnDestroy {
    matches: IMatch[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected matchService: MatchService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.matchService
            .query()
            .pipe(
                filter((res: HttpResponse<IMatch[]>) => res.ok),
                map((res: HttpResponse<IMatch[]>) => res.body)
            )
            .subscribe(
                (res: IMatch[]) => {
                    this.matches = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMatches();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMatch) {
        return item.id;
    }

    registerChangeInMatches() {
        this.eventSubscriber = this.eventManager.subscribe('matchListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
