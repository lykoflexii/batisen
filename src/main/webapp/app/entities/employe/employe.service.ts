import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Employe } from './employe.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Employe>;

@Injectable()
export class EmployeService {

    private resourceUrl =  SERVER_API_URL + 'api/employes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employes';
    private resourceEntrepriseUrl = SERVER_API_URL + 'api/filter/employes';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(employe: Employe): Observable<EntityResponseType> {
        const copy = this.convert(employe);
        return this.http.post<Employe>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(employe: Employe): Observable<EntityResponseType> {
        const copy = this.convert(employe);
        return this.http.put<Employe>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Employe>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Employe[]>> {
        const options = createRequestOption(req);
        return this.http.get<Employe[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Employe[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Employe[]>> {
        const options = createRequestOption(req);
        return this.http.get<Employe[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Employe[]>) => this.convertArrayResponse(res));
    }
    findEmploye(id: number): Observable<HttpResponse<Employe[]>> {
        return this.http.get<Employe[]>(`${this.resourceEntrepriseUrl}/${id}`, { observe: 'response'})
        .map((res: HttpResponse<Employe[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Employe = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Employe[]>): HttpResponse<Employe[]> {
        const jsonResponse: Employe[] = res.body;
        const body: Employe[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Employe.
     */
    private convertItemFromServer(employe: Employe): Employe {
        const copy: Employe = Object.assign({}, employe);
        copy.dateNaissance = this.dateUtils
            .convertLocalDateFromServer(employe.dateNaissance);
        return copy;
    }

    /**
     * Convert a Employe to a JSON which can be sent to the server.
     */
    private convert(employe: Employe): Employe {
        const copy: Employe = Object.assign({}, employe);
        copy.dateNaissance = this.dateUtils
            .convertLocalDateToServer(employe.dateNaissance);
        return copy;
    }
}
