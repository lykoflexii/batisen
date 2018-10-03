import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Facture } from './facture.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Facture>;

@Injectable()
export class FactureService {

    private resourceUrl =  SERVER_API_URL + 'api/factures';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/factures';
    private resourceLastIdUrl =  SERVER_API_URL + 'api/last/factures';
    private resourceEntreprisedUrl =  SERVER_API_URL + 'api/facEntreprise/factures';
    private resourceValidedUrl =  SERVER_API_URL + 'api/factureValide/factures';
    private resourceNonValidedUrl =  SERVER_API_URL + 'api/factureNonValide/factures';
    private resourceChanTravdUrl =  SERVER_API_URL + 'api/facChanTrav/factures';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(facture: Facture): Observable<EntityResponseType> {
        const copy = this.convert(facture);
        return this.http.post<Facture>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(facture: Facture): Observable<EntityResponseType> {
        const copy = this.convert(facture);
        return this.http.put<Facture>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Facture>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Facture[]>> {
        const options = createRequestOption(req);
        return this.http.get<Facture[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Facture[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Facture[]>> {
        const options = createRequestOption(req);
        return this.http.get<Facture[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Facture[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Facture = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Facture[]>): HttpResponse<Facture[]> {
        const jsonResponse: Facture[] = res.body;
        const body: Facture[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Facture.
     */
    private convertItemFromServer(facture: Facture): Facture {
        const copy: Facture = Object.assign({}, facture);
        copy.dateCreation = this.dateUtils
            .convertLocalDateFromServer(facture.dateCreation);
        return copy;
    }

    /**
     * Convert a Facture to a JSON which can be sent to the server.
     */
    private convert(facture: Facture): Facture {
        const copy: Facture = Object.assign({}, facture);
        copy.dateCreation = this.dateUtils
            .convertLocalDateToServer(facture.dateCreation);
        return copy;
    }

    lastId(id: number, id2: number): Observable<HttpResponse<number>> {
        return this.http.get<number>(`${this.resourceLastIdUrl}/${id}/${id2}`, { observe: 'response'}).map(
            (res: HttpResponse<number>) => res
        );
    }

    findFacture(id: number): Observable<HttpResponse<Facture[]>> {
        return this.http.get<Facture[]>(`${this.resourceEntreprisedUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Facture[]>) => this.convertArrayResponse(res));
    }
    findFactChanTrav(id: number, id2: number): Observable<HttpResponse<Facture>> {
        return this.http.get<Facture>(`${this.resourceEntreprisedUrl}/${id}/${id2}`, { observe: 'response'})
            .map((res: HttpResponse<Facture>) => this.convertResponse(res));
    }
    findFacturevalide(id: number): Observable<HttpResponse<Facture[]>> {
        return this.http.get<Facture[]>(`${this.resourceValidedUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Facture[]>) => this.convertArrayResponse(res));
    }
    findFactureNonvalide(id: number): Observable<HttpResponse<Facture[]>> {
        return this.http.get<Facture[]>(`${this.resourceNonValidedUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<Facture[]>) => this.convertArrayResponse(res));
    }
}
