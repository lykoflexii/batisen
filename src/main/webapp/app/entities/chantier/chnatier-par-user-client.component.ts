import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Chantier } from './chantier.model';
import { ChantierService } from './chantier.service';
import {Chantiers} from './chantiers.model';
import { ClientService, Client } from '../client';
import 'rxjs/add/operator/map';
import { Chart } from 'chart.js';
import { DevisService, Devis } from '../devis';
import { MatTableDataSource, MatSort, MatPaginator, MatIconRegistry } from '@angular/material';

@Component({
  selector: 'jhi-chnatier-par-user-client',
  templateUrl: './chnatier-par-user-client.component.html',
  styleUrls: [
    'chantier.css',
    'chantier.scss'
]
})
export class ChnatierParUserClientComponent implements OnInit, OnDestroy {

  chantiers: Chantiers[];
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    id: number;
    isCollapsed = [true];
    isCollapsed1 = [true];
    isCollapsed2 = [true];
    isCollapsed3 = [true];
    client: Client = new Client();
    devis: Devis [] = [];
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;
    displayedColumns = ['id', 'nomChantier', 'dateDebutPrevu', 'dateFinPrevu', 'dateDebutReelle', 'dateFinReelle', 'det'];
    dataSource: MatTableDataSource<Chantiers>;
    public lineChartData: Array<any> = [
        {data: [65, 59, 80, 81, 56, 55, 40], label: 'Series A'},
        {data: [28, 48, 40, 19, 86, 27, 90], label: 'Series B'},
        {data: [18, 48, 77, 9, 100, 27, 40], label: 'Series C'}
      ];
      public lineChartLabels: Array<any> = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
      public lineChartOptions: any = {
        responsive: true
      };
      panelOpenState = false;
      constructor(
        private eventManager: JhiEventManager,
        private chantierService: ChantierService,
        private route: ActivatedRoute,
        private devisService: DevisService,
        private clientService: ClientService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
           this.id = params['id'];
           this.load(params['id']);
           this.clientService.find(params['id'])
            .subscribe((clientResponse: HttpResponse<Client>) => {
                this.client = clientResponse.body;
            });
           sessionStorage.setItem('idClient', params['id'] );
        });
       this.registerChangeInChantiers();
    }

    load(id: number) {
        this.chantierService.findByCurrentUserClient(id)
            .subscribe((chantierResponse: HttpResponse<Chantiers[]>) => {
                this.chantiers = chantierResponse.body;
                this.dataSource = new MatTableDataSource(this.chantiers);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            });
    }
    previousState() {
        this.router.navigate(['/client']);
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

   registerChangeInChantiers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'chantierListModification',
            (response) => this.load(this.id)
        );
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
