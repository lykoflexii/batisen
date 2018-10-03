import { BaseEntity } from './../../shared';

export class Facture implements BaseEntity {
    constructor(
        public id?: number,
        public dateCreation?: any,
        public remise?: number,
        public valider?: boolean,
        public chantierNomChantier?: string,
        public chantierId?: number,
        public travauxNomTrav?: string,
        public travauxId?: number,
        public entrepriseName?: string,
        public entrepriseId?: number,
        public factures?: BaseEntity[],
    ) {
        this.valider = false;
    }
}
