import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IProfilePicture } from 'app/shared/model/profile-picture.model';
import { AccountService } from 'app/core';
import { ProfilePictureService } from './profile-picture.service';

@Component({
    selector: 'jhi-profile-picture',
    templateUrl: './profile-picture.component.html'
})
export class ProfilePictureComponent implements OnInit, OnDestroy {
    profilePictures: IProfilePicture[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected profilePictureService: ProfilePictureService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.profilePictureService
            .query()
            .pipe(
                filter((res: HttpResponse<IProfilePicture[]>) => res.ok),
                map((res: HttpResponse<IProfilePicture[]>) => res.body)
            )
            .subscribe(
                (res: IProfilePicture[]) => {
                    this.profilePictures = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProfilePictures();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProfilePicture) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInProfilePictures() {
        this.eventSubscriber = this.eventManager.subscribe('profilePictureListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
