import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';
import { MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatListModule} from '@angular/material/list';
import {
    DevisService,
    DevisPopupService,
    DevisComponent,
    DevisDetailComponent,
    DevisDialogComponent,
    DevisPopupComponent,
    DevisDeletePopupComponent,
    DevisDeleteDialogComponent,
    devisRoute,
    devisPopupRoute,
    DevisResolvePagingParams,
} from './';
import { DevisParChantierComponent } from './devis-par-chantier.component';
import { MatCardModule, MatTableModule, MatInputModule, MatSortModule, MatPaginatorModule, MatExpansionModule, MatFormFieldModule, MatGridListModule } from '@angular/material';

const ENTITY_STATES = [
    ...devisRoute,
    ...devisPopupRoute,
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
        DevisComponent,
        DevisDetailComponent,
        DevisDialogComponent,
        DevisDeleteDialogComponent,
        DevisPopupComponent,
        DevisDeletePopupComponent,
        DevisParChantierComponent,
    ],
    entryComponents: [
        DevisComponent,
        DevisDialogComponent,
        DevisPopupComponent,
        DevisDeleteDialogComponent,
        DevisDeletePopupComponent,
    ],
    providers: [
        DevisService,
        DevisPopupService,
        DevisResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpDevisModule {}
