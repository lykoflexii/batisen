import { BaseEntity } from './../../shared';

export class Entreprise implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public commercialName?: string,
        public sirenNumber?: string,
        public entrepriseAdminEmail?: string,
        public telephone?: number,
        public pack?: number,
        public clients?: BaseEntity[],
        public employes?: BaseEntity[],
        public facture4S?: BaseEntity[],
    ) {
    }
}
