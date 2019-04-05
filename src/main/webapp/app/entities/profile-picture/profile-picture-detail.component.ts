import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IProfilePicture } from 'app/shared/model/profile-picture.model';

@Component({
    selector: 'jhi-profile-picture-detail',
    templateUrl: './profile-picture-detail.component.html'
})
export class ProfilePictureDetailComponent implements OnInit {
    profilePicture: IProfilePicture;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ profilePicture }) => {
            this.profilePicture = profilePicture;
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
