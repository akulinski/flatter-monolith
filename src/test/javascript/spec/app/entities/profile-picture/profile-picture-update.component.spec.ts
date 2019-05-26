/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ProfilePictureUpdateComponent } from 'app/entities/profile-picture/profile-picture-update.component';
import { ProfilePictureService } from 'app/entities/profile-picture/profile-picture.service';
import { ProfilePicture } from 'app/shared/model/profile-picture.model';

describe('Component Tests', () => {
  describe('ProfilePicture Management Update Component', () => {
    let comp: ProfilePictureUpdateComponent;
    let fixture: ComponentFixture<ProfilePictureUpdateComponent>;
    let service: ProfilePictureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FlatterservermonolithTestModule],
        declarations: [ProfilePictureUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProfilePictureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProfilePictureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProfilePictureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProfilePicture(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProfilePicture();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
