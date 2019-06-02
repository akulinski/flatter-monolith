/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ProfilePictureDetailComponent } from 'app/entities/profile-picture/profile-picture-detail.component';
import { ProfilePicture } from 'app/shared/model/profile-picture.model';

describe('Component Tests', () => {
  describe('ProfilePicture Management Detail Component', () => {
    let comp: ProfilePictureDetailComponent;
    let fixture: ComponentFixture<ProfilePictureDetailComponent>;
    const route = ({ data: of({ profilePicture: new ProfilePicture(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FlatterservermonolithTestModule],
        declarations: [ProfilePictureDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProfilePictureDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProfilePictureDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.profilePicture).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
