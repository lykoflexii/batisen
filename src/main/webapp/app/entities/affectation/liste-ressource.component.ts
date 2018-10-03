import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Affectation } from './affectation.model';
import { AffectationService } from './affectation.service';
import {ChantierService} from '../chantier/chantier.service';
import {Chantier} from '../chantier/chantier.model';
import {EtatAffectation} from './affectation.model';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { Chart } from 'chart.js';
import { MatTableDataSource, MatSort, MatPaginator, MatIconRegistry } from '@angular/material';
import { DataSource } from '@angular/cdk/table';
import {DomSanitizer} from '@angular/platform-browser';
import { Employe } from '../employe';

@Component({
  selector: 'jhi-liste-ressource',
  templateUrl: './liste-ressource.component.html',
  styleUrls: [
    'affectation.css',
    'affectation.scss'
]
})
export class ListeRessourceComponent implements OnInit, OnDestroy {
  affectations: Affectation[];
  affectation: Affectation = new Affectation();
  id: number;
  private subscription: Subscription;
  private eventSubscriber: Subscription;
  isMess = true;
  isSaving: boolean;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  displayedColumns = ['id', 'nomEmploye', 'prenomEmploye', 'matricule', 'fonction', 'det'];
  dataSource: MatTableDataSource<Employe>;
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
      private affectationService: AffectationService,
      private route: ActivatedRoute,
      private chantierService: ChantierService,
      private router: Router,
      private jhiAlertService: JhiAlertService
  ) {
  }

  ngOnInit() {
      this.afficherMessage();
      this.subscription = this.route.params.subscribe((params) => {
          this.id = params['id'];
          this.load(params['id']);
         // sessionStorage.setItem('ida', params['id'] );
      });
      this.registerChangeInAffectations();
  }
  load(id) {
      this.affectationService.find(id)
          .subscribe((affectationResponse: HttpResponse<Affectation>) => {
              this.affectation = affectationResponse.body;
              this.dataSource = new MatTableDataSource(this.affectation.employes);
              this.dataSource.paginator = this.paginator;
              this.dataSource.sort = this.sort;
              // this.affectation = this.affectations[0];
                  // this.chantierService.find(id)
                  //     .subscribe((chantierResponse: HttpResponse<Chantier>) => {
                  //         this.chantier = chantierResponse.body;
                  //     });
          });
  }
  previousState() {
      this.router.navigate(['/affectations/', +sessionStorage.getItem('idChantier') ]);
  }

  ngOnDestroy() {
      this.subscription.unsubscribe();
      this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInAffectations() {
      this.eventSubscriber = this.eventManager.subscribe(
          'affectationListModification',
          (response) => this.load(this.id)
      );
  }

  afficherMessage() {
      setTimeout(
          () => {
              this.isMess = false;
          }, 4000
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
    supprimerRessource(idr: number) {
      let i = 0;
      for ( const employe of this.affectation.employes) {
        if (employe.id === idr) {
         const employes = this.affectation.employes.splice(i, 1);
        }
        i++;
      }
      this.affectation.dateDebut = new Date(this.affectation.dateDebut);
      this.affectation.dateFin = new Date(this.affectation.dateFin);
      this.save();
      this.dataSource = new MatTableDataSource(this.affectation.employes);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
    save() {
      this.isSaving = true;
      if (this.affectation.id !== undefined) {
          this.subscribeToSaveResponse(
              this.affectationService.update1(this.affectation));
      } else {
          this.subscribeToSaveResponse(
              this.affectationService.create(this.affectation));
      }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<Affectation>>) {
      result.subscribe((res: HttpResponse<Affectation>) =>
          this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess(result: Affectation) {
      this.eventManager.broadcast({ name: 'affectationListModification', content: 'OK'});
      this.isSaving = false;
      this.load(this.id);
  }

  private onSaveError() {
      this.isSaving = false;
  }

  private onError(error: any) {
      this.jhiAlertService.error(error.message, null, null);
  }
}
