import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import {
    EntrepriseService,
    EntreprisePopupService,
    EntrepriseComponent,
    EntrepriseDetailComponent,
    EntrepriseDialogComponent,
    EntreprisePopupComponent,
    EntrepriseDeletePopupComponent,
    EntrepriseDeleteDialogComponent,
    entrepriseRoute,
    entreprisePopupRoute,
} from './';
import { ListeEntreprsieAttenteComponent } from './liste-entreprsie-attente.component';
import { MatListModule, MatGridListModule, MatFormFieldModule, MatExpansionModule, MatTooltipModule, MatIconModule,
    MatPaginatorModule, MatSortModule, MatInputModule, MatTableModule, MatCardModule, MatButtonModule } from '@angular/material';
import { NoopAnimationsModule, BrowserAnimationsModule } from '@angular/platform-browser/animations';

const ENTITY_STATES = [
    ...entrepriseRoute,
    ...entreprisePopupRoute,
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
        EntrepriseComponent,
        EntrepriseDetailComponent,
        EntrepriseDialogComponent,
        EntrepriseDeleteDialogComponent,
        EntreprisePopupComponent,
        EntrepriseDeletePopupComponent,
        ListeEntreprsieAttenteComponent,
    ],
    entryComponents: [
        EntrepriseComponent,
        EntrepriseDialogComponent,
        EntreprisePopupComponent,
        EntrepriseDeleteDialogComponent,
        EntrepriseDeletePopupComponent,
    ],
    providers: [
        EntrepriseService,
        EntreprisePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpEntrepriseModule {}
