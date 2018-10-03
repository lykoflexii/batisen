import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { LigneDevis } from './ligne-devis.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<LigneDevis>;

@Injectable()
export class LigneDevisService {

    private resourceUrl =  SERVER_API_URL + 'api/ligne-devis';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ligne-devis';
    private resourceSearchParDevisUrl = SERVER_API_URL + 'api/parDevis/ligne-devis';

    constructor(private http: HttpClient) { }

    create(ligneDevis: LigneDevis): Observable<EntityResponseType> {
        const copy = this.convert(ligneDevis);
        return this.http.post<LigneDevis>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(ligneDevis: LigneDevis): Observable<EntityResponseType> {
        const copy = this.convert(ligneDevis);
        return this.http.put<LigneDevis>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<LigneDevis>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<LigneDevis[]>> {
        const options = createRequestOption(req);
        return this.http.get<LigneDevis[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LigneDevis[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<LigneDevis[]>> {
        const options = createRequestOption(req);
        return this.http.get<LigneDevis[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LigneDevis[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: LigneDevis = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<LigneDevis[]>): HttpResponse<LigneDevis[]> {
        const jsonResponse: LigneDevis[] = res.body;
        const body: LigneDevis[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to LigneDevis.
     */
    private convertItemFromServer(ligneDevis: LigneDevis): LigneDevis {
        const copy: LigneDevis = Object.assign({}, ligneDevis);
        return copy;
    }

    /**
     * Convert a LigneDevis to a JSON which can be sent to the server.
     */
    private convert(ligneDevis: LigneDevis): LigneDevis {
        const copy: LigneDevis = Object.assign({}, ligneDevis);
        return copy;
    }

    ligneDevisByDevisId(id: number): Observable<HttpResponse<LigneDevis[]>> {
        return this.http.get<LigneDevis[]>(`${this.resourceSearchParDevisUrl}/${id}`, { observe: 'response'})
            .map((res: HttpResponse<LigneDevis[]>) => this.convertArrayResponse(res));
    }
}
