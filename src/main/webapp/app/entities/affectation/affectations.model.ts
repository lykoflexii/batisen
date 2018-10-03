import { BaseEntity } from './../../shared';
import {Travaux} from '../travaux';
import {Chantier} from '../chantier';
import {Employe} from '../employe';

export const enum EtatAffectation {
    'TERMINER',
    'EN_COURS',
    'EN_RETARD'
}

export class Affectation2 implements BaseEntity {
    constructor(
        public id?: number,
        public dateDebut?: any,
        public dateFin?: any,
        public etat?: EtatAffectation,
        public travaux?: Travaux,
        public chantier?: Chantier,
        public employes?: Employe[],
    ) {
    }
}
