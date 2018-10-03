import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import {
    LigneFactureService,
    LigneFacturePopupService,
    LigneFactureComponent,
    LigneFactureDetailComponent,
    LigneFactureDialogComponent,
    LigneFacturePopupComponent,
    LigneFactureDeletePopupComponent,
    LigneFactureDeleteDialogComponent,
    ligneFactureRoute,
    ligneFacturePopupRoute,
    LigneFactureResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...ligneFactureRoute,
    ...ligneFacturePopupRoute,
];

@NgModule({
    imports: [
        GesBtpSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LigneFactureComponent,
        LigneFactureDetailComponent,
        LigneFactureDialogComponent,
        LigneFactureDeleteDialogComponent,
        LigneFacturePopupComponent,
        LigneFactureDeletePopupComponent,
    ],
    entryComponents: [
        LigneFactureComponent,
        LigneFactureDialogComponent,
        LigneFacturePopupComponent,
        LigneFactureDeleteDialogComponent,
        LigneFactureDeletePopupComponent,
    ],
    providers: [
        LigneFactureService,
        LigneFacturePopupService,
        LigneFactureResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpLigneFactureModule {}
