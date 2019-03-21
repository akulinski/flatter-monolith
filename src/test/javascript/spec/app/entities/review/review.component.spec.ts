/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { ReviewComponent } from 'app/entities/review/review.component';
import { ReviewService } from 'app/entities/review/review.service';
import { Review } from 'app/shared/model/review.model';

describe('Component Tests', () => {
    describe('Review Management Component', () => {
        let comp: ReviewComponent;
        let fixture: ComponentFixture<ReviewComponent>;
        let service: ReviewService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [ReviewComponent],
                providers: []
            })
                .overrideTemplate(ReviewComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReviewComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReviewService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Review(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.reviews[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
