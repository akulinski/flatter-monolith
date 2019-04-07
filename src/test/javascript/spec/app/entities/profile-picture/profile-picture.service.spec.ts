/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ProfilePictureService } from 'app/entities/profile-picture/profile-picture.service';
import { IProfilePicture, ProfilePicture } from 'app/shared/model/profile-picture.model';

describe('Service Tests', () => {
    describe('ProfilePicture Service', () => {
        let injector: TestBed;
        let service: ProfilePictureService;
        let httpMock: HttpTestingController;
        let elemDefault: IProfilePicture;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ProfilePictureService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new ProfilePicture(0, 'image/png', 'AAAAAAA', 0, 0, currentDate, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        taken: currentDate.format(DATE_TIME_FORMAT),
                        uploaded: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a ProfilePicture', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        taken: currentDate.format(DATE_TIME_FORMAT),
                        uploaded: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        taken: currentDate,
                        uploaded: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new ProfilePicture(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a ProfilePicture', async () => {
                const returnedFromService = Object.assign(
                    {
                        image: 'BBBBBB',
                        height: 1,
                        width: 1,
                        taken: currentDate.format(DATE_TIME_FORMAT),
                        uploaded: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        taken: currentDate,
                        uploaded: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of ProfilePicture', async () => {
                const returnedFromService = Object.assign(
                    {
                        image: 'BBBBBB',
                        height: 1,
                        width: 1,
                        taken: currentDate.format(DATE_TIME_FORMAT),
                        uploaded: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        taken: currentDate,
                        uploaded: currentDate
                    },
                    returnedFromService
                );
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

            it('should delete a ProfilePicture', async () => {
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