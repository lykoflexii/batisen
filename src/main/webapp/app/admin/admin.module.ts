import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GesBtpSharedModule } from '../shared';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */
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
    adminState,
    AuditsComponent,
    UserMgmtComponent,
    UserDialogComponent,
    UserDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtDialogComponent,
    UserMgmtDeleteDialogComponent,
    EntrepriseMgmtComponent,
    EntrepriseMgmtDetailComponent,
    EntrepriseMgmtDialogComponent,
    EntrepriseDialogComponent,
    EntrepriseDeleteDialogComponent,
    EntrepriseMgmtDeleteDialogComponent,
    LogsComponent,
    JhiMetricsMonitoringModalComponent,
    JhiMetricsMonitoringComponent,
    JhiHealthModalComponent,
    JhiHealthCheckComponent,
    JhiConfigurationComponent,
    JhiDocsComponent,
    AuditsService,
    JhiConfigurationService,
    JhiHealthService,
    JhiMetricsService,
    LogsService,
    UserResolvePagingParams,
    UserResolve,
    UserModalService,
    EntrepriseResolvePagingParams,
    EntrepriseService,
    EntrepriseModalService
} from './';

@NgModule({
    imports: [
        GesBtpSharedModule,
        BrowserAnimationsModule,
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
        MatSelectModule,
        RouterModule.forRoot(adminState, { useHash: true }),
        /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    ],
    declarations: [
        AuditsComponent,
        UserMgmtComponent,
        UserDialogComponent,
        UserDeleteDialogComponent,
        UserMgmtDetailComponent,
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        EntrepriseMgmtComponent,
        EntrepriseMgmtDetailComponent,
        EntrepriseMgmtDialogComponent,
        EntrepriseDialogComponent,
        EntrepriseDeleteDialogComponent,
        EntrepriseMgmtDeleteDialogComponent,
        LogsComponent,
        JhiConfigurationComponent,
        JhiHealthCheckComponent,
        JhiHealthModalComponent,
        JhiDocsComponent,
        JhiMetricsMonitoringComponent,
        JhiMetricsMonitoringModalComponent
    ],
    entryComponents: [
        UserMgmtDialogComponent,
        UserMgmtDeleteDialogComponent,
        JhiHealthModalComponent,
        JhiMetricsMonitoringModalComponent,
        EntrepriseMgmtDialogComponent,
        EntrepriseMgmtDeleteDialogComponent
    ],
    providers: [
        AuditsService,
        JhiConfigurationService,
        JhiHealthService,
        JhiMetricsService,
        LogsService,
        UserResolvePagingParams,
        UserResolve,
        UserModalService,
        EntrepriseResolvePagingParams,
        EntrepriseService,
        EntrepriseModalService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpAdminModule {}
