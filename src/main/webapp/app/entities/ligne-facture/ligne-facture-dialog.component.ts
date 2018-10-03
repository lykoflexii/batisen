import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LigneFacture } from './ligne-facture.model';
import { LigneFacturePopupService } from './ligne-facture-popup.service';
import { LigneFactureService } from './ligne-facture.service';
import { Facture, FactureService } from '../facture';

@Component({
    selector: 'jhi-ligne-facture-dialog',
    templateUrl: './ligne-facture-dialog.component.html'
})
export class LigneFactureDialogComponent implements OnInit {

    ligneFacture: LigneFacture;
    isSaving: boolean;

    factures: Facture[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ligneFactureService: LigneFactureService,
        private factureService: FactureService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.factureService.query()
            .subscribe((res: HttpResponse<Facture[]>) => { this.factures = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ligneFacture.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ligneFactureService.update(this.ligneFacture));
        } else {
            this.subscribeToSaveResponse(
                this.ligneFactureService.create(this.ligneFacture));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<LigneFacture>>) {
        result.subscribe((res: HttpResponse<LigneFacture>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: LigneFacture) {
        this.eventManager.broadcast({ name: 'ligneFactureListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackFactureById(index: number, item: Facture) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-ligne-facture-popup',
    template: ''
})
export class LigneFacturePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ligneFacturePopupService: LigneFacturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ligneFacturePopupService
                    .open(LigneFactureDialogComponent as Component, params['id']);
            } else {
                this.ligneFacturePopupService
                    .open(LigneFactureDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
