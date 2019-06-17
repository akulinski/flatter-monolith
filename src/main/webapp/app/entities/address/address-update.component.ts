import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAddress, Address } from 'app/shared/model/address.model';
import { AddressService } from './address.service';
import { IOffer } from 'app/shared/model/offer.model';
import { OfferService } from 'app/entities/offer';

@Component({
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html'
})
export class AddressUpdateComponent implements OnInit {
  address: IAddress;
  isSaving: boolean;

  offers: IOffer[];

  editForm = this.fb.group({
    id: [],
    city: [],
    zipCode: [],
    street: [],
    flatNumber: [],
    offer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected addressService: AddressService,
    protected offerService: OfferService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ address }) => {
      this.updateForm(address);
      this.address = address;
    });
    this.offerService
      .query({ filter: 'address-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IOffer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IOffer[]>) => response.body)
      )
      .subscribe(
        (res: IOffer[]) => {
          if (!this.address.offer || !this.address.offer.id) {
            this.offers = res;
          } else {
            this.offerService
              .find(this.address.offer.id)
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
  }

  updateForm(address: IAddress) {
    this.editForm.patchValue({
      id: address.id,
      city: address.city,
      zipCode: address.zipCode,
      street: address.street,
      flatNumber: address.flatNumber,
      offer: address.offer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const address = this.createFromForm();
    if (address.id !== undefined) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  private createFromForm(): IAddress {
    const entity = {
      ...new Address(),
      id: this.editForm.get(['id']).value,
      city: this.editForm.get(['city']).value,
      zipCode: this.editForm.get(['zipCode']).value,
      street: this.editForm.get(['street']).value,
      flatNumber: this.editForm.get(['flatNumber']).value,
      offer: this.editForm.get(['offer']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>) {
    result.subscribe((res: HttpResponse<IAddress>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
