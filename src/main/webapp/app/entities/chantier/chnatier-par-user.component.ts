import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Chantier } from './chantier.model';
import { ChantierService } from './chantier.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { Chantiers } from './chantiers.model';

@Component({
  selector: 'jhi-chnatier-par-user',
  templateUrl: './chnatier-par-user.component.html',
  styleUrls: [
    'chantier.css',
    'chantier.scss'
]
})
export class ChnatierParUserComponent implements OnInit, OnDestroy {
  currentAccount: any;
chantiers: Chantier[];
chantiers2: Chantier[];
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
displayedColumns = ['id', 'nomChantier', 'dateDebutPrevu', 'dateFinPrevu', 'dateDebutReelle', 'dateFinReelle', 'det'];
dataSource: MatTableDataSource<Chantier>;

  constructor(
    private chantierService: ChantierService,
    private parseLinks: JhiParseLinks,
    private jhiAlertService: JhiAlertService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private eventManager: JhiEventManager
  ) { }

  loadAll() {
    if (this.currentSearch) {
        this.chantierService.search({
            page: this.page - 1,
            query: this.currentSearch,
            size: this.itemsPerPage,
            sort: this.sort1()}).subscribe(
                (res: HttpResponse<Chantier[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        return;
    }
    this.chantierService.query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort1()}).subscribe(
            (res: HttpResponse<Chantier[]>) => this.onSuccess(res.body, res.headers),
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
    this.router.navigate(['/chantier'], {queryParams:
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
    this.router.navigate(['/chantier', {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    }]);
    this.loadAll();
}
previousState() {
    window.history.back();
}
search(query) {
    if (!query) {
        return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.router.navigate(['/chantier', {
        search: this.currentSearch,
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
    }]);
    this.loadAll();
}
ngOnInit() {
    // this.loadAll();
    this.load();
    this.principal.identity().then((account) => {
        this.currentAccount = account;
    });
    this.registerChangeInChantiers();
}

ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
}

trackId(index: number, item: Chantier) {
    return item.id;
}
registerChangeInChantiers() {
    this.eventSubscriber = this.eventManager.subscribe('chantierListModification', (response) => this.load());
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
    this.chantiers = data;
}
private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
}
load() {
    this.chantierService.findByCurrentUser()
        .subscribe((chantierResponse: HttpResponse<Chantier[]>) => {
            this.chantiers2 = chantierResponse.body;
            this.dataSource = new MatTableDataSource(this.chantiers2);
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
