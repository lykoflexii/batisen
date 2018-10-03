import { BaseEntity } from './../../shared';

export class LigneFacture implements BaseEntity {
    constructor(
        public id?: number,
        public designation?: string,
        public prixUnitaire?: number,
        public quantite?: number,
        public quantiteRetenue?: number,
        public factureId?: number,
    ) {
    }
}
