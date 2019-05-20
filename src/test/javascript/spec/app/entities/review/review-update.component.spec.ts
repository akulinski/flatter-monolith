/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ReviewUpdateComponent } from 'app/entities/review/review-update.component';
import { ReviewService } from 'app/entities/review/review.service';
import { Review } from 'app/shared/model/review.model';

describe('Component Tests', () => {
    describe('Review Management Update Component', () => {
        let comp: ReviewUpdateComponent;
        let fixture: ComponentFixture<ReviewUpdateComponent>;
        let service: ReviewService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [ReviewUpdateComponent]
            })
                .overrideTemplate(ReviewUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReviewUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Review(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.review = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Review();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.review = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
