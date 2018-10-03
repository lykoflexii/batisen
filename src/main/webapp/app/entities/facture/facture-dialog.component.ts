import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Facture } from './facture.model';
import { FacturePopupService } from './facture-popup.service';
import { FactureService } from './facture.service';
import { Chantier, ChantierService } from '../chantier';
import { Travaux, TravauxService } from '../travaux';
import { Entreprise, EntrepriseService } from '../entreprise';
import { LigneFactureService, LigneFacture } from '../ligne-facture';
import { AffectationService, Affectation } from '../affectation';
import { Affectation2 } from '../affectation/affectations.model';

@Component({
    selector: 'jhi-facture-dialog',
    templateUrl: './facture-dialog.component.html'
})
export class FactureDialogComponent implements OnInit {

    facture: Facture;
    isSaving: boolean;
    isSavingligne: boolean;
    update: boolean;
    ligneFacture: any[];
    lastId: number;

    chantiers: Chantier[];

    travauxes: Travaux[];

    entreprises: Entreprise[];
    dateCreationDp: any;
    validiteDp: any;
    listComponentFacture = [];
    ligneFactureASupprimer = [];
    affectation: Affectation2[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private factureService: FactureService,
        private chantierService: ChantierService,
        private travauxService: TravauxService,
        private entrepriseService: EntrepriseService,
        private eventManager: JhiEventManager,
        private ligneFactureService: LigneFactureService,
        private eventManagerligne: JhiEventManager,
        private affectationService: AffectationService
    ) {
    }

    ngOnInit() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.isSaving = false;
        this.chantierService.findChantier(id)
            .subscribe((res: HttpResponse<Chantier[]>) => { this.chantiers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.travauxService.query()
            .subscribe((res: HttpResponse<Travaux[]>) => { this.travauxes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        // this.entrepriseService.query()
        //     .subscribe((res: HttpResponse<Entreprise[]>) => { this.entreprises = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        // this.dernierId();
        if (this.facture.id !== undefined) {
            this.ligneFactureService.ligneFactureByFactureId(this.facture.id).subscribe((ligneFactureResponse: HttpResponse<LigneFacture[]>) => {
                        this.ligneFacture = ligneFactureResponse.body;
                        this.ligneFacture.map((res, i) => {
                            this.listComponentFacture.push({id: i, value: res});
                        });
                });
                this.update = true;
        }else {
            this.update = false;
        }
        }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        // if (!this.compareDate( this.facture.dateCreation , this.facture.validite )) {
        //     alert('La date de fin doit etre supperieur à la date de debut');
        //     this.onSaveError();
        //     this.jhiAlertService.error('the start date must be less than the end date', null, null);
        //    // this.router.navigate( ['chantiers/' + +sessionStorage.getItem('idClient')] );
        // } else {
                this.isSaving = true;
                if (this.facture.id !== undefined) {
                    this.subscribeToSaveResponse(
                        this.factureService.update(this.facture));
                } else {
                    this.facture.entrepriseId = +sessionStorage.getItem('entreprise_id');
                    this.subscribeToSaveResponse(
                        this.factureService.create(this.facture));
                }
            //  }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Facture>>) {
        result.subscribe((res: HttpResponse<Facture>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Facture) {
        this.eventManager.broadcast({ name: 'factureListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
        if (this.update) {
            this.listComponentFacture.map((res) => {
                if (res.value.factureId === undefined) {
                    res.value.factureId = this.facture.id;
                }
                this.saveligne(res.value);
            });
            this.ligneFactureASupprimer.map((res1) => {
              if (res1[0].value.id !== undefined) {
                this.ligneFactureService.delete(res1[0].value.id).subscribe((response) => {
                });
              }
            });
             this.activeModal.dismiss(result);
        }else {
            this.dernierId(result);
        }
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackChantierById(index: number, item: Chantier) {
        return item.id;
    }

    trackTravauxById(index: number, item: Travaux) {
        return item.id;
    }

    trackEntrepriseById(index: number, item: Entreprise) {
        return item.id;
    }
     dernierId(result) {
        const id = +sessionStorage.getItem('entreprise_id');
        this.factureService.lastId(this.facture.chantierId, this.facture.travauxId).subscribe((res: HttpResponse<number>) => {
             this.lastId = res.body;
             this.isSaving = false;
             this.listComponentFacture.map((res1) => {
                     res1.value.factureId = this.lastId;
                     this.saveligne(res1.value);
             });
         this.activeModal.dismiss(result);
         });
     }
     ajoutComposant() {
        this.listComponentFacture.push(
            {
                id: this.listComponentFacture.length + 1,
                value: new LigneFacture()
            }
        );
    }
    removeComponent(id) {
        this.listComponentFacture.map((res, i) => {
            if (res.id === id) {
                this.ligneFactureASupprimer.push(this.listComponentFacture.splice(i, 1));
            }
        });
    }
    saveligne(ligneFacture: LigneFacture) {
        this.isSavingligne = true;
        if (ligneFacture.id !== undefined) {
            this.subscribeToSaveResponseligne(
                this.ligneFactureService.update(ligneFacture));
        } else {
            this.subscribeToSaveResponseligne(
                this.ligneFactureService.create(ligneFacture));
        }
    }
    private subscribeToSaveResponseligne(result: Observable<HttpResponse<LigneFacture>>) {
        result.subscribe((res: HttpResponse<LigneFacture>) =>
            this.onSaveSuccessligne(res.body), (res: HttpErrorResponse) => this.onSaveErrorligne());
    }

    private onSaveSuccessligne(result: LigneFacture) {
        this.eventManagerligne.broadcast({ name: 'ligneDevisListModification', content: 'OK'});
        this.isSavingligne = false;
    }

    private onSaveErrorligne() {
        this.isSavingligne = false;
    }
    chargerTravaux() {
        const id = this.facture.chantierId;
        this.affectationService.findByChantier2(id)
        .subscribe((res: HttpResponse<Affectation2[]>) => {
            this.affectation = res.body;
            this.travauxes = [];
            this.affectation.map((res1) => {
               this.travauxes.push(res1.travaux);
            });
        }, (res: HttpErrorResponse) => this.onError(res.message));
    }
    compareDate(date1: any, date2: any) {
        const a1 = +date1['year'];
        const b1 = +date2['year'];
        const a2 = +date1['month'];
        const b2 = +date2['month'];
        const a3 = +date1['day'];
        const b3 = +date2['day'];
        if ( a1 <= b1 ) {
            if ( a2 <= b2 ) {
                if ( a3 <= b3 ) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

@Component({
    selector: 'jhi-facture-popup',
    template: ''
})
export class FacturePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private facturePopupService: FacturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.facturePopupService
                    .open(FactureDialogComponent as Component, params['id']);
            } else {
                this.facturePopupService
                    .open(FactureDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
