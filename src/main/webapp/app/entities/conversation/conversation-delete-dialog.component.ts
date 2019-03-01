import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConversation } from 'app/shared/model/conversation.model';
import { ConversationService } from './conversation.service';

@Component({
    selector: 'jhi-conversation-delete-dialog',
    templateUrl: './conversation-delete-dialog.component.html'
})
export class ConversationDeleteDialogComponent {
    conversation: IConversation;

    constructor(
        protected conversationService: ConversationService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.conversationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'conversationListModification',
                content: 'Deleted an conversation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-conversation-delete-popup',
    template: ''
})
export class ConversationDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ conversation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConversationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.conversation = conversation;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/conversation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/conversation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
