import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Entreprise } from './entreprise.model';
import { EntrepriseService } from './entreprise.service';
import { ITEMS_PER_PAGE, Principal} from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-entreprise-mgmt',
    templateUrl: './entreprise-management.component.html'
})
export class EntrepriseMgmtComponent implements OnInit, OnDestroy {
    entreprises: Entreprise[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: any;
    page: any;
    previousPage: any;
    routeData: any;
    links: any;
    reverse: any;
    predicate: any;
    totalItems: any;
    queryCount: any;

    constructor(
        private entrepriseService: EntrepriseService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInEntreprises();
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    loadAll() {
        this.entrepriseService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: HttpResponse<Entreprise[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/entreprise-management'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    trackId(index: number, item: Entreprise) {
        return item.id;
    }

    registerChangeInEntreprises() {
        this.eventSubscriber = this.eventManager.subscribe('entrepriseListModification', (response) => this.loadAll());
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.entreprises = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
