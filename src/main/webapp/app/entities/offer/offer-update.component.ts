import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IOffer } from 'app/shared/model/offer.model';
import { OfferService } from './offer.service';
import { IUser, UserService } from 'app/core';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address';
import { IAlbum } from 'app/shared/model/album.model';
import { AlbumService } from 'app/entities/album';

@Component({
    selector: 'jhi-offer-update',
    templateUrl: './offer-update.component.html'
})
export class OfferUpdateComponent implements OnInit {
    offer: IOffer;
    isSaving: boolean;

    users: IUser[];

    addresses: IAddress[];

    albums: IAlbum[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected offerService: OfferService,
        protected userService: UserService,
        protected addressService: AddressService,
        protected albumService: AlbumService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ offer }) => {
            this.offer = offer;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.addressService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAddress[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAddress[]>) => response.body)
            )
            .subscribe((res: IAddress[]) => (this.addresses = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.albumService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IAlbum[]>) => mayBeOk.ok),
                map((response: HttpResponse<IAlbum[]>) => response.body)
            )
            .subscribe((res: IAlbum[]) => (this.albums = res), (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.offer.id !== undefined) {
            this.subscribeToSaveResponse(this.offerService.update(this.offer));
        } else {
            this.subscribeToSaveResponse(this.offerService.create(this.offer));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOffer>>) {
        result.subscribe((res: HttpResponse<IOffer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackAddressById(index: number, item: IAddress) {
        return item.id;
    }

    trackAlbumById(index: number, item: IAlbum) {
        return item.id;
    }
}
