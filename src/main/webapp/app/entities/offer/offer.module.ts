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
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatStepperModule,
  MatTabsModule
} from '@angular/material';
import {FullOfferCreatorComponent} from "app/entities/offer/full-offer-creator.component";
import {FormsModule} from "@angular/forms";

const ENTITY_STATES = [...offerRoute, ...offerPopupRoute];

@NgModule({
  imports: [FlatterservermonolithSharedModule, FormsModule, RouterModule.forChild(ENTITY_STATES), MatGridListModule, MatTabsModule, MatCardModule, MatDividerModule, MatButtonModule, MatIconModule, MatListModule, MatStepperModule, MatInputModule],
  declarations: [OfferComponent, OfferDetailComponent, OfferUpdateComponent, OfferDeleteDialogComponent, OfferDeletePopupComponent, FullOfferCreatorComponent],
  entryComponents: [OfferComponent, OfferUpdateComponent, OfferDeleteDialogComponent, OfferDeletePopupComponent, FullOfferCreatorComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithOfferModule {
}
