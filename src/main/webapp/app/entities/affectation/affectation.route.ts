import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AffectationComponent } from './affectation.component';
import { AffectationDetailComponent } from './affectation-detail.component';
import { AffectationPopupComponent, AffectationDialogComponent } from './affectation-dialog.component';
import { AffectationDeletePopupComponent } from './affectation-delete-dialog.component';
import { ListeChantierComponent } from './liste-chantier.component';
import {ChantierDialogParClientComponent} from '../chantier/chantier-dialog-par-client.component';
import {TravauxDialogParChantierComponent} from './travaux-dialog-par-chantier.component';
import { AffectationPopup2Component } from './affecter-ressource.component';
import { ListeRessourceComponent } from './liste-ressource.component';
import { RedirectComponent } from './redirect.component';
import { DashboardComponent } from './dashboard.component';

@Injectable()
export class AffectationResolvePagingParams implements Resolve<any> {

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

export const affectationRoute: Routes = [
    {
        path: 'redirect',
        component: RedirectComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectations/ressource/:id',
        component: ListeRessourceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectations/creer_edit',
        component: TravauxDialogParChantierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.chantier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectations/:id',
        component: ListeChantierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectation/affectation-new',
        component: AffectationDialogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectation-new',
        component: AffectationDialogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectation/:id/edit',
        component: AffectationDialogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'affectation',
        component: AffectationComponent,
        resolve: {
            'pagingParams': AffectationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'affectation/:id',
        component: ListeChantierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'dashboard',
        component: DashboardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Dashboard'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const affectationPopupRoute: Routes = [
    {
        path: 'affectation-new',
        component: AffectationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'affectation/:id/edit',
        component: AffectationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'affectation/:id/delete',
        component: AffectationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'affectation/:id/add',
        component: AffectationPopup2Component,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.affectation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
