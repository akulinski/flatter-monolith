import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FlatterservermonolithSharedModule } from 'app/shared';
import {
    ProfilePictureComponent,
    ProfilePictureDetailComponent,
    ProfilePictureUpdateComponent,
    ProfilePictureDeletePopupComponent,
    ProfilePictureDeleteDialogComponent,
    profilePictureRoute,
    profilePicturePopupRoute
} from './';

const ENTITY_STATES = [...profilePictureRoute, ...profilePicturePopupRoute];

@NgModule({
    imports: [FlatterservermonolithSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProfilePictureComponent,
        ProfilePictureDetailComponent,
        ProfilePictureUpdateComponent,
        ProfilePictureDeleteDialogComponent,
        ProfilePictureDeletePopupComponent
    ],
    entryComponents: [
        ProfilePictureComponent,
        ProfilePictureUpdateComponent,
        ProfilePictureDeleteDialogComponent,
        ProfilePictureDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithProfilePictureModule {}
