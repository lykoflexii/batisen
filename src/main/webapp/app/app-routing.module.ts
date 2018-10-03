import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from './app.constants';
import { PaiementComponent } from './paiement/paiement.component';
import { VirementComponent } from './virement/virement.component';

const LAYOUT_ROUTES = [
    navbarRoute,
    ...errorRoute,
    { path: 'paiement', component: PaiementComponent },
    { path: 'virement', component: VirementComponent }
];

@NgModule({
    imports: [
        RouterModule.forRoot(LAYOUT_ROUTES, { useHash: true , enableTracing: DEBUG_INFO_ENABLED })
    ],
    exports: [
        RouterModule
    ]
})
export class GesBtpAppRoutingModule {}
