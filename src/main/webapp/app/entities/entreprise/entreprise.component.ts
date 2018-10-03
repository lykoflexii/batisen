import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntrepriseService } from './entreprise.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-entreprise',
    templateUrl: './entreprise.component.html'
})
export class EntrepriseComponent implements OnInit, OnDestroy {
entreprises: Entreprise[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private entrepriseService: EntrepriseService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.entrepriseService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<Entreprise[]>) => this.entreprises = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.entrepriseService.query().subscribe(
            (res: HttpResponse<Entreprise[]>) => {
                this.entreprises = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInEntreprises();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Entreprise) {
        return item.id;
    }
    registerChangeInEntreprises() {
        this.eventSubscriber = this.eventManager.subscribe('entrepriseListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
