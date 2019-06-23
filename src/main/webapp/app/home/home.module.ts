import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FlatterservermonolithSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import {MatButtonModule, MatGridListModule, MatCardModule, MatCheckboxModule} from '@angular/material';

@NgModule({
  imports: [FlatterservermonolithSharedModule, RouterModule.forChild([HOME_ROUTE]), MatButtonModule, MatCheckboxModule, MatCardModule, MatGridListModule],

  declarations: [HomeComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithHomeModule {}
