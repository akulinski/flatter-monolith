import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IOffer } from 'app/shared/model/offer.model';

@Component({
  selector: 'jhi-offer-detail',
  templateUrl: './offer-detail.component.html'
})
export class OfferDetailComponent implements OnInit {
  offer: IOffer;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ offer }) => {
      this.offer = offer;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
