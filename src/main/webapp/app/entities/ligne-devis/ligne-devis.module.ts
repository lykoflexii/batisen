import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import {
    LigneDevisService,
    LigneDevisPopupService,
    LigneDevisComponent,
    LigneDevisDetailComponent,
    LigneDevisDialogComponent,
    LigneDevisPopupComponent,
    LigneDevisDeletePopupComponent,
    LigneDevisDeleteDialogComponent,
    ligneDevisRoute,
    ligneDevisPopupRoute,
    LigneDevisResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...ligneDevisRoute,
    ...ligneDevisPopupRoute,
];

@NgModule({
    imports: [
        GesBtpSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LigneDevisComponent,
        LigneDevisDetailComponent,
        LigneDevisDialogComponent,
        LigneDevisDeleteDialogComponent,
        LigneDevisPopupComponent,
        LigneDevisDeletePopupComponent,
    ],
    entryComponents: [
        LigneDevisComponent,
        LigneDevisDialogComponent,
        LigneDevisPopupComponent,
        LigneDevisDeleteDialogComponent,
        LigneDevisDeletePopupComponent,
    ],
    providers: [
        LigneDevisService,
        LigneDevisPopupService,
        LigneDevisResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpLigneDevisModule {}
