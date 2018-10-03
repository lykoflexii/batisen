import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Affectation } from './affectation.model';
import { AffectationPopupService } from './affectation-popup.service';
import { AffectationService } from './affectation.service';
import { Travaux, TravauxService } from '../travaux';
import { Chantier, ChantierService } from '../chantier';
import { Employe, EmployeService } from '../employe';
@Component({
  selector: 'jhi-affecter-ressource',
  templateUrl: './affecter-ressource.component.html',
  styles: []
})
export class AffecterRessourceComponent implements OnInit {
  affectation: Affectation;
  isSaving: boolean;

  travauxes: Travaux[];

  chantiers: Chantier[];

  employes: Employe[];
  dateDebutDp: any;
  dateFinDp: any;

  constructor(
      public activeModal: NgbActiveModal,
      private jhiAlertService: JhiAlertService,
      private affectationService: AffectationService,
      private travauxService: TravauxService,
      private chantierService: ChantierService,
      private employeService: EmployeService,
      private eventManager: JhiEventManager
  ) {
  }

  ngOnInit() {
      this.isSaving = false;
    //   this.travauxService.query()
    //       .subscribe((res: HttpResponse<Travaux[]>) => { this.travauxes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    //   this.chantierService.query()
    //       .subscribe((res: HttpResponse<Chantier[]>) => { this.chantiers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
      this.employeService.findEmploye(+sessionStorage.getItem('entreprise_id'))
          .subscribe((res: HttpResponse<Employe[]>) => { this.employes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
  }

  clear() {
      this.activeModal.dismiss('cancel');
  }

  save() {
      this.isSaving = true;
      if (this.affectation.id !== undefined) {
          this.subscribeToSaveResponse(
              this.affectationService.update(this.affectation));
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
      this.activeModal.dismiss(result);
  }

  private onSaveError() {
      this.isSaving = false;
  }

  private onError(error: any) {
      this.jhiAlertService.error(error.message, null, null);
  }

  trackTravauxById(index: number, item: Travaux) {
      return item.id;
  }

  trackChantierById(index: number, item: Chantier) {
      return item.id;
  }

  trackEmployeById(index: number, item: Employe) {
      return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
      if (selectedVals) {
          for (let i = 0; i < selectedVals.length; i++) {
              if (option.id === selectedVals[i].id) {
                  return selectedVals[i];
              }
          }
      }
      return option;
  }

}

@Component({
  selector: 'jhi-affectation-popup2',
  template: ''
})
export class AffectationPopup2Component implements OnInit, OnDestroy {

  routeSub: any;

  constructor(
      private route: ActivatedRoute,
      private affectationPopupService: AffectationPopupService
  ) {}

  ngOnInit() {
      this.routeSub = this.route.params.subscribe((params) => {
          if ( params['id'] ) {
              this.affectationPopupService
                  .open(AffecterRessourceComponent as Component, params['id']);
          } else {
              this.affectationPopupService
                  .open(AffecterRessourceComponent as Component);
          }
      });
  }

  ngOnDestroy() {
      this.routeSub.unsubscribe();
  }
}
