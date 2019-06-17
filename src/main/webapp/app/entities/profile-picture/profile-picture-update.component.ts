import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IProfilePicture, ProfilePicture } from 'app/shared/model/profile-picture.model';
import { ProfilePictureService } from './profile-picture.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-profile-picture-update',
  templateUrl: './profile-picture-update.component.html'
})
export class ProfilePictureUpdateComponent implements OnInit {
  profilePicture: IProfilePicture;
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    image: [null, [Validators.required]],
    imageContentType: [],
    height: [],
    width: [],
    taken: [],
    uploaded: [],
    user: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected profilePictureService: ProfilePictureService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ profilePicture }) => {
      this.updateForm(profilePicture);
      this.profilePicture = profilePicture;
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(profilePicture: IProfilePicture) {
    this.editForm.patchValue({
      id: profilePicture.id,
      image: profilePicture.image,
      imageContentType: profilePicture.imageContentType,
      height: profilePicture.height,
      width: profilePicture.width,
      taken: profilePicture.taken != null ? profilePicture.taken.format(DATE_TIME_FORMAT) : null,
      uploaded: profilePicture.uploaded != null ? profilePicture.uploaded.format(DATE_TIME_FORMAT) : null,
      user: profilePicture.user
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

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const profilePicture = this.createFromForm();
    if (profilePicture.id !== undefined) {
      this.subscribeToSaveResponse(this.profilePictureService.update(profilePicture));
    } else {
      this.subscribeToSaveResponse(this.profilePictureService.create(profilePicture));
    }
  }

  private createFromForm(): IProfilePicture {
    const entity = {
      ...new ProfilePicture(),
      id: this.editForm.get(['id']).value,
      imageContentType: this.editForm.get(['imageContentType']).value,
      image: this.editForm.get(['image']).value,
      height: this.editForm.get(['height']).value,
      width: this.editForm.get(['width']).value,
      taken: this.editForm.get(['taken']).value != null ? moment(this.editForm.get(['taken']).value, DATE_TIME_FORMAT) : undefined,
      uploaded: this.editForm.get(['uploaded']).value != null ? moment(this.editForm.get(['uploaded']).value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfilePicture>>) {
    result.subscribe((res: HttpResponse<IProfilePicture>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
