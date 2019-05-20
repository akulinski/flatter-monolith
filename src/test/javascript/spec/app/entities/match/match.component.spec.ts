/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { MatchComponent } from 'app/entities/match/match.component';
import { MatchService } from 'app/entities/match/match.service';
import { Match } from 'app/shared/model/match.model';

describe('Component Tests', () => {
    describe('Match Management Component', () => {
        let comp: MatchComponent;
        let fixture: ComponentFixture<MatchComponent>;
        let service: MatchService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [MatchComponent],
                providers: []
            })
                .overrideTemplate(MatchComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MatchComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MatchService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Match(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.matches[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
