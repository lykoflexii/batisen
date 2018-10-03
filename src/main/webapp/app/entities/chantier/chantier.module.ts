import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChartsModule } from 'ng2-charts';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

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
import {
    ChantierService,
    ChantierPopupService,
    ChantierComponent,
    ChantierDetailComponent,
    ChantierDialogComponent,
    ChantierPopupComponent,
    ChantierDeletePopupComponent,
    ChantierDeleteDialogComponent,
    chantierRoute,
    chantierPopupRoute,
    ChantierResolvePagingParams,
} from './';
import {ChantierParClientComponent} from './chantier-par-client.component';
import {ChantierDialogParClientComponent, ChantierPopup2Component} from './chantier-dialog-par-client.component';
import { MatFormFieldModule, MatGridListModule } from '@angular/material';
import { ChnatierParUserComponent } from './chnatier-par-user.component';
import { ChnatierParUserClientComponent } from './chnatier-par-user-client.component';

const ENTITY_STATES = [
    ...chantierRoute,
    ...chantierPopupRoute,
];

@NgModule({
    imports: [
        NgxDatatableModule,
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
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ChantierComponent,
        ChantierDetailComponent,
        ChantierDialogComponent,
        ChantierDeleteDialogComponent,
        ChantierPopupComponent,
        ChantierPopup2Component,
        ChantierDeletePopupComponent,
        ChantierParClientComponent,
        ChantierDialogParClientComponent,
        ChnatierParUserComponent,
        ChnatierParUserClientComponent
    ],
    entryComponents: [
        ChantierComponent,
        ChantierDialogComponent,
        ChantierPopupComponent,
        ChantierPopup2Component,
        ChantierDeleteDialogComponent,
        ChantierDeletePopupComponent,
        ChantierParClientComponent,
        ChantierDialogParClientComponent
    ],
    providers: [
        ChantierService,
        ChantierPopupService,
        ChantierResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpChantierModule {}
