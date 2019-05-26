import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {
  FlatterservermonolithSharedLibsModule,
  FlatterservermonolithSharedCommonModule,
  JhiLoginModalComponent,
  HasAnyAuthorityDirective
} from './';

@NgModule({
  imports: [FlatterservermonolithSharedLibsModule, FlatterservermonolithSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [FlatterservermonolithSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FlatterservermonolithSharedModule {
  static forRoot() {
    return {
      ngModule: FlatterservermonolithSharedModule
    };
  }
}
