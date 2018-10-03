import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Affectation } from './affectation.model';
import { createRequestOption } from '../../shared';
import { Affectation2 } from './affectations.model';

export type EntityResponseType = HttpResponse<Affectation>;

@Injectable()
export class AffectationService {

    private resourceUrl =  SERVER_API_URL + 'api/affectations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/affectations';
    private resourceUrls =  SERVER_API_URL + 'api/affectationss';
    private resourceUrlss =  SERVER_API_URL + 'api/affectationsss';
    private resourceTerminerUrl = SERVER_API_URL + 'api/etatTravaux/affectations';
    private resourceCourChantierTacheUrl = SERVER_API_URL + 'api/coutMainDoeuvreChantierPartache/affectations';
    private resourceEnRetardUrl = SERVER_API_URL + 'api/enretard/affectations';
    private resourceCoutChantierUrl = SERVER_API_URL + 'api/coutChantier/affectations';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(affectation: Affectation): Observable<EntityResponseType> {
        const copy = this.convert(affectation);
        return this.http.post<Affectation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(affectation: Affectation): Observable<EntityResponseType> {
        const copy = this.convert(affectation);
        return this.http.put<Affectation>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Affectation>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Affectation[]>> {
        const options = createRequestOption(req);
        return this.http.get<Affectation[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Affectation[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Affectation[]>> {
        const options = createRequestOption(req);
        return this.http.get<Affectation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Affectation[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Affectation = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Affectation[]>): HttpResponse<Affectation[]> {
        const jsonResponse: Affectation[] = res.body;
        const body: Affectation[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertArrayResponse2(res: HttpResponse<Affectation2[]>): HttpResponse<Affectation2[]> {
        const jsonResponse: Affectation2[] = res.body;
        const body: Affectation2[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer2(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Affectation.
     */
    private convertItemFromServer(affectation: Affectation): Affectation {
        const copy: Affectation = Object.assign({}, affectation);
        copy.dateDebut = this.dateUtils
            .convertLocalDateFromServer(affectation.dateDebut);
        copy.dateFin = this.dateUtils
            .convertLocalDateFromServer(affectation.dateFin);
        return copy;
    }

    /**
     * Convert a returned JSON object to Affectation.
     */
    private convertItemFromServer2(affectation: Affectation2): Affectation2 {
        const copy: Affectation2 = Object.assign({}, affectation);
        copy.dateDebut = this.dateUtils
            .convertLocalDateFromServer(affectation.dateDebut);
        copy.dateFin = this.dateUtils
            .convertLocalDateFromServer(affectation.dateFin);
        return copy;
    }
    update1(affectation: Affectation): Observable<EntityResponseType> {
        // const copy = this.convert(affectation);
        return this.http.put<Affectation>(this.resourceUrl, affectation, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    /**
     * Convert a Affectation to a JSON which can be sent to the server.
     */
    private convert(affectation: Affectation): Affectation {
        const copy: Affectation = Object.assign({}, affectation);
        copy.dateDebut = this.dateUtils
            .convertLocalDateToServer(affectation.dateDebut);
        copy.dateFin = this.dateUtils
            .convertLocalDateToServer(affectation.dateFin);
        return copy;
    }

    findByChantier(id: number): Observable<HttpResponse<Affectation[]>> {
        return this.http.get<Affectation[]>(`${this.resourceUrls}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Affectation[]>) => this.convertArrayResponse(res));
    }

    findByChantier2(id: number): Observable<HttpResponse<Affectation2[]>> {
        return this.http.get<Affectation2[]>(`${this.resourceUrls}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Affectation2[]>) => this.convertArrayResponse2(res));
    }

    findByTravaux(id: number): Observable<HttpResponse<Affectation[]>> {
        return this.http.get<Affectation[]>(`${this.resourceUrlss}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Affectation[]>) => this.convertArrayResponse(res));
    }

    /**
     * retourne la liste des chantiers pour une client donne
     */
    findEtatTravaux(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceTerminerUrl}/${id}`, { observe: 'response'})
            .map( (res: HttpResponse<any>) => res);
    }

    findChantierParTache(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceCourChantierTacheUrl}/${id}`, { observe: 'response'})
            .map( (res: HttpResponse<any>) => res);
    }

    coutChantier(id: number): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceCoutChantierUrl}/${id}`, { observe: 'response'})
            .map( (res: HttpResponse<any>) => res);
    }

     /**anyany
     * retourne la liste des chantiers pour une client donne
     */
    // findTravauxEnCours(id: number): Observable<HttpResponse<number>> {
    //     return this.http.get<number>(`${this.resourceEnCoursUrl}/${id}`, { observe: 'response'})
    //         .map( (res: HttpResponse<number>) => res);
    // }

     /**
     * retourne la liste des chantiers pour une client donne
     */
    // findTravauxEnRetard(id: number): Observable<HttpResponse<number>> {
    //     return this.http.get<number>(`${this.resourceEnRetardUrl}/${id}`, { observe: 'response'}).map(
    //             (res: HttpResponse<number>) => res
    //         );
    // }
}
