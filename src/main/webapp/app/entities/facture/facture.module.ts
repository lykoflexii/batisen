import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import {
    FactureService,
    FacturePopupService,
    FactureComponent,
    FactureDetailComponent,
    FactureDialogComponent,
    FacturePopupComponent,
    FactureDeletePopupComponent,
    FactureDeleteDialogComponent,
    factureRoute,
    facturePopupRoute,
    FactureResolvePagingParams,
} from './';
import { MatListModule, MatGridListModule, MatFormFieldModule, MatExpansionModule, MatTooltipModule, MatIconModule,
    MatPaginatorModule, MatSortModule, MatInputModule, MatTableModule, MatCardModule, MatButtonModule } from '@angular/material';
import { NoopAnimationsModule, BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ExporteFactureComponent } from './exporte-facture.component';
import { FactureParChefChantierComponent } from './facture-par-chef-chantier.component';

const ENTITY_STATES = [
    ...factureRoute,
    ...facturePopupRoute,
];

@NgModule({
    imports: [
        GesBtpSharedModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
        MatButtonModule,
        MatCardModule,
        MatTableModule,
        MatInputModule,
        MatSortModule,
        MatPaginatorModule,
        MatIconModule,
        MatTooltipModule,
        MatExpansionModule,
        MatFormFieldModule,
        MatGridListModule,
        MatListModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FactureComponent,
        FactureDetailComponent,
        FactureDialogComponent,
        FactureDeleteDialogComponent,
        FacturePopupComponent,
        FactureDeletePopupComponent,
        ExporteFactureComponent,
        FactureParChefChantierComponent,
    ],
    entryComponents: [
        FactureComponent,
        FactureDialogComponent,
        FacturePopupComponent,
        FactureDeleteDialogComponent,
        FactureDeletePopupComponent,
    ],
    providers: [
        FactureService,
        FacturePopupService,
        FactureResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpFactureModule {}
