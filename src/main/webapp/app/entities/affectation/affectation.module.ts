import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChartsModule } from 'ng2-charts';

import { GesBtpSharedModule } from '../../shared';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';
import { MatButtonModule} from '@angular/material/button';
import { MatCardModule} from '@angular/material/card';
import { MatTableModule} from '@angular/material/table';
import { MatPaginatorModule} from '@angular/material/paginator';
import { MatInputModule} from '@angular/material/input';
import { MatSortModule} from '@angular/material/sort';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatSelectModule} from '@angular/material/select';
import { MultiselectDropdownModule } from 'angular-4-dropdown-multiselect';
import {
    AffectationService,
    AffectationPopupService,
    AffectationComponent,
    AffectationDetailComponent,
    AffectationDialogComponent,
    AffectationPopupComponent,
    AffectationDeletePopupComponent,
    AffectationDeleteDialogComponent,
    affectationRoute,
    affectationPopupRoute,
    AffectationResolvePagingParams,
} from './';
import {ListeChantierComponent} from './liste-chantier.component';
import {TravauxDialogParChantierComponent} from './travaux-dialog-par-chantier.component';
import { AffecterRessourceComponent, AffectationPopup2Component } from './affecter-ressource.component';
import { ListeRessourceComponent } from './liste-ressource.component';
import { MatFormFieldModule, MatGridListModule, MatListModule } from '@angular/material';
import { RedirectComponent } from './redirect.component';
import { DashboardComponent } from './dashboard.component';
import { AmChartsModule } from '@amcharts/amcharts3-angular';

const ENTITY_STATES = [
    ...affectationRoute,
    ...affectationPopupRoute,
];

@NgModule({
    imports: [
        GesBtpSharedModule,
        ChartsModule,
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
        MatSelectModule,
        MultiselectDropdownModule,
        AmChartsModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AffectationComponent,
        AffectationDetailComponent,
        AffectationDialogComponent,
        AffectationDeleteDialogComponent,
        AffectationPopupComponent,
        AffectationDeletePopupComponent,
        ListeChantierComponent,
        TravauxDialogParChantierComponent,
        AffecterRessourceComponent,
        AffectationPopup2Component,
        ListeRessourceComponent,
        RedirectComponent,
        DashboardComponent
    ],
    entryComponents: [
        AffectationComponent,
        AffectationDialogComponent,
        AffectationPopupComponent,
        AffectationDeleteDialogComponent,
        AffectationDeletePopupComponent,
        ListeChantierComponent,
        TravauxDialogParChantierComponent,
        AffectationPopup2Component,
        AffecterRessourceComponent
    ],
    providers: [
        AffectationService,
        AffectationPopupService,
        AffectationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpAffectationModule {}
