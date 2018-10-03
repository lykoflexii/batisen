import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { EmployeComponent } from './employe.component';
import { EmployeDetailComponent } from './employe-detail.component';
import { EmployePopupComponent } from './employe-dialog.component';
import { EmployeDeletePopupComponent } from './employe-delete-dialog.component';

@Injectable()
export class EmployeResolvePagingParams implements Resolve<any> {

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

export const employeRoute: Routes = [
    {
        path: 'employe',
        component: EmployeComponent,
        resolve: {
            'pagingParams': EmployeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'employe/:id',
        component: EmployeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const employePopupRoute: Routes = [
    {
        path: 'employe-new',
        component: EmployePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employe/:id/edit',
        component: EmployePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employe/:id/delete',
        component: EmployeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.employe.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
