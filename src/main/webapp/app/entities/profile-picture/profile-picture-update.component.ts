import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IProfilePicture } from 'app/shared/model/profile-picture.model';
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
    taken: string;
    uploaded: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected profilePictureService: ProfilePictureService,
        protected userService: UserService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ profilePicture }) => {
            this.profilePicture = profilePicture;
            this.taken = this.profilePicture.taken != null ? this.profilePicture.taken.format(DATE_TIME_FORMAT) : null;
            this.uploaded = this.profilePicture.uploaded != null ? this.profilePicture.uploaded.format(DATE_TIME_FORMAT) : null;
        });
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

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.profilePicture, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.profilePicture.taken = this.taken != null ? moment(this.taken, DATE_TIME_FORMAT) : null;
        this.profilePicture.uploaded = this.uploaded != null ? moment(this.uploaded, DATE_TIME_FORMAT) : null;
        if (this.profilePicture.id !== undefined) {
            this.subscribeToSaveResponse(this.profilePictureService.update(this.profilePicture));
        } else {
            this.subscribeToSaveResponse(this.profilePictureService.create(this.profilePicture));
        }
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
