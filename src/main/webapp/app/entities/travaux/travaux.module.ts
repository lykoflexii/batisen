import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import {
    TravauxService,
    TravauxPopupService,
    TravauxComponent,
    TravauxDetailComponent,
    TravauxDialogComponent,
    TravauxPopupComponent,
    TravauxDeletePopupComponent,
    TravauxDeleteDialogComponent,
    travauxRoute,
    travauxPopupRoute,
    TravauxResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...travauxRoute,
    ...travauxPopupRoute,
];

@NgModule({
    imports: [
        GesBtpSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TravauxComponent,
        TravauxDetailComponent,
        TravauxDialogComponent,
        TravauxDeleteDialogComponent,
        TravauxPopupComponent,
        TravauxDeletePopupComponent,
    ],
    entryComponents: [
        TravauxComponent,
        TravauxDialogComponent,
        TravauxPopupComponent,
        TravauxDeleteDialogComponent,
        TravauxDeletePopupComponent,
    ],
    providers: [
        TravauxService,
        TravauxPopupService,
        TravauxResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpTravauxModule {}
