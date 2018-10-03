import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../../shared';
import {MatIconModule} from '@angular/material/icon';
import { MatTableModule} from '@angular/material/table';
import { MatPaginatorModule} from '@angular/material/paginator';
import { MatInputModule} from '@angular/material/input';
import { MatSortModule} from '@angular/material/sort';
import {MatTooltipModule} from '@angular/material/tooltip';
import {
    ClientService,
    ClientPopupService,
    ClientComponent,
    ClientDetailComponent,
    ClientDialogComponent,
    ClientPopupComponent,
    ClientDeletePopupComponent,
    ClientDeleteDialogComponent,
    clientRoute,
    clientPopupRoute,
    ClientResolvePagingParams,
} from './';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatCardModule, MatGridListModule, MatFormFieldModule, MatExpansionModule } from '@angular/material';

const ENTITY_STATES = [
    ...clientRoute,
    ...clientPopupRoute,
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
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ClientComponent,
        ClientDetailComponent,
        ClientDialogComponent,
        ClientDeleteDialogComponent,
        ClientPopupComponent,
        ClientDeletePopupComponent,
    ],
    entryComponents: [
        ClientComponent,
        ClientDialogComponent,
        ClientPopupComponent,
        ClientDeleteDialogComponent,
        ClientDeletePopupComponent,
    ],
    providers: [
        ClientService,
        ClientPopupService,
        ClientResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpClientModule {}
