import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConversation } from 'app/shared/model/conversation.model';
import { AccountService } from 'app/core';
import { ConversationService } from './conversation.service';

@Component({
    selector: 'jhi-conversation',
    templateUrl: './conversation.component.html'
})
export class ConversationComponent implements OnInit, OnDestroy {
    conversations: IConversation[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected conversationService: ConversationService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.conversationService
            .query()
            .pipe(
                filter((res: HttpResponse<IConversation[]>) => res.ok),
                map((res: HttpResponse<IConversation[]>) => res.body)
            )
            .subscribe(
                (res: IConversation[]) => {
                    this.conversations = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInConversations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IConversation) {
        return item.id;
    }

    registerChangeInConversations() {
        this.eventSubscriber = this.eventManager.subscribe('conversationListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
