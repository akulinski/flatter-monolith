import { NgModule } from '@angular/core';

import { FlatterservermonolithSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [FlatterservermonolithSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [FlatterservermonolithSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FlatterservermonolithSharedCommonModule {}
