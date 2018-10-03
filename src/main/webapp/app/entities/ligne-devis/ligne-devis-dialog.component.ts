import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LigneDevis } from './ligne-devis.model';
import { LigneDevisPopupService } from './ligne-devis-popup.service';
import { LigneDevisService } from './ligne-devis.service';
import { Devis, DevisService } from '../devis';

@Component({
    selector: 'jhi-ligne-devis-dialog',
    templateUrl: './ligne-devis-dialog.component.html'
})
export class LigneDevisDialogComponent implements OnInit {

    ligneDevis: LigneDevis;
    isSaving: boolean;

    devis: Devis[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ligneDevisService: LigneDevisService,
        private devisService: DevisService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.devisService.query()
            .subscribe((res: HttpResponse<Devis[]>) => { this.devis = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ligneDevis.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ligneDevisService.update(this.ligneDevis));
        } else {
            this.subscribeToSaveResponse(
                this.ligneDevisService.create(this.ligneDevis));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<LigneDevis>>) {
        result.subscribe((res: HttpResponse<LigneDevis>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: LigneDevis) {
        this.eventManager.broadcast({ name: 'ligneDevisListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDevisById(index: number, item: Devis) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-ligne-devis-popup',
    template: ''
})
export class LigneDevisPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ligneDevisPopupService: LigneDevisPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ligneDevisPopupService
                    .open(LigneDevisDialogComponent as Component, params['id']);
            } else {
                this.ligneDevisPopupService
                    .open(LigneDevisDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
