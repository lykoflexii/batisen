import { BaseEntity } from './../../shared';

export class LigneDevis implements BaseEntity {
    constructor(
        public id?: number,
        public designation?: string,
        public prixUnitaire?: number,
        public quantite?: number,
        public devisId?: number,
    ) {
    }
}
