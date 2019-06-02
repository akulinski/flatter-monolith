import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IOffer, Offer } from 'app/shared/model/offer.model';
import { OfferService } from './offer.service';
import { IUser, UserService } from 'app/core';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address';
import { IAlbum } from 'app/shared/model/album.model';
import { AlbumService } from 'app/entities/album';
import { IMatch } from 'app/shared/model/match.model';
import { MatchService } from 'app/entities/match';

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

  matches: IMatch[];

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    totalCost: [null, [Validators.required]],
    roomAmount: [null, [Validators.required]],
    size: [null, [Validators.required]],
    type: [],
    constructionYear: [],
    pets: [],
    smokingInside: [],
    isFurnished: [],
    user: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected offerService: OfferService,
    protected userService: UserService,
    protected addressService: AddressService,
    protected albumService: AlbumService,
    protected matchService: MatchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ offer }) => {
      this.updateForm(offer);
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
    this.matchService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMatch[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMatch[]>) => response.body)
      )
      .subscribe((res: IMatch[]) => (this.matches = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(offer: IOffer) {
    this.editForm.patchValue({
      id: offer.id,
      description: offer.description,
      totalCost: offer.totalCost,
      roomAmount: offer.roomAmount,
      size: offer.size,
      type: offer.type,
      constructionYear: offer.constructionYear,
      pets: offer.pets,
      smokingInside: offer.smokingInside,
      isFurnished: offer.isFurnished,
      user: offer.user
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const offer = this.createFromForm();
    if (offer.id !== undefined) {
      this.subscribeToSaveResponse(this.offerService.update(offer));
    } else {
      this.subscribeToSaveResponse(this.offerService.create(offer));
    }
  }

  private createFromForm(): IOffer {
    const entity = {
      ...new Offer(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value,
      totalCost: this.editForm.get(['totalCost']).value,
      roomAmount: this.editForm.get(['roomAmount']).value,
      size: this.editForm.get(['size']).value,
      type: this.editForm.get(['type']).value,
      constructionYear: this.editForm.get(['constructionYear']).value,
      pets: this.editForm.get(['pets']).value,
      smokingInside: this.editForm.get(['smokingInside']).value,
      isFurnished: this.editForm.get(['isFurnished']).value,
      user: this.editForm.get(['user']).value
    };
    return entity;
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

  trackMatchById(index: number, item: IMatch) {
    return item.id;
  }
}
