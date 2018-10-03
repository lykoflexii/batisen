import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EntrepriseMgmtComponent } from './entreprise-management.component';
import { EntrepriseMgmtDetailComponent } from './entreprise-management-detail.component';
import { EntrepriseDialogComponent } from './entreprise-management-dialog.component';
import { EntrepriseDeleteDialogComponent } from './entreprise-management-delete-dialog.component';
import { EntrepriseRouteAccessService } from './../../shared';

@Injectable()
export class EntrepriseResolvePagingParams implements Resolve<any> {

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

export const entrepriseMgmtRoute: Routes = [
    {
        path: 'entreprise-management',
        component: EntrepriseMgmtComponent,
        resolve: {
            'pagingParams': EntrepriseResolvePagingParams
        },
        data: {
            pageTitle: 'entrepriseManagement.home.title'
        },
        canActivate: [EntrepriseRouteAccessService]
    },
    {
        path: 'entreprise-management/:id',
        component: EntrepriseMgmtDetailComponent,
        data: {
            pageTitle: 'entrepriseManagement.home.title'
        },
        canActivate: [EntrepriseRouteAccessService]
    }
];

export const entrepriseDialogRoute: Routes = [
    {
        path: 'entreprise-management-new',
        component: EntrepriseDialogComponent,
        outlet: 'popup',
        canActivate: [EntrepriseRouteAccessService]
    },
    {
        path: 'entreprise-management/:id/edit',
        component: EntrepriseDialogComponent,
        outlet: 'popup',
        canActivate: [EntrepriseRouteAccessService]
    },
    {
        path: 'entreprise-management/:id/delete',
        component: EntrepriseDeleteDialogComponent,
        outlet: 'popup',
        canActivate: [EntrepriseRouteAccessService]
    }
];
