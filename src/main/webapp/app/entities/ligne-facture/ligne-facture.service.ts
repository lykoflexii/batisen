import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { LigneFacture } from './ligne-facture.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<LigneFacture>;

@Injectable()
export class LigneFactureService {

    private resourceUrl =  SERVER_API_URL + 'api/ligne-factures';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ligne-factures';
    private resourceSearchParFactureUrl = SERVER_API_URL + 'api/parFacture/ligne-factures';

    constructor(private http: HttpClient) { }

    create(ligneFacture: LigneFacture): Observable<EntityResponseType> {
        const copy = this.convert(ligneFacture);
        return this.http.post<LigneFacture>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(ligneFacture: LigneFacture): Observable<EntityResponseType> {
        const copy = this.convert(ligneFacture);
        return this.http.put<LigneFacture>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<LigneFacture>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<LigneFacture[]>> {
        const options = createRequestOption(req);
        return this.http.get<LigneFacture[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LigneFacture[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<LigneFacture[]>> {
        const options = createRequestOption(req);
        return this.http.get<LigneFacture[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LigneFacture[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: LigneFacture = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<LigneFacture[]>): HttpResponse<LigneFacture[]> {
        const jsonResponse: LigneFacture[] = res.body;
        const body: LigneFacture[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to LigneFacture.
     */
    private convertItemFromServer(ligneFacture: LigneFacture): LigneFacture {
        const copy: LigneFacture = Object.assign({}, ligneFacture);
        return copy;
    }

    /**
     * Convert a LigneFacture to a JSON which can be sent to the server.
     */
    private convert(ligneFacture: LigneFacture): LigneFacture {
        const copy: LigneFacture = Object.assign({}, ligneFacture);
        return copy;
    }

    ligneFactureByFactureId(id: number): Observable<HttpResponse<LigneFacture[]>> {
        return this.http.get<LigneFacture[]>(`${this.resourceSearchParFactureUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<LigneFacture[]>) => this.convertArrayResponse(res));
    }
}
