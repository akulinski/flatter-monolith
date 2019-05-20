import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { QuestionnaireService } from './questionnaire.service';

@Component({
    selector: 'jhi-questionnaire-delete-dialog',
    templateUrl: './questionnaire-delete-dialog.component.html'
})
export class QuestionnaireDeleteDialogComponent {
    questionnaire: IQuestionnaire;

    constructor(
        protected questionnaireService: QuestionnaireService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.questionnaireService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'questionnaireListModification',
                content: 'Deleted an questionnaire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-questionnaire-delete-popup',
    template: ''
})
export class QuestionnaireDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ questionnaire }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(QuestionnaireDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.questionnaire = questionnaire;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/questionnaire', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/questionnaire', { outlets: { popup: null } }]);
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
