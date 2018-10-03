import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Employe } from './employe.model';
import { EmployeService } from './employe.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { MatTableDataSource, MatSort, MatPaginator, MatIconRegistry } from '@angular/material';

@Component({
    selector: 'jhi-employe',
    templateUrl: './employe.component.html',
    styleUrls: [
        'employe.css',
        'employe.scss'
    ]
})
export class EmployeComponent implements OnInit, OnDestroy {

currentAccount: any;
    employes: Employe[];
    employes2: Employe[];
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
    displayedColumns = ['matricule', 'nomEmploye', 'prenomEmploye', 'fonction', 'det'];
    dataSource: MatTableDataSource<Employe>;

    constructor(
        private employeService: EmployeService,
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
            this.employeService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort1()}).subscribe(
                    (res: HttpResponse<Employe[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.employeService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort1()}).subscribe(
                (res: HttpResponse<Employe[]>) => this.onSuccess(res.body, res.headers),
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
        this.router.navigate(['/employe'], {queryParams:
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
        this.router.navigate(['/employe', {
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
        this.router.navigate(['/employe', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.load(id);
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInEmployes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Employe) {
        return item.id;
    }
    registerChangeInEmployes() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.eventSubscriber = this.eventManager.subscribe('employeListModification', (response) =>  this.load(id));
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
        this.employes = data;
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
        this.employeService.findEmploye(id)
            .subscribe((employeResponse: HttpResponse<Employe[]>) => {
                this.employes2 = employeResponse.body;
                this.dataSource = new MatTableDataSource(this.employes2);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            });
    }
}
