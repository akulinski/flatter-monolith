/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { MatchDeleteDialogComponent } from 'app/entities/match/match-delete-dialog.component';
import { MatchService } from 'app/entities/match/match.service';

describe('Component Tests', () => {
    describe('Match Management Delete Component', () => {
        let comp: MatchDeleteDialogComponent;
        let fixture: ComponentFixture<MatchDeleteDialogComponent>;
        let service: MatchService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [MatchDeleteDialogComponent]
            })
                .overrideTemplate(MatchDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MatchDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MatchService);
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
