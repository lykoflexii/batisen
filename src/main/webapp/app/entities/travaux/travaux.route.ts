import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TravauxComponent } from './travaux.component';
import { TravauxDetailComponent } from './travaux-detail.component';
import { TravauxPopupComponent } from './travaux-dialog.component';
import { TravauxDeletePopupComponent } from './travaux-delete-dialog.component';

@Injectable()
export class TravauxResolvePagingParams implements Resolve<any> {

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

export const travauxRoute: Routes = [
    {
        path: 'travaux',
        component: TravauxComponent,
        resolve: {
            'pagingParams': TravauxResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.travaux.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'travaux/:id',
        component: TravauxDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.travaux.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const travauxPopupRoute: Routes = [
    {
        path: 'travaux-new',
        component: TravauxPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.travaux.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'travaux/:id/edit',
        component: TravauxPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.travaux.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'travaux/:id/delete',
        component: TravauxDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.travaux.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
