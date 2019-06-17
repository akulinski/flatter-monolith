import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {JhiDataUtils} from 'ng-jhipster';

import {IOffer} from 'app/shared/model/offer.model';
import {IPhoto} from 'app/shared/model/photo.model';
import {DomSanitizer} from '@angular/platform-browser';
import {MatIconRegistry} from '@angular/material';

@Component({
  selector: 'jhi-offer-detail',
  templateUrl: './offer-detail.component.html',
  styleUrls: ['./offer-detail.component.scss']
})
export class OfferDetailComponent implements OnInit {
  offer: IOffer;
  currentPhoto: IPhoto;
  currentIndex: number;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute, iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    this.currentIndex = 0;
    iconRegistry.addSvgIcon(
      'right',
      sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-arrow_forward_ios-24px.svg'));

    iconRegistry.addSvgIcon(
      'left',
      sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-arrow_back_ios-24px.svg'));

    iconRegistry.addSvgIcon(
      'money',
      sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-attach_money-24px.svg'));

    iconRegistry.addSvgIcon(
      'area',
      sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-border_outer-24px.svg'));

    iconRegistry.addSvgIcon(
      'date',
      sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-date_range-24px.svg'));

    iconRegistry.addSvgIcon(
      'rooms',
      sanitizer.bypassSecurityTrustResourceUrl('/content/assets/img/baseline-hotel-24px.svg'));
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({offer}) => {
      this.offer = offer;
      this.currentPhoto = this.offer.album.photos[this.currentIndex];
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

  nextPhoto() {
    if (this.currentIndex < this.offer.album.photos.length - 1) {
      this.currentIndex++;
      this.currentPhoto = this.offer.album.photos[this.currentIndex];
    }
  }

  prevPhoto() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.currentPhoto = this.offer.album.photos[this.currentIndex];
    }
  }
}
