import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ChantierComponent } from './chantier.component';
import { ChantierDetailComponent } from './chantier-detail.component';
import { ChantierPopupComponent } from './chantier-dialog.component';
import { ChantierDeletePopupComponent } from './chantier-delete-dialog.component';
import { ChantierParClientComponent } from './chantier-par-client.component';
import {ChantierDialogParClientComponent, ChantierPopup2Component} from './chantier-dialog-par-client.component';
import { ChnatierParUserComponent } from './chnatier-par-user.component';
import { ChnatierParUserClientComponent } from './chnatier-par-user-client.component';

@Injectable()
export class ChantierResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

export const chantierRoute: Routes = [
    {
        path: 'chantiers/creer_edit/:id',
        component: ChantierDialogParClientComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'chantiers/creer_edit',
        component: ChantierDialogParClientComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'chantiers/:id',
        component: ChantierParClientComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
    }
    ,
    {
        path: 'chantiers',
        component: ChnatierParUserComponent,
        data: {
            authorities: ['ROLE_CHEF_CHANTIER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
    }
    ,
    {
        path: 'chantier1/:id',
        component: ChnatierParUserClientComponent,
        data: {
            authorities: ['ROLE_CHEF_CHANTIER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
    },
    {
        path: 'chantier',
        component: ChantierComponent,
        resolve: {
            'pagingParams': ChantierResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'chantier/:id',
        component: ChantierDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const chantierPopupRoute: Routes = [
    {
        path: 'chantier-new2',
        component: ChantierPopup2Component,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chantier/:id/edit2',
        component: ChantierPopup2Component,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chantier-new',
        component: ChantierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chantier/:id/edit',
        component: ChantierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'chantier/:id/delete',
        component: ChantierDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
