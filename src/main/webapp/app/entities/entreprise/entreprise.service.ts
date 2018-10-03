import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Entreprise } from './entreprise.model';
import { createRequestOption, User } from '../../shared';

export type EntityResponseType = HttpResponse<Entreprise>;

@Injectable()
export class EntrepriseService {

    private resourceUrl =  SERVER_API_URL + 'api/entreprises';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/entreprises';
    private resourceListeAttenteUrl = SERVER_API_URL + 'api/listeAttente/entreprises';
    private resourceValiderCompteUrl = SERVER_API_URL + 'api/registerValiderCompte';

    constructor(private http: HttpClient) { }

    create(entreprise: Entreprise): Observable<EntityResponseType> {
        const copy = this.convert(entreprise);
        return this.http.post<Entreprise>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(entreprise: Entreprise): Observable<EntityResponseType> {
        const copy = this.convert(entreprise);
        return this.http.put<Entreprise>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Entreprise>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Entreprise[]>> {
        const options = createRequestOption(req);
        return this.http.get<Entreprise[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Entreprise[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Entreprise[]>> {
        const options = createRequestOption(req);
        return this.http.get<Entreprise[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Entreprise[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Entreprise = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Entreprise[]>): HttpResponse<Entreprise[]> {
        const jsonResponse: Entreprise[] = res.body;
        const body: Entreprise[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Entreprise.
     */
    private convertItemFromServer(entreprise: Entreprise): Entreprise {
        const copy: Entreprise = Object.assign({}, entreprise);
        return copy;
    }

    /**
     * Convert a Entreprise to a JSON which can be sent to the server.
     */
    private convert(entreprise: Entreprise): Entreprise {
        const copy: Entreprise = Object.assign({}, entreprise);
        return copy;
    }

    getlisteAttente(): Observable<HttpResponse<Entreprise[]>> {
        return this.http.get<Entreprise[]>(`${this.resourceListeAttenteUrl}`, { observe: 'response'})
            .map((res: HttpResponse<Entreprise[]>) => this.convertArrayResponse(res));
    }

    validerCompte(user: User): Observable<any> {
       // const copy = this.convert(entreprise);
        return this.http.post(this.resourceValiderCompteUrl, user);
    }
}
