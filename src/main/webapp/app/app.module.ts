import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage, LocalStorageService, SessionStorageService  } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';
import { MatButtonModule} from '@angular/material/button';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { GesBtpSharedModule, UserRouteAccessService, EntrepriseRouteAccessService } from './shared';
import { GesBtpAppRoutingModule} from './app-routing.module';
import { GesBtpHomeModule } from './home/home.module';
import { GesBtpAdminModule } from './admin/admin.module';
import { GesBtpAccountModule } from './account/account.module';
import { GesBtpEntityModule } from './entities/entity.module';
import { PaginationConfig } from './blocks/config/uib-pagination.config';
import { setTheme } from 'ngx-bootstrap/utils';
// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';
import { PaiementComponent } from './paiement/paiement.component';
import { VirementComponent } from './virement/virement.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [
        NgxDatatableModule,
        BrowserModule,
        BrowserAnimationsModule,
        MatButtonModule,
        GesBtpAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        GesBtpSharedModule,
        GesBtpHomeModule,
        GesBtpAdminModule,
        GesBtpAccountModule,
        GesBtpEntityModule,
        ReactiveFormsModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        PaiementComponent,
        VirementComponent,
    ],
    providers: [
        ProfileService,
        PaginationConfig,
        UserRouteAccessService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [
                LocalStorageService,
                SessionStorageService
            ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [
                Injector
            ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [
                JhiEventManager
            ]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [
                Injector
            ]
        }
    ],
    bootstrap: [ JhiMainComponent ]
})
export class GesBtpAppModule {
    constructor() {
        setTheme('bs4'); // or 'bs4'
    }
}
