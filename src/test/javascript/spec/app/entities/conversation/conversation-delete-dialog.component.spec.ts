/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ConversationDeleteDialogComponent } from 'app/entities/conversation/conversation-delete-dialog.component';
import { ConversationService } from 'app/entities/conversation/conversation.service';

describe('Component Tests', () => {
  describe('Conversation Management Delete Component', () => {
    let comp: ConversationDeleteDialogComponent;
    let fixture: ComponentFixture<ConversationDeleteDialogComponent>;
    let service: ConversationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FlatterservermonolithTestModule],
        declarations: [ConversationDeleteDialogComponent]
      })
        .overrideTemplate(ConversationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConversationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConversationService);
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
