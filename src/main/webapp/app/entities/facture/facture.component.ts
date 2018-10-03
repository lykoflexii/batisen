import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Facture } from './facture.model';
import { FactureService } from './facture.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';

@Component({
    selector: 'jhi-facture',
    templateUrl: './facture.component.html',
    styleUrls: [
        'facture.css',
        'facture.scss'
    ]
})
export class FactureComponent implements OnInit, OnDestroy {

currentAccount: any;
    factures: Facture[];
    factures2: Facture[];
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
    itemchoisi = 'Facture Non Valider';
    itemNonchoisi = 'Facture Valider';
    message = 'VALIDEES';
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;
    dataSource: MatTableDataSource<Facture>;
    displayedColumns = ['dateCreation', 'travau', 'chantier', 'det'];

    constructor(
        private factureService: FactureService,
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
            this.factureService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort1()}).subscribe(
                    (res: HttpResponse<Facture[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.factureService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort1()}).subscribe(
                (res: HttpResponse<Facture[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/facture'], {queryParams:
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
        this.router.navigate(['/facture', {
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
        this.router.navigate(['/facture', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.load(id);
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInFactures();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Facture) {
        return item.id;
    }
    registerChangeInFactures() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.eventSubscriber = this.eventManager.subscribe('factureListModification', (response) => this.load(id));
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
        this.factures = data;
        this.dataSource = new MatTableDataSource(this.factures);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
    applyFilter(filterValue: string) {
        filterValue = filterValue.trim(); // Remove whitespace
        filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
        this.dataSource.filter = filterValue;
        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
          }
      }
      load(id) {
        // this.factureService.findFacture(id)
        //     .subscribe((clientResponse: HttpResponse<Facture[]>) => {
        //         this.factures2 = clientResponse.body;
        //         this.dataSource = new MatTableDataSource(this.factures2);
        //         this.dataSource.paginator = this.paginator;
        //         this.dataSource.sort = this.sort;
        //     });
        if (this.itemchoisi ===  'Facture Valider') {
            this.factureValider();
        }else {
            this.factureNonValider();
        }
}

teste() {
this.itemchoisi = (this.itemchoisi ===  'Facture Valider' ?  'Facture Non Valider' :  'Facture Valider') ;
this.itemNonchoisi = (this.itemchoisi ===  'Facture Valider' ?  'Facture Non Valider' :  'Facture Valider') ;
if (this.itemchoisi ===  'Facture Valider') {
    this.factureValider();
}else {
    this.factureNonValider();
}
}
factureValider() {
    const id = +sessionStorage.getItem('entreprise_id');
    this.factureService.findFacturevalide(id)
            .subscribe((clientResponse: HttpResponse<Facture[]>) => {
                this.factures = clientResponse.body;
                this.message = ' VALIDEES';
                this.dataSource = new MatTableDataSource(this.factures);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            });
}
factureNonValider() {
    const id = +sessionStorage.getItem('entreprise_id');
    this.factureService.findFactureNonvalide(id)
            .subscribe((clientResponse: HttpResponse<Facture[]>) => {
                this.factures = clientResponse.body;
                this.message = ' NON VALIDEES';
                this.dataSource = new MatTableDataSource(this.factures);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            });
}
}
