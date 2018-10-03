import { BaseEntity } from './../../shared';

export class Devis implements BaseEntity {
    constructor(
        public id?: number,
        public titre?: string,
        public dateDeCreation?: any,
        public validite?: any,
        public tva?: number,
        public coutMainDoeuvre?: number,
        public clientNomClient?: string,
        public clientId?: number,
        public chantierNomChantier?: string,
        public chantierId?: number,
        public devis?: BaseEntity[],
    ) {
    }
}
