import { BaseEntity } from './../../shared';

export const enum Type {
    'PARTICULIER',
    'PROFESSIONNEL'
}

export class Client implements BaseEntity {
    constructor(
        public id?: number,
        public nomClient?: string,
        public villeClient?: string,
        public adresseClient?: string,
        public telephoneClient?: string,
        public fax?: string,
        public emailClient?: string,
        public typeClient?: Type,
        public prenomClient?: string,
        public nomCommercial?: string,
        public entrepriseName?: string,
        public entrepriseId?: number,
        public clients?: BaseEntity[],
        public client1S?: BaseEntity[],
    ) {
    }
}
