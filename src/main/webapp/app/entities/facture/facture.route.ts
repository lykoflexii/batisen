import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { FactureComponent } from './facture.component';
import { FactureDetailComponent } from './facture-detail.component';
import { FacturePopupComponent } from './facture-dialog.component';
import { FactureDeletePopupComponent } from './facture-delete-dialog.component';
import { ExporteFactureComponent } from './exporte-facture.component';

@Injectable()
export class FactureResolvePagingParams implements Resolve<any> {

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

export const factureRoute: Routes = [
    {
        path: 'facture',
        component: FactureComponent,
        resolve: {
            'pagingParams': FactureResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'facture/:id',
        component: FactureDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'facture/exportFacture/:id',
        component: ExporteFactureComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const facturePopupRoute: Routes = [
    {
        path: 'facture-new',
        component: FacturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'facture/:id/edit',
        component: FacturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'facture/:id/delete',
        component: FactureDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.facture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
