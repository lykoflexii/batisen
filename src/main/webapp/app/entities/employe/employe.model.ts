import { BaseEntity } from './../../shared';

export const enum Sexe {
    'MASCULIN',
    'FEMININ'
}

export class Employe implements BaseEntity {
    constructor(
        public id?: number,
        public nomEmploye?: string,
        public prenomEmploye?: string,
        public matricule?: string,
        public fonction?: string,
        public dateNaissance?: any,
        public telephoneEmploye?: string,
        public salaire?: number,
        public sexe?: Sexe,
        public entrepriseName?: string,
        public entrepriseId?: number,
        public affectation1S?: BaseEntity[],
    ) {
    }
}
