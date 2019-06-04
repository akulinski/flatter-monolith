import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {FlatterservermonolithSharedModule} from 'app/shared';
import {
  OfferComponent,
  OfferDeleteDialogComponent,
  OfferDeletePopupComponent,
  OfferDetailComponent,
  offerPopupRoute,
  offerRoute,
  OfferUpdateComponent
} from './';
import {
  MatButtonModule,
  MatCardModule,
  MatDividerModule,
  MatGridListModule,
  MatIconModule, MatListModule,
  MatTabsModule
} from '@angular/material';

const ENTITY_STATES = [...offerRoute, ...offerPopupRoute];

@NgModule({
  imports: [FlatterservermonolithSharedModule, RouterModule.forChild(ENTITY_STATES), MatGridListModule, MatTabsModule, MatCardModule, MatDividerModule, MatButtonModule, MatIconModule, MatListModule],
  declarations: [OfferComponent, OfferDetailComponent, OfferUpdateComponent, OfferDeleteDialogComponent, OfferDeletePopupComponent],
  entryComponents: [OfferComponent, OfferUpdateComponent, OfferDeleteDialogComponent, OfferDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithOfferModule {
}
