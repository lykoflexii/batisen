import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Devis } from './devis.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Devis>;

@Injectable()
export class DevisService {

    private resourceUrl =  SERVER_API_URL + 'api/devis';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/devis';
    private resourceLastIdUrl = SERVER_API_URL + 'api/last/devis';
    private resourceLastDevisUrl = SERVER_API_URL + 'api/lastDevis/devis';
    private resourceSearchparClientIdUrl = SERVER_API_URL + 'api/parClient/devis';
    private resourceSearchparChantierIdUrl = SERVER_API_URL + 'api/parChantier/devis';
    private resourceEntrepriseUrl = SERVER_API_URL + 'api/filter/devis';
    private resourceCinqDernierUrl = SERVER_API_URL + 'api/cinqDernier/devis';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(devis: Devis): Observable<EntityResponseType> {
        const copy = this.convert(devis);
        return this.http.post<Devis>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(devis: Devis): Observable<EntityResponseType> {
        const copy = this.convert(devis);
        return this.http.put<Devis>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Devis>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Devis[]>> {
        const options = createRequestOption(req);
        return this.http.get<Devis[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Devis[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Devis[]>> {
        const options = createRequestOption(req);
        return this.http.get<Devis[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Devis[]>) => this.convertArrayResponse(res));
    }

    findDevis(id: number): Observable<HttpResponse<Devis[]>> {
        return this.http.get<Devis[]>(`${this.resourceEntrepriseUrl}/${id}`, { observe: 'response'})
        .map((res: HttpResponse<Devis[]>) => this.convertArrayResponse(res));
    }
    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Devis = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Devis[]>): HttpResponse<Devis[]> {
        const jsonResponse: Devis[] = res.body;
        const body: Devis[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Devis.
     */
    private convertItemFromServer(devis: Devis): Devis {
        const copy: Devis = Object.assign({}, devis);
        copy.dateDeCreation = this.dateUtils
            .convertLocalDateFromServer(devis.dateDeCreation);
        copy.validite = this.dateUtils
            .convertLocalDateFromServer(devis.validite);
        return copy;
    }

    /**
     * Convert a Devis to a JSON which can be sent to the server.
     */
    private convert(devis: Devis): Devis {
        const copy: Devis = Object.assign({}, devis);
        copy.dateDeCreation = this.dateUtils
            .convertLocalDateToServer(devis.dateDeCreation);
        copy.validite = this.dateUtils
            .convertLocalDateToServer(devis.validite);
        return copy;
    }

    lastId(): Observable<HttpResponse<number>> {
        return this.http.get<number>(`${this.resourceLastIdUrl}`, { observe: 'response'}).map(
            (res: HttpResponse<number>) => res
        );
    }
    lastDevis(): Observable<EntityResponseType> {
        return this.http.get<Devis>(`${this.resourceLastDevisUrl}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }
    devisByClientId(id: number):  Observable<HttpResponse<Devis[]>> {
        return this.http.get<Devis[]>(`${this.resourceSearchparClientIdUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Devis[]>) => this.convertArrayResponse(res));
    }
    devisByChantierId(id: number):  Observable<HttpResponse<Devis>> {
        return this.http.get<Devis>(`${this.resourceSearchparChantierIdUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Devis>) => this.convertResponse(res));
    }

    cinqDerniersDevis(id: number): Observable<HttpResponse<Devis[]>> {
        return this.http.get<Devis[]>(`${this.resourceCinqDernierUrl}/${id}`, { observe: 'response'})
        .map((res: HttpResponse<Devis[]>) => this.convertArrayResponse(res));
    }
}
