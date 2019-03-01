import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IAlbum } from 'app/shared/model/album.model';
import { AlbumService } from './album.service';
import { IOffer } from 'app/shared/model/offer.model';
import { OfferService } from 'app/entities/offer';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-album-update',
    templateUrl: './album-update.component.html'
})
export class AlbumUpdateComponent implements OnInit {
    album: IAlbum;
    isSaving: boolean;

    offers: IOffer[];

    users: IUser[];
    created: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected albumService: AlbumService,
        protected offerService: OfferService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ album }) => {
            this.album = album;
            this.created = this.album.created != null ? this.album.created.format(DATE_TIME_FORMAT) : null;
        });
        this.offerService
            .query({ filter: 'album-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IOffer[]>) => mayBeOk.ok),
                map((response: HttpResponse<IOffer[]>) => response.body)
            )
            .subscribe(
                (res: IOffer[]) => {
                    if (!this.album.offer || !this.album.offer.id) {
                        this.offers = res;
                    } else {
                        this.offerService
                            .find(this.album.offer.id)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IOffer>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IOffer>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IOffer) => (this.offers = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.album.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        if (this.album.id !== undefined) {
            this.subscribeToSaveResponse(this.albumService.update(this.album));
        } else {
            this.subscribeToSaveResponse(this.albumService.create(this.album));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlbum>>) {
        result.subscribe((res: HttpResponse<IAlbum>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackOfferById(index: number, item: IOffer) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
