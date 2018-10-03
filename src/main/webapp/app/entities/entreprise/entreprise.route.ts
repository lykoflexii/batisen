import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { EntrepriseComponent } from './entreprise.component';
import { EntrepriseDetailComponent } from './entreprise-detail.component';
import { EntreprisePopupComponent } from './entreprise-dialog.component';
import { EntrepriseDeletePopupComponent } from './entreprise-delete-dialog.component';
import { ListeEntreprsieAttenteComponent } from './liste-entreprsie-attente.component';

export const entrepriseRoute: Routes = [
    {
        path: 'entreprise',
        component: EntrepriseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.entreprise.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'entreprise/:id',
        component: EntrepriseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.entreprise.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'listeAttente/entreprise',
        component: ListeEntreprsieAttenteComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.entreprise.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const entreprisePopupRoute: Routes = [
    {
        path: 'entreprise-new',
        component: EntreprisePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.entreprise.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'entreprise/:id/edit',
        component: EntreprisePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.entreprise.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'entreprise/:id/delete',
        component: EntrepriseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'gesBtpApp.entreprise.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
