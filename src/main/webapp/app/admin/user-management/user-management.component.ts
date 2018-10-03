import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE, Principal, User, UserService } from '../../shared';
import { MatTableDataSource, MatSort, MatPaginator, MatIconRegistry } from '@angular/material';
import { DataSource } from '@angular/cdk/table';
import {DomSanitizer} from '@angular/platform-browser';
import { Entreprise, EntrepriseService } from '../../entities/entreprise';
import PNotify from 'pnotify/dist/es/PNotify';

@Component({
    selector: 'jhi-user-mgmt',
    templateUrl: './user-management.component.html',
    styleUrls: [
        'user.css',
        'user.scss'
    ]
})
export class UserMgmtComponent implements OnInit, OnDestroy {

    currentAccount: any;
    users: User[];
    error: any;
    success: any;
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
    displayedColumns = ['login', 'firstName', 'lastName', 'email', 'det'];
    dataSource: MatTableDataSource<User>;
    entreprise = new Entreprise();
    nombre = 0;

    constructor(
        private userService: UserService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private entrepriseService: EntrepriseService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            PNotify.defaults.styling = 'bootstrap4';
            PNotify.defaults.width = '400px';
            PNotify.defaults.heigth = '400px';
            PNotify.defaults.icons = 'fontawesome4';
            PNotify.defaults.delay = 3000;
        });
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInUsers();
        });
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    registerChangeInUsers() {
        this.eventManager.subscribe('userListModification', (response) => this.loadAll());
    }

    setActive(user, isActivated) {
        user.activated = isActivated;

        this.userService.update(user).subscribe(
            (response) => {
                if (response.status === 200) {
                    this.error = null;
                    this.success = 'OK';
                    this.loadAll();
                } else {
                    this.success = null;
                    this.error = 'ERROR';
                }
            });
    }

    loadAll() {
        this.userService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort1()}).subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
        );
    }

    trackIdentity(index, item: User) {
        return item.id;
    }

    sort1() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/user-management'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data;
        this.nombre = this.users.length;
        this.dataSource = new MatTableDataSource(this.users);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.entrepriseService.find(this.users[0].entreprise.id).subscribe((reponse) => {
            this.entreprise = reponse.body;
        });
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
    applyFilter(filterValue: string) {
        filterValue = filterValue.trim(); // Remove whitespace
        filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
        this.dataSource.filter = filterValue;
        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
          }
      }
      packLimite() {
        return this.entreprise.pack === this.nombre;
      }

      limiteAtteinte() {
        PNotify.error({
            text: '\n\n Vous avez atteint le nombre maximal d\'utlistaeur permis\n\n',
            stack: {
              dir1: 'down', dir2: 'right', // Position from the top left corner.
              firstpos1: 60, firstpos2: 400 // 90px from the top, 90px from the left.
            }
          });
        }
}
