import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Devis } from './devis.model';
import { DevisPopupService } from './devis-popup.service';
import { DevisService } from './devis.service';
import { Client, ClientService } from '../client';
import { Chantier, ChantierService } from '../chantier';
import { LigneDevis, LigneDevisService } from '../ligne-devis';

@Component({
    selector: 'jhi-devis-dialog',
    templateUrl: './devis-dialog.component.html'
})
export class DevisDialogComponent implements OnInit {

    devis: Devis;
    lastdevis: Devis;
    isSaving: boolean;
    isSavingligne: boolean;
    update: boolean;

    clients: Client[];
    ligneDevis: any[];
    lastId: number;

    chantiers: Chantier[];
    dateDeCreationDp: any;
    listComponentDevis = [];
    ligneDevisASupprimer = [];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private devisService: DevisService,
        private clientService: ClientService,
        private chantierService: ChantierService,
        private ligneDevisService: LigneDevisService,
        private eventManager: JhiEventManager,
        private eventManagerligne: JhiEventManager
    ) {
    }

    ngOnInit() {
        const id = +sessionStorage.getItem('entreprise_id');
        this.isSaving = false;
        this.clientService.findClient(id)
            .subscribe((res: HttpResponse<Client[]>) => { this.clients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.chantierService.findChantier(id)
            .subscribe((res: HttpResponse<Chantier[]>) => { this.chantiers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
            if (this.devis.id !== undefined) {
                this.ligneDevisService.ligneDevisByDevisId(this.devis.id).subscribe((ligneDevisResponse: HttpResponse<LigneDevis[]>) => {
                            this.ligneDevis = ligneDevisResponse.body;
                            this.ligneDevis.map((res, i) => {
                                this.listComponentDevis.push({id: i, value: res});
                            });
                    });
                    this.update = true;
            }else {
                this.update = false;
            }
    }
    ajoutComposant() {
        this.listComponentDevis.push(
            {
                id: this.listComponentDevis.length + 1,
                value: new LigneDevis()
            }
        );
    }
    recieveData(event: any) {
        console.log(event);
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.devis.id !== undefined) {
            this.subscribeToSaveResponse(
                this.devisService.update(this.devis));
        } else {
            this.subscribeToSaveResponse(
                this.devisService.create(this.devis));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Devis>>) {
        result.subscribe((res: HttpResponse<Devis>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private  onSaveSuccess(result: Devis) {
        this.eventManager.broadcast({ name: 'devisListModification', content: 'OK'});
        if (this.update) {
            this.listComponentDevis.map((res) => {
                if (res.value.devisId === undefined) {
                    res.value.devisId = this.devis.id;
                }
                this.saveligne(res.value);
            });
            this.ligneDevisASupprimer.map((res1) => {
              if (res1[0].value.id !== undefined) {
                this.ligneDevisService.delete(res1[0].value.id).subscribe((response) => {
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

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    trackChantierById(index: number, item: Chantier) {
        return item.id;
    }

     dernierId(result) {
       this.devisService.lastId().subscribe((res: HttpResponse<number>) => {
            this.lastId = res.body;
            this.isSaving = false;
            this.listComponentDevis.map((res1) => {
                    res1.value.devisId = this.lastId;
                    this.saveligne(res1.value);
            });
        this.activeModal.dismiss(result);
        });
    }
    removeComponent(id) {
        this.listComponentDevis.map((res, i) => {
            if (res.id === id) {
                this.ligneDevisASupprimer.push(this.listComponentDevis.splice(i, 1));
            }
        });
    }
    saveligne(ligneDevis: LigneDevis) {
        this.isSavingligne = true;
        if (ligneDevis.id !== undefined) {
            this.subscribeToSaveResponseligne(
                this.ligneDevisService.update(ligneDevis));
        } else {
            this.subscribeToSaveResponseligne(
                this.ligneDevisService.create(ligneDevis));
        }
    }

    private subscribeToSaveResponseligne(result: Observable<HttpResponse<LigneDevis>>) {
        result.subscribe((res: HttpResponse<LigneDevis>) =>
            this.onSaveSuccessligne(res.body), (res: HttpErrorResponse) => this.onSaveErrorligne());
    }

    private onSaveSuccessligne(result: LigneDevis) {
        this.eventManagerligne.broadcast({ name: 'ligneDevisListModification', content: 'OK'});
        this.isSavingligne = false;
    }

    private onSaveErrorligne() {
        this.isSavingligne = false;
    }

}

@Component({
    selector: 'jhi-devis-popup',
    template: ''
})
export class DevisPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private devisPopupService: DevisPopupService
    ) {}
    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.devisPopupService
                    .open(DevisDialogComponent as Component, params['id']);
            } else {
                this.devisPopupService
                    .open(DevisDialogComponent as Component);
            }
        });
    }
    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
