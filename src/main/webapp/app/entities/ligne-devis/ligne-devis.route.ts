import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { LigneDevisComponent } from './ligne-devis.component';
import { LigneDevisDetailComponent } from './ligne-devis-detail.component';
import { LigneDevisPopupComponent } from './ligne-devis-dialog.component';
import { LigneDevisDeletePopupComponent } from './ligne-devis-delete-dialog.component';

@Injectable()
export class LigneDevisResolvePagingParams implements Resolve<any> {

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

export const ligneDevisRoute: Routes = [
    {
        path: 'ligne-devis',
        component: LigneDevisComponent,
        resolve: {
            'pagingParams': LigneDevisResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneDevis.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ligne-devis/:id',
        component: LigneDevisDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneDevis.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ligneDevisPopupRoute: Routes = [
    {
        path: 'ligne-devis-new',
        component: LigneDevisPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneDevis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ligne-devis/:id/edit',
        component: LigneDevisPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneDevis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ligne-devis/:id/delete',
        component: LigneDevisDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneDevis.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
