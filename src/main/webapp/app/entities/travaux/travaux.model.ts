import { BaseEntity } from './../../shared';

export class Travaux implements BaseEntity {
    constructor(
        public id?: number,
        public nomTrav?: string,
        public descriptionTrav?: string,
        public affectation2S?: BaseEntity[],
        public facture3S?: BaseEntity[],
    ) {
    }
}
