import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { LigneFactureComponent } from './ligne-facture.component';
import { LigneFactureDetailComponent } from './ligne-facture-detail.component';
import { LigneFacturePopupComponent } from './ligne-facture-dialog.component';
import { LigneFactureDeletePopupComponent } from './ligne-facture-delete-dialog.component';

@Injectable()
export class LigneFactureResolvePagingParams implements Resolve<any> {

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

export const ligneFactureRoute: Routes = [
    {
        path: 'ligne-facture',
        component: LigneFactureComponent,
        resolve: {
            'pagingParams': LigneFactureResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneFacture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ligne-facture/:id',
        component: LigneFactureDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneFacture.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ligneFacturePopupRoute: Routes = [
    {
        path: 'ligne-facture-new',
        component: LigneFacturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneFacture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ligne-facture/:id/edit',
        component: LigneFacturePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneFacture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ligne-facture/:id/delete',
        component: LigneFactureDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.ligneFacture.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
