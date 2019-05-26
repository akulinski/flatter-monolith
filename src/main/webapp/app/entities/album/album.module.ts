import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FlatterservermonolithSharedModule } from 'app/shared';
import {
  AlbumComponent,
  AlbumDetailComponent,
  AlbumUpdateComponent,
  AlbumDeletePopupComponent,
  AlbumDeleteDialogComponent,
  albumRoute,
  albumPopupRoute
} from './';

const ENTITY_STATES = [...albumRoute, ...albumPopupRoute];

@NgModule({
  imports: [FlatterservermonolithSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [AlbumComponent, AlbumDetailComponent, AlbumUpdateComponent, AlbumDeleteDialogComponent, AlbumDeletePopupComponent],
  entryComponents: [AlbumComponent, AlbumUpdateComponent, AlbumDeleteDialogComponent, AlbumDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithAlbumModule {}
