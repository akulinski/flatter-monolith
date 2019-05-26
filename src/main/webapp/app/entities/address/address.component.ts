import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAddress } from 'app/shared/model/address.model';
import { AccountService } from 'app/core';
import { AddressService } from './address.service';

@Component({
  selector: 'jhi-address',
  templateUrl: './address.component.html'
})
export class AddressComponent implements OnInit, OnDestroy {
  addresses: IAddress[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected addressService: AddressService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.addressService
      .query()
      .pipe(
        filter((res: HttpResponse<IAddress[]>) => res.ok),
        map((res: HttpResponse<IAddress[]>) => res.body)
      )
      .subscribe(
        (res: IAddress[]) => {
          this.addresses = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInAddresses();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IAddress) {
    return item.id;
  }

  registerChangeInAddresses() {
    this.eventSubscriber = this.eventManager.subscribe('addressListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
