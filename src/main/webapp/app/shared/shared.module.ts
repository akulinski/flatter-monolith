import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import {
    FlatterservermonolithSharedLibsModule,
    FlatterservermonolithSharedCommonModule,
    JhiLoginModalComponent,
    HasAnyAuthorityDirective
} from './';

@NgModule({
    imports: [FlatterservermonolithSharedLibsModule, FlatterservermonolithSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
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
