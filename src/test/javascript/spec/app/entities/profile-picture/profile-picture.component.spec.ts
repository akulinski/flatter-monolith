/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ProfilePictureComponent } from 'app/entities/profile-picture/profile-picture.component';
import { ProfilePictureService } from 'app/entities/profile-picture/profile-picture.service';
import { ProfilePicture } from 'app/shared/model/profile-picture.model';

describe('Component Tests', () => {
    describe('ProfilePicture Management Component', () => {
        let comp: ProfilePictureComponent;
        let fixture: ComponentFixture<ProfilePictureComponent>;
        let service: ProfilePictureService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [ProfilePictureComponent],
                providers: []
            })
                .overrideTemplate(ProfilePictureComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProfilePictureComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfilePictureService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ProfilePicture(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.profilePictures[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
