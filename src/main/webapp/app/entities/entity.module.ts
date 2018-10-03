import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { GesBtpTravauxModule } from './travaux/travaux.module';
import { GesBtpEmployeModule } from './employe/employe.module';
import { GesBtpAffectationModule } from './affectation/affectation.module';
import { GesBtpChantierModule } from './chantier/chantier.module';
import { GesBtpClientModule } from './client/client.module';
import { GesBtpDevisModule } from './devis/devis.module';
import { GesBtpLigneDevisModule } from './ligne-devis/ligne-devis.module';
import { GesBtpEntrepriseModule } from './entreprise/entreprise.module';
import { GesBtpFactureModule } from './facture/facture.module';
import { GesBtpLigneFactureModule } from './ligne-facture/ligne-facture.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        GesBtpTravauxModule,
        GesBtpEmployeModule,
        GesBtpAffectationModule,
        GesBtpChantierModule,
        GesBtpClientModule,
        GesBtpDevisModule,
        GesBtpLigneDevisModule,
        GesBtpEntrepriseModule,
        GesBtpFactureModule,
        GesBtpLigneFactureModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GesBtpEntityModule {}
