import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Client } from './client.model';
import { ClientService } from './client.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';

@Component({
    selector: 'jhi-client',
    templateUrl: './client.component.html',
    styleUrls: [
        'client.css',
        'client.scss'
    ]
})
export class ClientComponent implements OnInit, OnDestroy {

currentAccount: any;
    clients: Client[];
    clients2: Client[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;
    dataSource: MatTableDataSource<Client>;
    displayedColumns = ['nomClient', 'villeClient', 'adresseClient', 'det'];

    constructor(
        private clientService: ClientService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.clientService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort1()}).subscribe(
                    (res: HttpResponse<Client[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.clientService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort1()}).subscribe(
                (res: HttpResponse<Client[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/client'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/client', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/client', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.load(id);
        // this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInClients();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Client) {
        return item.id;
    }
    registerChangeInClients() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.eventSubscriber = this.eventManager.subscribe('clientListModification', (response) => this.load(id));
    }

    sort1() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.clients = data;
        // this.dataSource = new MatTableDataSource(this.clients);
        //         this.dataSource.paginator = this.paginator;
        //         this.dataSource.sort = this.sort;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
    load(id) {
        this.clientService.findClient(id)
            .subscribe((chantierResponse: HttpResponse<Client[]>) => {
                this.clients2 = chantierResponse.body;
                this.dataSource = new MatTableDataSource(this.clients2);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            });
    }
    applyFilter(filterValue: string) {
        filterValue = filterValue.trim(); // Remove whitespace
        filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
        this.dataSource.filter = filterValue;
        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
          }
      }
}
