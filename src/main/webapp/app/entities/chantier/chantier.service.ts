import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Chantier } from './chantier.model';
import { Chantiers } from './chantiers.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Chantier>;

@Injectable()
export class ChantierService {

    private resourceUrl =  SERVER_API_URL + 'api/chantiers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/chantiers';
    private resourceUrls =  SERVER_API_URL + 'api/chantierss';
    private resourceEntrepriseUrl = SERVER_API_URL + 'api/filter/chantiers';
    private resourceUserUrl = SERVER_API_URL + 'api/parUser/chantiers';
    private resourceUserClientUrl = SERVER_API_URL + 'api/parUserClient/chantiers';
    private resourceEtatChantierUrl = SERVER_API_URL + 'api/etatChantier/chantiers';
    private resourceEtatChantierParClientUrl = SERVER_API_URL + 'api/etatChantierParClient/chantiers';
    // private resourceEnCoursUrl = SERVER_API_URL + 'api/enCours/chantiers';
    // private resourceEnRetardUrl = SERVER_API_URL + 'api/enRetard/chantiers';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(chantier: Chantier): Observable<EntityResponseType> {
        const copy = this.convert(chantier);
        return this.http.post<Chantier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(chantier: Chantier): Observable<EntityResponseType> {
        const copy = this.convert(chantier);
        return this.http.put<Chantier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update2(chantier: Chantier): Observable<EntityResponseType> {
        // const copy = this.convert(chantier);
        const copy = chantier;
        return this.http.put<Chantier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse2(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Chantier>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Chantier[]>> {
        const options = createRequestOption(req);
        return this.http.get<Chantier[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Chantier[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Chantier[]>> {
        const options = createRequestOption(req);
        return this.http.get<Chantier[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Chantier[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Chantier = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Chantier[]>): HttpResponse<Chantier[]> {
        const jsonResponse: Chantier[] = res.body;
        const body: Chantier[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Chantier.
     */
    private convertItemFromServer(chantier: Chantier): Chantier {
        const copy: Chantier = Object.assign({}, chantier);
        copy.dateDebutReelle = this.dateUtils
            .convertLocalDateFromServer(chantier.dateDebutReelle);
        copy.dateFinReelle = this.dateUtils
            .convertLocalDateFromServer(chantier.dateFinReelle);
        copy.dateDebutPrevu = this.dateUtils
            .convertLocalDateFromServer(chantier.dateDebutPrevu);
        copy.dateFinPrevu = this.dateUtils
            .convertLocalDateFromServer(chantier.dateFinPrevu);
        return copy;
    }

    /**
     * Convert a Chantier to a JSON which can be sent to the server.
     */
    private convert(chantier: Chantier): Chantier {
        const copy: Chantier = Object.assign({}, chantier);
        copy.dateDebutReelle = this.dateUtils
            .convertLocalDateToServer(chantier.dateDebutReelle);
        copy.dateFinReelle = this.dateUtils
            .convertLocalDateToServer(chantier.dateFinReelle);
        copy.dateDebutPrevu = this.dateUtils
            .convertLocalDateToServer(chantier.dateDebutPrevu);
        copy.dateFinPrevu = this.dateUtils
            .convertLocalDateToServer(chantier.dateFinPrevu);
        return copy;
    }

    /**
     * retourne la liste des chantiers pour une client donne
     */
    findByClient(id: number): Observable<HttpResponse<Chantiers[]>> {
        return this.http.get<Chantiers[]>(`${this.resourceUrls}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Chantiers[]>) => this.convertArrayResponses(res));
    }

    /**
     * retourne la liste des chantiers pour une client donne
     */
    findByCurrentUser(): Observable<HttpResponse<Chantier[]>> {
        return this.http.get<Chantier[]>(`${this.resourceUserUrl}`, { observe: 'response'})
            .map((res: HttpResponse<Chantier[]>) => this.convertArrayResponse(res));
    }

    findByCurrentUserClient(id: number): Observable<HttpResponse<Chantiers[]>> {
        return this.http.get<Chantiers[]>(`${this.resourceUserClientUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Chantiers[]>) => this.convertArrayResponses(res));
    }

    /**
     * retourne la liste des chantiers pour une client donne
     */
    findChantier(id: number): Observable<HttpResponse<Chantier[]>> {
        return this.http.get<Chantier[]>(`${this.resourceEntrepriseUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Chantier[]>) => this.convertArrayResponse(res));
    }

     /**
     * retourne la liste des chantiers pour une client donne
     */
    findEtatChantier(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceEtatChantierUrl}/${id}`, { observe: 'response'})
            .map( (res: HttpResponse<any>) => res);
    }

    findEtatChantierParClient(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceEtatChantierParClientUrl}/${id}`, { observe: 'response'})
            .map( (res: HttpResponse<any>) => res);
    }

     /**
     * retourne la liste des chantiers pour une client donne
     */
    // findChantiereEnCours(id: number): Observable<HttpResponse<number>> {
    //     return this.http.get<number>(`${this.resourceEnCoursUrl}/${id}`, { observe: 'response'})
    //         .map( (res: HttpResponse<number>) => res);
    // }

     /**
     * retourne la liste des chantiers pour une client donne
     */
    // findChantierEnRetard(id: number): Observable<HttpResponse<number>> {
    //     return this.http.get<number>(`${this.resourceEnRetardUrl}/${id}`, { observe: 'response'}).map(
    //             (res: HttpResponse<number>) => res
    //         );
    // }
    private convertArrayResponses(res: HttpResponse<Chantiers[]>): HttpResponse<Chantiers[]> {
        const jsonResponse: Chantiers[] = res.body;
        const body: Chantiers[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServers(jsonResponse[i]));
        }
        return res.clone({body});
    }
    /**
     * Convert a returned JSON object to Chantier.
     */
    private convertItemFromServers(chantier: Chantiers): Chantiers {
        const copy: Chantiers = Object.assign({}, chantier);
        copy.dateDebutReelle = this.dateUtils
            .convertLocalDateFromServer(chantier.dateDebutReelle);
        copy.dateFinReelle = this.dateUtils
            .convertLocalDateFromServer(chantier.dateFinReelle);
        copy.dateDebutPrevu = this.dateUtils
            .convertLocalDateFromServer(chantier.dateDebutPrevu);
        copy.dateFinPrevu = this.dateUtils
            .convertLocalDateFromServer(chantier.dateFinPrevu);
        return copy;
    }

    private convertResponse2(res: EntityResponseType): EntityResponseType {
        // const body: Chantier = this.convertItemFromServer(res.body);
        return res;
    }
}
