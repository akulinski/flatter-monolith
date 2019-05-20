/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FlatterservermonolithTestModule } from '../../../test.module';
import { QuestionnaireUpdateComponent } from 'app/entities/questionnaire/questionnaire-update.component';
import { QuestionnaireService } from 'app/entities/questionnaire/questionnaire.service';
import { Questionnaire } from 'app/shared/model/questionnaire.model';

describe('Component Tests', () => {
    describe('Questionnaire Management Update Component', () => {
        let comp: QuestionnaireUpdateComponent;
        let fixture: ComponentFixture<QuestionnaireUpdateComponent>;
        let service: QuestionnaireService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FlatterservermonolithTestModule],
                declarations: [QuestionnaireUpdateComponent]
            })
                .overrideTemplate(QuestionnaireUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(QuestionnaireUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionnaireService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Questionnaire(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.questionnaire = entity;
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
                    const entity = new Questionnaire();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.questionnaire = entity;
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
