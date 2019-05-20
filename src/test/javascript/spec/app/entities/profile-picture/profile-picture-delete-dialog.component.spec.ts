/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ProfilePictureDeleteDialogComponent } from 'app/entities/profile-picture/profile-picture-delete-dialog.component';
import { ProfilePictureService } from 'app/entities/profile-picture/profile-picture.service';

describe('Component Tests', () => {
    describe('ProfilePicture Management Delete Component', () => {
        let comp: ProfilePictureDeleteDialogComponent;
        let fixture: ComponentFixture<ProfilePictureDeleteDialogComponent>;
        let service: ProfilePictureService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [ProfilePictureDeleteDialogComponent]
            })
                .overrideTemplate(ProfilePictureDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProfilePictureDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfilePictureService);
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
