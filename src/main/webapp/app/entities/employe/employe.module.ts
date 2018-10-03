import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

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
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';

import {
    EmployeService,
    EmployePopupService,
    EmployeComponent,
    EmployeDetailComponent,
    EmployeDialogComponent,
    EmployePopupComponent,
    EmployeDeletePopupComponent,
    EmployeDeleteDialogComponent,
    employeRoute,
    employePopupRoute,
    EmployeResolvePagingParams,
} from './';
import { MatGridListModule, MatListModule } from '@angular/material';

const ENTITY_STATES = [
    ...employeRoute,
    ...employePopupRoute,
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
        MatSelectModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EmployeComponent,
        EmployeDetailComponent,
        EmployeDialogComponent,
        EmployeDeleteDialogComponent,
        EmployePopupComponent,
        EmployeDeletePopupComponent,
    ],
    entryComponents: [
        EmployeComponent,
        EmployeDialogComponent,
        EmployePopupComponent,
        EmployeDeleteDialogComponent,
        EmployeDeletePopupComponent,
    ],
    providers: [
        EmployeService,
        EmployePopupService,
        EmployeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpEmployeModule {}
