/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { QuestionnaireDetailComponent } from 'app/entities/questionnaire/questionnaire-detail.component';
import { Questionnaire } from 'app/shared/model/questionnaire.model';

describe('Component Tests', () => {
    describe('Questionnaire Management Detail Component', () => {
        let comp: QuestionnaireDetailComponent;
        let fixture: ComponentFixture<QuestionnaireDetailComponent>;
        const route = ({ data: of({ questionnaire: new Questionnaire(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [QuestionnaireDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(QuestionnaireDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(QuestionnaireDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.questionnaire).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
