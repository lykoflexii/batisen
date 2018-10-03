import { BaseEntity } from './../../shared';
import {Client} from "../client";

export const enum EtatChantier {
    'EN_COURS',
    'EN_RETARD',
    'TERMINER'
}

export class Chantiers implements BaseEntity {
    constructor(
        public id?: number,
        public nomChantier?: string,
        public descriptionChantier?: string,
        public ville?: string,
        public adresse?: string,
        public etatChantier?: EtatChantier,
        public dateDebutReelle?: any,
        public dateFinReelle?: any,
        public dateDebutPrevu?: any,
        public dateFinPrevu?: any,
        public client?: Client,
        public affectation3S?: BaseEntity[],
    ) {
    }
}
