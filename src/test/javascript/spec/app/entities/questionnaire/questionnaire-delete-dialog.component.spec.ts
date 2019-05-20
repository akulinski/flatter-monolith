/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { QuestionnaireDeleteDialogComponent } from 'app/entities/questionnaire/questionnaire-delete-dialog.component';
import { QuestionnaireService } from 'app/entities/questionnaire/questionnaire.service';

describe('Component Tests', () => {
    describe('Questionnaire Management Delete Component', () => {
        let comp: QuestionnaireDeleteDialogComponent;
        let fixture: ComponentFixture<QuestionnaireDeleteDialogComponent>;
        let service: QuestionnaireService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [QuestionnaireDeleteDialogComponent]
            })
                .overrideTemplate(QuestionnaireDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(QuestionnaireDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionnaireService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
