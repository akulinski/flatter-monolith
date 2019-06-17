import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {JhiAlertService, JhiDataUtils} from "ng-jhipster";
import {IPhoto} from "app/shared/model/photo.model";
import {IFulllOfferModel} from "app/entities/offer/fulll-offer-model";
import {IOffer, Offer} from "app/shared/model/offer.model";
import {Address} from "app/shared/model/address.model";
import {Album} from "app/shared/model/album.model";
import {OfferService} from "app/entities/offer/offer.service";
import {filter, map} from "rxjs/operators";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'jhi-full-offer-creator-compontent',
  templateUrl: './full-offer-creator.component.html',
  styleUrls: ['./full-offer-creator.component.scss']
})
export class FullOfferCreatorComponent implements OnInit {
  photos: IPhoto[] = [];
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  thirdFormGroup: FormGroup
  isLinear = true;
  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    image: [null, [Validators.required]],
    imageContentType: [],
    height: [],
    width: [],
    taken: [],
    uploaded: [],
    album: []
  });

  fullOfferModel: IFulllOfferModel;
  offer: Offer = new Offer();
  address: Address = new Address();

  constructor(private router: Router, private _formBuilder: FormBuilder, protected dataUtils: JhiDataUtils, private fb: FormBuilder, protected jhiAlertService: JhiAlertService, private offerService: OfferService) {
  }

  ngOnInit() {
    this.firstFormGroup = this._formBuilder.group({
      description: ['', Validators.required],
      totalCost: ['', Validators.required],
      roomAmount: ['', Validators.required],
      size: ['', Validators.required],
      constructionYear: ['', Validators.required],
      pets: ['', Validators.required],
      smokingInside: ['', Validators.required],
      isFurnished: ['', Validators.required],

    });
    this.secondFormGroup = this._formBuilder.group({
      city: ['', Validators.required],
      zipCode: ['', Validators.required],
      street: ['', Validators.required],
      flatNumber: ['', Validators.required],
    });
  }

  createFullOfferModel(): IOffer {
    let album = new Album();
    console.log('Offer: ' + this.offer.description.split(/\s+/).slice(0, 5).join(" "));
    album.description = this.offer.description;
    album.title = this.offer.description.split(/\s+/).slice(0, 5).join(" "); //returns first 5 words from description
    album.photos = this.photos;
    this.offer.album = album;
    this.offer.address = this.address;

    return this.offer;
  }

  submitOffer() {
    this.offerService.create(this.createFullOfferModel())
      .pipe(
        filter((mayBeOk: HttpResponse<IOffer>) => mayBeOk.ok),
        map((response: HttpResponse<IOffer>) => response.body)
      )
      .subscribe((res: IOffer) => (this.router.navigate(['/offer', res.id, 'view']), (res: HttpErrorResponse) => console.log(res)));
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  trackId(index: number, item: IPhoto) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
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
            this.photos.push({image: base64Data, imageContentType: file.type})
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

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
