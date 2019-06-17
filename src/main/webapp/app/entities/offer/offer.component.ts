import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IOffer } from 'app/shared/model/offer.model';
import { Account, AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { OfferService } from './offer.service';
import { log } from 'util';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.scss']
})
export class OfferComponent implements OnInit, OnDestroy {
  currentAccount: any;
  account: Account;
  offers: IOffer[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  constructor(
    protected offerService: OfferService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected iconRegistry: MatIconRegistry,
    protected sanitizer: DomSanitizer
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });

    iconRegistry.addSvgIcon('date', sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-date_range-24px.svg'));

    iconRegistry.addSvgIcon('area', sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-border_outer-24px.svg'));

    iconRegistry.addSvgIcon('info', sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-info-24px.svg'));

    iconRegistry.addSvgIcon('id', sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-perm_identity-24px.svg'));

    iconRegistry.addSvgIcon('money', sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-attach_money-24px.svg'));
  }

  loadAll() {
    this.offerService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IOffer[]>) => this.paginateOffers(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/offer'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/offer',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });

    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
    this.registerChangeInOffers();
    this.registerAuthenticationSuccess();
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().then(account => {
        this.account = account;
      });
    });
  }

  getCorrectURL(item: IOffer) {
    return 'https://ui-avatars.com/api/?name=' + item.user.firstName + '+' + item.user.lastName;
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IOffer) {
    return item.id;
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  checkisOfferValid(item: IOffer) {
    console.log('New user: ');
    console.log('User: ' + item.user.firstName + ' ' + item.user.lastName);
    console.log('Address: ' + item.address.city + ' ' + item.address.street + ' ' + item.address.zipCode);
    return true;
  }

  checkIfItIsYourOfferOrYouAreAdmin(item: IOffer) {
    console.log(this.account.login);
    console.log(item.user.login);

    return this.account.login == item.user.login;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInOffers() {
    this.eventSubscriber = this.eventManager.subscribe('offerListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateOffers(data: IOffer[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.offers = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
