/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { QuestionnaireService } from 'app/entities/questionnaire/questionnaire.service';
import { IQuestionnaire, Questionnaire } from 'app/shared/model/questionnaire.model';

describe('Service Tests', () => {
    describe('Questionnaire Service', () => {
        let injector: TestBed;
        let service: QuestionnaireService;
        let httpMock: HttpTestingController;
        let elemDefault: IQuestionnaire;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(QuestionnaireService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Questionnaire(0, false, false, false, 0, 0, 0, 0, 0, 0, 'AAAAAAA', 0, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Questionnaire', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Questionnaire(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Questionnaire', async () => {
                const returnedFromService = Object.assign(
                    {
                        pets: true,
                        smokingInside: true,
                        isFurnished: true,
                        roomAmountMin: 1,
                        roomAmountMax: 1,
                        sizeMin: 1,
                        sizeMax: 1,
                        constructionYearMin: 1,
                        constructionYearMax: 1,
                        type: 'BBBBBB',
                        totalCostMin: 1,
                        totalCostMax: 1
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Questionnaire', async () => {
                const returnedFromService = Object.assign(
                    {
                        pets: true,
                        smokingInside: true,
                        isFurnished: true,
                        roomAmountMin: 1,
                        roomAmountMax: 1,
                        sizeMin: 1,
                        sizeMax: 1,
                        constructionYearMin: 1,
                        constructionYearMax: 1,
                        type: 'BBBBBB',
                        totalCostMin: 1,
                        totalCostMax: 1
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Questionnaire', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
