import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Travaux } from './travaux.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Travaux>;

@Injectable()
export class TravauxService {

    private resourceUrl =  SERVER_API_URL + 'api/travauxes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/travauxes';

    constructor(private http: HttpClient) { }

    create(travaux: Travaux): Observable<EntityResponseType> {
        const copy = this.convert(travaux);
        return this.http.post<Travaux>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(travaux: Travaux): Observable<EntityResponseType> {
        const copy = this.convert(travaux);
        return this.http.put<Travaux>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Travaux>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Travaux[]>> {
        const options = createRequestOption(req);
        return this.http.get<Travaux[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Travaux[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Travaux[]>> {
        const options = createRequestOption(req);
        return this.http.get<Travaux[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Travaux[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Travaux = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Travaux[]>): HttpResponse<Travaux[]> {
        const jsonResponse: Travaux[] = res.body;
        const body: Travaux[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Travaux.
     */
    private convertItemFromServer(travaux: Travaux): Travaux {
        const copy: Travaux = Object.assign({}, travaux);
        return copy;
    }

    /**
     * Convert a Travaux to a JSON which can be sent to the server.
     */
    private convert(travaux: Travaux): Travaux {
        const copy: Travaux = Object.assign({}, travaux);
        return copy;
    }
}
